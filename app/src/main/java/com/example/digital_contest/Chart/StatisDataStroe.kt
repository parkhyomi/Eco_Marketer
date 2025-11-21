package com.example.digital_contest.API.Data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.digital_contest.Chart.CategoryData
import com.example.digital_contest.Chart.CategoryDetail
import com.example.digital_contest.Chart.CompanyData
import com.example.digital_contest.Chart.CompanyDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * 통계 데이터 저장소 (DataStore Pattern)
 * SRP: 데이터 저장/조회만 담당
 */

// DataStore 정의
private val Context.totalCategoryDataStore: DataStore<Preferences> by preferencesDataStore(name = "total_category_data")
private val Context.myCategoryDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_category_data")
private val Context.companyDataStore: DataStore<Preferences> by preferencesDataStore(name = "company_data")
private val Context.myCompanyDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_company_data")
private val Context.companyDetailDataStore: DataStore<Preferences> by preferencesDataStore(name = "company_detail_data")
private val Context.myCompanyDetailDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_company_detail_data")
private val Context.categoryDetailDataStore: DataStore<Preferences> by preferencesDataStore(name = "category_detail_data")
private val Context.myCategoryDetailDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_category_detail_data")

class StatisDataStore(private val context: Context) {
    companion object {
        private val COMPANY_KEY = stringPreferencesKey("company")
        private val CATEGORY_KEY = stringPreferencesKey("category")
        private val COMPANY_DETAIL_KEY = stringPreferencesKey("company_detail")
        private val CATEGORY_DETAIL_KEY = stringPreferencesKey("category_detail")

        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    // ========== 저장 함수들 ==========

    suspend fun saveTotalData(data: List<CompanyData>) {
        try {
            val jsonString = json.encodeToString(data)
            context.companyDataStore.edit { it[COMPANY_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    suspend fun saveMyData(data: List<CompanyData>) {
        try {
            val jsonString = json.encodeToString(data)
            context.myCompanyDataStore.edit { it[COMPANY_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    suspend fun saveTotalCategoryData(data: List<CategoryData>) {
        try {
            val jsonString = json.encodeToString(data)
            context.totalCategoryDataStore.edit { it[CATEGORY_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    suspend fun saveMyCategoryData(data: List<CategoryData>) {
        try {
            val jsonString = json.encodeToString(data)
            context.myCategoryDataStore.edit { it[CATEGORY_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    suspend fun saveCompanyDetailData(data: CompanyDetail) {
        try {
            val jsonString = json.encodeToString(data)
            context.companyDetailDataStore.edit { it[COMPANY_DETAIL_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    suspend fun saveMyCompanyDetailData(data: CompanyDetail) {
        try {
            val jsonString = json.encodeToString(data)
            context.myCompanyDetailDataStore.edit { it[COMPANY_DETAIL_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    suspend fun saveCategoryDetailData(data: CategoryDetail) {
        try {
            val jsonString = json.encodeToString(data)
            context.categoryDetailDataStore.edit { it[CATEGORY_DETAIL_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    suspend fun saveMyCategoryDetailData(data: CategoryDetail) {
        try {
            val jsonString = json.encodeToString(data)
            context.myCategoryDetailDataStore.edit { it[CATEGORY_DETAIL_KEY] = jsonString }
        } catch (e: Exception) {
            Log.e("StatisDataStore", "저장 실패: ${e.message}")
        }
    }

    // ========== Flow 데이터 읽기 ==========

    val totalData: Flow<List<CompanyData>> = context.companyDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_KEY] ?: "[]"
            try {
                json.decodeFromString<List<CompanyData>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }

    val myData: Flow<List<CompanyData>> = context.myCompanyDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_KEY] ?: "[]"
            try {
                json.decodeFromString<List<CompanyData>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }

    val totalCategoryData: Flow<List<CategoryData>> = context.totalCategoryDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_KEY] ?: "[]"
            try {
                json.decodeFromString<List<CategoryData>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }

    val myCategoryData: Flow<List<CategoryData>> = context.myCategoryDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_KEY] ?: "[]"
            try {
                json.decodeFromString<List<CategoryData>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }

    val companyDetailData: Flow<CompanyDetail> = context.companyDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_DETAIL_KEY] ?: "{\"data\":[]}"
            try {
                json.decodeFromString<CompanyDetail>(jsonString)
            } catch (e: Exception) {
                CompanyDetail(emptyList())
            }
        }

    val mycompanyDetailData: Flow<CompanyDetail> = context.myCompanyDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_DETAIL_KEY] ?: "{\"data\":[]}"
            try {
                json.decodeFromString<CompanyDetail>(jsonString)
            } catch (e: Exception) {
                CompanyDetail(emptyList())
            }
        }

    val categoryDetailData: Flow<CategoryDetail> = context.categoryDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_DETAIL_KEY] ?: "{\"data\":[]}"
            try {
                json.decodeFromString<CategoryDetail>(jsonString)
            } catch (e: Exception) {
                CategoryDetail(emptyList())
            }
        }

    val mycategoryDetailData: Flow<CategoryDetail> = context.myCategoryDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_DETAIL_KEY] ?: "{\"data\":[]}"
            try {
                json.decodeFromString<CategoryDetail>(jsonString)
            } catch (e: Exception) {
                CategoryDetail(emptyList())
            }
        }
}

