package com.example.digital_contest.Login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: LoginRepository = LoginRepository.getInstance()
) : ViewModel() {

    // UI 상태
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginWithKakao(context: Context) {
        _uiState.value = LoginUiState.Loading

        // 카카오 로그인 콜백
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> {
                    _uiState.value = LoginUiState.Error("카카오 로그인 실패: ${error.message}")
                }
                token != null -> {
                    loginToBackend(token.accessToken)
                }
            }
        }

        // 카카오톡 앱 또는 웹 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun loginToBackend(kakaoAccessToken: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            when (val result = repository.loginWithKakao(kakaoAccessToken)) {
                is LoginResult.Success -> {
                    _uiState.value = LoginUiState.Success(result.loginData)
                }
                is LoginResult.Error -> {
                    _uiState.value = LoginUiState.Error(result.message)
                }
            }
        }
    }

    /**
     * UI 상태 초기화
     */
    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }
}

class LoginViewModelFactory(
    private val repository: LoginRepository = LoginRepository.getInstance()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}




