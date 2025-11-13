package com.example.digital_contest.Login

import com.example.digital_contest.API.APIRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.awaitResponse
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiService {

    // 카카오 토큰으로 백엔드 로그인
    @POST("/api/auth/login/kakao")
    fun loginWithKakao(
        @Header("Authorization") accessToken: String
    ): Call<LoginBackendResponse>

    // 액세스 토큰 재발급
    @POST("/auth/token-reissue")
    fun reissueToken(
        @Header("Authorization") accessToken: String,
        @Header("refresh") refreshToken: String
    ): Call<LoginBackendResponse>

    // 로그아웃
    @DELETE("/api/auth/logout")
    fun logout(
        @Header("Authorization") accessToken: String
    ): Call<Void>
}