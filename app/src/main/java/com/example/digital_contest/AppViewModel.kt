package com.example.digital_contest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_contest.Login.TokenManager
import com.example.digital_contest.Login.AuthDataStore
import com.example.digital_contest.onboarding.OnboardingDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 1. 앱 시작 화면 결정
 *    - 온보딩 미완료 → Onboarding
 *    - 온보딩 완료 + 로그인 안됨 → Login
 *    - 온보딩 완료 + 로그인 됨 → Main
 * 2. 앱 시작 시 토큰 자동 갱신
 */
class AppViewModel(context: Context) : ViewModel() {

    private val onboardingDataStore = OnboardingDataStore(context)
    private val authDataStore = AuthDataStore(context)
    private val tokenManager = TokenManager(context)
    private val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    private val _isTokenChecking = MutableStateFlow(true)

    init {
        // 앱 시작 시 토큰 체크 및 갱신
        viewModelScope.launch {
            val isOnboardingCompleted = onboardingDataStore.isCompleted.stateIn(viewModelScope).value
            val hasToken = tokenManager.hasValidToken()

            // 온보딩 완료 + 토큰이 있으면 자동 갱신 시도
            if (isOnboardingCompleted && hasToken) {
                tokenManager.checkAndRefreshToken()
            }

            _isTokenChecking.value = false
        }
    }

    /**
     * 앱 시작 화면 결정
     * splash → onboarding / login / main
     */
    val startDestination: StateFlow<String> = combine(
        onboardingDataStore.isCompleted,
        authDataStore.isLoggedIn,
        _isTokenChecking
    ) { isOnboardingCompleted, isLoggedIn, isChecking ->

        // 온보딩 미완료 → onboarding
        if (!isOnboardingCompleted) {
            return@combine "onboarding"
        }

        // 토큰 체크 중
        if (isChecking) {
            return@combine "login"
        }

        val hasSharedPrefToken = sharedPreferences.getString("accessToken", null)?.isNotEmpty() == true

        if (isLoggedIn || hasSharedPrefToken) {
            return@combine "main"
        }
        // 로그인 안됨 → login
        "login"

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "login"
    )
}



