package com.example.digital_contest.API.Main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository (private val datastore: DataStore<Preferences>){
    private object PreferencesKeys{ //datastore에 사용할 키를 정의
        val ONBOARDING= booleanPreferencesKey("onboarding_completed")

    }
    //온보딩상태를flow로서 실시간으로 체크,즉 감시
    val onboardingState: Flow<Boolean> =datastore.data.map {preferences ->
        preferences[PreferencesKeys.ONBOARDING] ?:false
    }
    //온보딩 상태를 저장하는 함수
    suspend fun saveOnboardingstate(complted:Boolean){
        datastore.edit {preferences ->
            preferences[PreferencesKeys.ONBOARDING]=complted
        }
    }

}