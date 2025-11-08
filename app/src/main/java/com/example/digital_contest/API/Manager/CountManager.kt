//package com.example.digital_contest.API.Manager
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.intPreferencesKey
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.first
//
//class CountManager(private val context: Context) {
//    companion object {
//        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "count_data")
//        private val COUNT_KEY = intPreferencesKey("count")
//        private val COLOR_KEY = stringPreferencesKey("color")
//    }
//
//    // count 값을 Flow로 제공
//    val count: Flow<Int>
//        get() = context.dataStore.data.map { preferences ->
//            preferences[COUNT_KEY] ?: 0 // 기본값을 0으로 설정
//        }
//
//    val color:Flow<String?>
//        get() = context.dataStore.data.map { preferences ->
//            preferences[COLOR_KEY]
//        }
//
//    // count 값을 저장
//    suspend fun saveCount(count: Int) {
//        context.dataStore.edit { preferences ->
//            preferences[COUNT_KEY] = count
//        }
//    }
//
//    // count 값을 즉시 가져오기
//    suspend fun getCount(): Int {
//        val preferences = context.dataStore.data.first()
//        return preferences[COUNT_KEY] ?: 0 // 기본값을 0으로 설정
//    }
//
//    suspend fun savecolor(color: String) {
//        context.dataStore.edit { preferences ->
//            preferences[COLOR_KEY] = color
//        }
//    }
//}