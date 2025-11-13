package com.example.digital_contest.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OnboardingViewModel(context: Context) : ViewModel() {

    private val dataStore = OnboardingDataStore(context)

    /**
     * 온보딩 완료 처리
     */
    fun saveCompleted() {
        viewModelScope.launch {
            dataStore.saveCompleted()
        }
    }
}
