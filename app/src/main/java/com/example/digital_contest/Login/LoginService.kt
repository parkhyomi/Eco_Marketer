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

class LoginRepository {

    private val api: LoginApiService = APIRetrofit.createService()

    /**
     * 카카오 로그인
     */
    suspend fun loginWithKakao(kakaoAccessToken: String): LoginResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.loginWithKakao("Bearer $kakaoAccessToken").awaitResponse()

                if (response.isSuccessful && response.body() != null) {
                    LoginResult.Success(response.body()!!.data)
                } else {
                    LoginResult.Error("로그인 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                LoginResult.Error("네트워크 오류: ${e.message}")
            }
        }
    }

    /**
     * 토큰 재발급
     */
    suspend fun reissueToken(accessToken: String, refreshToken: String): LoginResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.reissueToken(
                    "Bearer $accessToken",
                    "Bearer $refreshToken"
                ).awaitResponse()

                if (response.isSuccessful && response.body() != null) {
                    LoginResult.Success(response.body()!!.data)
                } else {
                    LoginResult.Error("토큰 재발급 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                LoginResult.Error("토큰 재발급 오류: ${e.message}")
            }
        }
    }

    /**
     * 로그아웃
     */
    suspend fun logout(accessToken: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.logout("Bearer $accessToken").awaitResponse()
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(): LoginRepository {
            return instance ?: synchronized(this) {
                instance ?: LoginRepository().also { instance = it }
            }
        }
    }
}
