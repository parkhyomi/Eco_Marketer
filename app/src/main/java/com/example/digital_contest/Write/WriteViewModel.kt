package com.example.digital_contest.Write

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.digital_contest.API.APIRetrofit
import com.example.digital_contest.Login.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WriteUiState {
    object Idle : WriteUiState()
    object Loading : WriteUiState()
    data class Success(val introduceText: String) : WriteUiState()
    data class Error(val message: String) : WriteUiState()
}

sealed class ProductPlusState {
    object Idle : ProductPlusState()
    object Loading : ProductPlusState()
    object Success : ProductPlusState()
    data class Error(val message: String) : ProductPlusState()
}

class WriteViewModel(
    private val repository: WriteRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _writeUiState = MutableStateFlow<WriteUiState>(WriteUiState.Idle)
    val writeUiState: StateFlow<WriteUiState> = _writeUiState.asStateFlow()

    private val _productPlusState = MutableStateFlow<ProductPlusState>(ProductPlusState.Idle)
    val productPlusState: StateFlow<ProductPlusState> = _productPlusState.asStateFlow()

    private val _imageUri = MutableStateFlow<Uri?>(null)

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _price = MutableStateFlow("")
    val price: StateFlow<String> = _price.asStateFlow()

    private val _selectedPlatform = MutableStateFlow("")
    val selectedPlatform: StateFlow<String> = _selectedPlatform.asStateFlow()

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedConcept = MutableStateFlow("")
    val selectedConcept: StateFlow<String> = _selectedConcept.asStateFlow()

    private val _generatedText = MutableStateFlow("")
    val generatedText: StateFlow<String> = _generatedText.asStateFlow()

    private val _selectedPlatforms = MutableStateFlow<List<String>>(emptyList())

    fun updateImageUri(uri: Uri?) { _imageUri.value = uri }
    fun updateTitle(value: String) { _title.value = value }
    fun updatePrice(value: String) { _price.value = value }
    fun updatePlatform(value: String) { _selectedPlatform.value = value }
    fun updateCategory(value: String) { _selectedCategory.value = value }
    fun updateConcept(value: String) { _selectedConcept.value = value }

    fun uploadProduct() {
        viewModelScope.launch {
            try {
                _writeUiState.value = WriteUiState.Loading

                val imageUri = _imageUri.value
                if (imageUri == null) {
                    _writeUiState.value = WriteUiState.Error("이미지를 선택해주세요")
                    return@launch
                }

                if (_title.value.isEmpty()) {
                    _writeUiState.value = WriteUiState.Error("제목을 입력해주세요")
                    return@launch
                }

                if (_price.value.isEmpty()) {
                    _writeUiState.value = WriteUiState.Error("가격을 입력해주세요")
                    return@launch
                }

                if (!tokenManager.hasValidToken()) {
                    _writeUiState.value = WriteUiState.Error("로그인이 필요합니다")
                    return@launch
                }

                val token = tokenManager.getAccessToken() ?: run {
                    _writeUiState.value = WriteUiState.Error("로그인이 필요합니다")
                    return@launch
                }

                repository.uploadProduct(
                    token = token,
                    imageUri = imageUri,
                    title = _title.value,
                    price = _price.value,
                    platform = _selectedPlatform.value,
                    category = _selectedCategory.value,
                    concept = _selectedConcept.value
                ).onSuccess { introduceText ->
                    _generatedText.value = introduceText
                    _writeUiState.value = WriteUiState.Success(introduceText)
                }.onFailure { exception ->
                    _writeUiState.value = WriteUiState.Error(exception.message ?: "오류 발생")
                }
            } catch (e: Exception) {
                _writeUiState.value = WriteUiState.Error(e.message ?: "오류 발생")
            }
        }
    }

    fun productPlus() {
        viewModelScope.launch {
            try {
                _productPlusState.value = ProductPlusState.Loading

                val imageUri = _imageUri.value
                if (imageUri == null) {
                    _productPlusState.value = ProductPlusState.Error("이미지를 선택해주세요")
                    return@launch
                }

                if (_generatedText.value.isEmpty()) {
                    _productPlusState.value = ProductPlusState.Error("먼저 게시글을 생성해주세요")
                    return@launch
                }

                if (_selectedPlatforms.value.isEmpty()) {
                    _productPlusState.value = ProductPlusState.Error("플랫폼을 선택해주세요")
                    return@launch
                }

                if (!tokenManager.hasValidToken()) {
                    _productPlusState.value = ProductPlusState.Error("로그인이 필요합니다")
                    return@launch
                }

                val token = tokenManager.getAccessToken() ?: run {
                    _productPlusState.value = ProductPlusState.Error("로그인이 필요합니다")
                    return@launch
                }

                repository.addProductToOtherPlatforms(
                    token = token,
                    imageUri = imageUri,
                    title = _title.value,
                    price = _price.value,
                    concept = _selectedConcept.value,
                    category = _selectedCategory.value,
                    generatedText = _generatedText.value,
                    selectedPlatforms = _selectedPlatforms.value
                ).onSuccess {
                    _productPlusState.value = ProductPlusState.Success
                }.onFailure { exception ->
                    _productPlusState.value = ProductPlusState.Error(exception.message ?: "오류 발생")
                }
            } catch (e: Exception) {
                _productPlusState.value = ProductPlusState.Error(e.message ?: "오류 발생")
            }
        }
    }

    fun resetState() {
        _writeUiState.value = WriteUiState.Idle
        _productPlusState.value = ProductPlusState.Idle
    }
}

class WriteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val writeService = APIRetrofit.createService<WriteService>()
            val repository = WriteRepository(writeService)
            val tokenManager = TokenManager(context)

            return WriteViewModel(repository, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
