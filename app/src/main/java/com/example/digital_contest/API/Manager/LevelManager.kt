//package com.example.digital_contest.API.Manager
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.intPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//class LevelManager(private val context: Context) {
//    companion object{
//        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "LevelExperience_data")
//        private val LEVELEXPERIENCE_KEY = intPreferencesKey("levelexperience")
//        private val MY_EXPERIENCE_KEY = intPreferencesKey("myexperience")
//        private val MY_LEVEL_KEY = intPreferencesKey("mylevel")
//    }
//    // 레벨 경험을 Flow로 가져오기
//    val levelExperience: Flow<Int> = context.dataStore.data
//        .map { preferences ->
//            preferences[LEVELEXPERIENCE_KEY] ?: 0 // 기본값 0
//        }
//
//    // 내 경험을 Flow로 가져오기
//    val myExperience: Flow<Int> = context.dataStore.data
//        .map { preferences ->
//            preferences[MY_EXPERIENCE_KEY] ?: 0 // 기본값 0
//        }
//
//    // 내 레벨을 Flow로 가져오기
//    val myLevel: Flow<Int> = context.dataStore.data
//        .map { preferences ->
//            preferences[MY_LEVEL_KEY] ?: 0 // 기본값 0
//        }
//
//    // 레벨 경험 저장하기
//    suspend fun saveLevelExperience(levelExperience: Int) {
//        context.dataStore.edit { preferences ->
//            preferences[LEVELEXPERIENCE_KEY] = levelExperience
//        }
//    }
//
//    // 내 경험 저장하기
//    suspend fun saveMyExperience(myExperience: Int) {
//        context.dataStore.edit { preferences ->
//            preferences[MY_EXPERIENCE_KEY] = myExperience
//        }
//    }
//
//    // 내 레벨 저장하기
//    suspend fun saveMyLevel(myLevel: Int) {
//        context.dataStore.edit { preferences ->
//            preferences[MY_LEVEL_KEY] = myLevel
//        }
//    }
//}