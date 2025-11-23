package com.example.digital_contest.Login

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.authDataStore by preferencesDataStore(name = "auth_preferences")

class AuthDataStore(private val context: Context) {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val EXPIRES_IN_KEY = stringPreferencesKey("expiresIn")
    }

    /**
     * 로그인 상태 확인
     */
    val isLoggedIn: Flow<Boolean> = context.authDataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]?.isNotEmpty() == true
    }

    /**
     * 액세스 토큰 가져오기
     */
    val accessToken: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    /**
     * 리프레시 토큰 가져오기
     */
    val refreshToken: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    /**
     * 로그인 데이터 저장
     */
    suspend fun saveLoginData(accessToken: String, refreshToken: String, expiresIn: String) {
        context.authDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[EXPIRES_IN_KEY] = expiresIn
        }
    }


    /**
     * 로그아웃
     */
    suspend fun clear() {
        context.authDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

