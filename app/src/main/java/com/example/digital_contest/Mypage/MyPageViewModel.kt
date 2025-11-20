package com.example.digital_contest.Mypage

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.digital_contest.Login.AuthDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val repository: MyPageRepository
) : ViewModel() {

    private val _onSaleProducts = MutableStateFlow<List<ProductData>>(emptyList())
    val onSaleProducts: StateFlow<List<ProductData>> = _onSaleProducts.asStateFlow()

    private val _completedProducts = MutableStateFlow<List<ProductData>>(emptyList())
    val completedProducts: StateFlow<List<ProductData>> = _completedProducts.asStateFlow()

    private val _failedProducts = MutableStateFlow<List<ProductData>>(emptyList())
    val failedProducts: StateFlow<List<ProductData>> = _failedProducts.asStateFlow()

    private val _userNickname = MutableStateFlow("")
    val userNickname: StateFlow<String> = _userNickname.asStateFlow()

    /**
     * 모든 상품 로드 (판매중, 판매완료, 판매실패)
     */
    fun loadAllProducts() {
        viewModelScope.launch {
            try {
                Log.d("MyPageViewModel", "Loading all products...")
                loadProductsByStatus(ProductStatus.ON_SALE)
                loadProductsByStatus(ProductStatus.COMPLETED)
                loadProductsByStatus(ProductStatus.FAILED)
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "Error loading all products", e)
            }
        }
    }

    /**
     * 상태별 상품 로드
     */
    private suspend fun loadProductsByStatus(status: ProductStatus) {
        try {
            val products = repository.getProductsByStatus(status)
            updateProductsState(status, products)
            Log.d("MyPageViewModel", "Loaded ${products.size} products for status: $status")
        } catch (e: Exception) {
            Log.e("MyPageViewModel", "Error loading products for status: $status", e)
        }
    }

    /**
     * 상품 상태 업데이트
     */
    private fun updateProductsState(status: ProductStatus, products: List<ProductData>) {
        when (status) {
            ProductStatus.ON_SALE -> {
                _onSaleProducts.value = products.sortedBy { it.productId }
            }
            ProductStatus.COMPLETED -> {
                _completedProducts.value = products.sortedBy { it.productId }
            }
            ProductStatus.FAILED -> {
                _failedProducts.value = products.sortedBy { it.productId }
            }
        }
    }

    /**
     * 상품 상태 변경 (판매완료/판매실패)
     */
    fun updateProductStatus(productId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateProductStatus(productId, isCompleted)
                moveProductToNewStatus(productId, isCompleted)
                Log.d("MyPageViewModel", "Product $productId status updated to ${if (isCompleted) "completed" else "failed"}")
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "Error updating product status", e)
            }
        }
    }

    /**
     * 로컬 상태에서 상품 이동
     */
    private fun moveProductToNewStatus(productId: Int, isCompleted: Boolean) {
        val product = _onSaleProducts.value.find { it.productId == productId } ?: return

        _onSaleProducts.value = _onSaleProducts.value.filter { it.productId != productId }
        if (isCompleted) {
            _completedProducts.value = (_completedProducts.value + product).sortedBy { it.productId }
        } else {
            _failedProducts.value = (_failedProducts.value + product).sortedBy { it.productId }
        }
    }

    /**
     * 사용자 닉네임 로드
     */
    fun loadUserNickname() {
        viewModelScope.launch {
            try {
                val nickname = repository.getUserNickname()
                _userNickname.value = nickname
                Log.d("MyPageViewModel", "Loaded nickname: $nickname")
            } catch (e: Exception) {
                _userNickname.value = "사용자"
                Log.e("MyPageViewModel", "Error loading nickname", e)
            }
        }
    }
}

class MyPageViewModelFactory(
    private val context: Context,
    private val authDataStore: AuthDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            val repository = MyPageRepositoryImpl(context, authDataStore)
            @Suppress("UNCHECKED_CAST")
            return MyPageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}