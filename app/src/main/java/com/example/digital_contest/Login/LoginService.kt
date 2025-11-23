package com.example.digital_contest.Login

import android.util.Log
import com.example.digital_contest.API.APIRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiService {

    // 카카오 토큰으로 백엔드 로그인
    @POST("/auth/login/kakao")
    suspend fun loginWithKakao(
        @Header("Authorization") accessToken: String
    ): Response<LoginData>

    // 액세스 토큰 재발급 (Request Body 방식)
    @POST("/auth/token-reissue")
    suspend fun reissueToken(
        @Body request: RefreshTokenRequest
    ): Response<LoginData>

    // 로그아웃
    @DELETE("/auth/logout")
    suspend fun logout(
        @Header("Authorization") accessToken: String
    ): Response<Void>
}

class LoginRepository {

    private val api: LoginApiService = APIRetrofit.createService()
    val TAG = "KakaoLogin"

    /**
     * 카카오 로그인
     */
    suspend fun loginWithKakao(kakaoAccessToken: String): LoginResult {
        return withContext(Dispatchers.IO) {
            try {

                val response = api.loginWithKakao(kakaoAccessToken)

                if (response.isSuccessful && response.body() != null) {
                    val loginData = response.body()!!

                    Log.d(TAG, "로그인 성공")
                    LoginResult.Success(loginData)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "알 수 없는 오류"
                    Log.e(TAG, "로그인 실패 (${response.code()}): $errorBody")
                    LoginResult.Error("로그인 실패 (${response.code()}): $errorBody")
                }
            }catch (e: Exception) {
                Log.e(TAG, "네트워크 오류: ${e.javaClass.simpleName} - ${e.message}", e)
                e.printStackTrace()
                LoginResult.Error("네트워크 오류: ${e.javaClass.simpleName} - ${e.message}")
            }
        }
    }

    /**
     * 토큰 재발급
     */
    suspend fun reissueToken(refreshToken: String): LoginResult {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "토큰 재발급 시작")
                Log.d(TAG, "RefreshToken 길이: ${refreshToken.length}")
                val response = api.reissueToken(RefreshTokenRequest(refreshToken))

                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "토큰 재발급 성공")
                    LoginResult.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "알 수 없는 오류"
                    Log.e(TAG, "토큰 재발급 실패 (${response.code()}): $errorBody")
                    LoginResult.Error("토큰 재발급 실패 (${response.code()}): $errorBody")
                }
            } catch (e: Exception) {
                Log.e(TAG, "토큰 재발급 오류: ${e.javaClass.simpleName} - ${e.message}", e)
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
                val response = api.logout("Bearer $accessToken")
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
