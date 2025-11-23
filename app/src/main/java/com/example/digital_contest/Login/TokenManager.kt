package com.example.digital_contest.Login

import android.content.Context
import kotlinx.coroutines.flow.first

/**
 * 1. 앱 시작 시 토큰 체크
 * 2. 토큰 자동 갱신
 * 3. 만료된 토큰 정리
 */
class TokenManager(private val context: Context) {

    private val authDataStore = AuthDataStore(context)
    private val repository = LoginRepository.getInstance()
    private val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    suspend fun checkAndRefreshToken(): Boolean {
        // 저장된 토큰 가져오기
        val accessToken = authDataStore.accessToken.first()
        val refreshToken = authDataStore.refreshToken.first()

        // 토큰이 없으면 로그인 필요
        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            return false
        }

        // 토큰 재발급 시도
        return try {
            when (val result = repository.reissueToken(refreshToken)) {
                is LoginResult.Success -> {
                    // 1. DataStore에 새 토큰 저장
                    authDataStore.saveLoginData(
                        accessToken = result.loginData.accessToken!!,
                        refreshToken = result.loginData.refreshToken!!,
                        expiresIn = result.loginData.expiresIn!!.toString()
                    )

                    // 2. SharedPreferences에도 저장 (하위 호환성)
                    sharedPreferences.edit().apply {
                        putString("accessToken", result.loginData.accessToken)
                        putString("refreshToken", result.loginData.refreshToken)
                        putString("expiresIn", result.loginData.expiresIn.toString())
                        apply()
                    }

                    true
                }
                is LoginResult.Error -> {
                    // 재발급 실패 - 토큰 만료, 로그인 필요
                    clearAllTokens()
                    false
                }
            }
        } catch (e: Exception) {
            // 예외 발생 시 토큰 삭제
            clearAllTokens()
            false
        }
    }

    /**
     * 토큰 존재 여부만 체크 (갱신 X)
     */
    suspend fun hasValidToken(): Boolean {
        val accessToken = authDataStore.accessToken.first()
        val refreshToken = authDataStore.refreshToken.first()
        return !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
    }

    /**
     * Access Token 가져오기
     */
    suspend fun getAccessToken(): String? {
        return authDataStore.accessToken.first()
    }

    /**
     * 모든 토큰 삭제 (DataStore + SharedPreferences)
     */
    private suspend fun clearAllTokens() {
        authDataStore.clear()
        sharedPreferences.edit().clear().apply()
    }
}

