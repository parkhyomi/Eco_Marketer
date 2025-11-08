//package com.example.digital_contest.API.Manager
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//
//class UserManager(private val context: Context){
//    companion object{
//        private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "name_data")
//        private val NICKNAME_KEY = stringPreferencesKey("nickname")
//    }
//
//    val nickname: Flow<String?> = context.dataStore.data
//        .map { preferences ->
//            preferences[NICKNAME_KEY]
//        }
//    suspend fun saveNickname(nickname: String) {
//        context.dataStore.edit { preferences ->
//            preferences[NICKNAME_KEY] = nickname
//        }
//    }
//}