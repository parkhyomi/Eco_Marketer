package com.example.digital_contest.Login

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("expiresIn")
    val expiresIn: Int?
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
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

