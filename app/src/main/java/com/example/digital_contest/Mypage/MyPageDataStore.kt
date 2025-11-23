package com.example.digital_contest.Mypage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// DataStore를 싱글톤으로 관리
private val Context.productDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_product_manager")
private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "name_data")

/**
 * MyPage용 DataStore 싱글톤 접근자
 */
object MyPageDataStore {
    fun getProductDataStore(context: Context): DataStore<Preferences> {
        return context.productDataStore
    }

    fun getUserDataStore(context: Context): DataStore<Preferences> {
        return context.userDataStore
    }
}

