package com.example.digital_contest.Login

data class LoginBackendResponse(
    val data: LoginData
)

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val role: String
)

sealed class LoginResult {
    data class Success(val loginData: LoginData) : LoginResult()
    data class Error(val message: String) : LoginResult()
}


sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val loginData: LoginData) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

