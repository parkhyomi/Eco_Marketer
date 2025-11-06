package com.example.digital_contest.API.Statis

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.digital_contest.API.CategoryData
import com.example.digital_contest.API.CategoryDetail
import com.example.digital_contest.API.CompanyData
import com.example.digital_contest.API.CompanyDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Top-level 프로퍼티로 DataStore 정의

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
            allowSpecialFloatingPointValues = true
            useAlternativeNames = false
        }
    }

    suspend fun saveTotalData(companyDataList: List<CompanyData>) {
        val jsonString = json.encodeToString(companyDataList)
        context.companyDataStore.edit { preferences ->
            preferences.clear() // 기존 데이터 삭제
            preferences[COMPANY_KEY] = jsonString
        }
        logSavedData(context.companyDataStore, "TOTAL_DATA_STORE")
    }

    // 내 통계 데이터 저장 및 로그 출력
    suspend fun saveMyData(companyDataList: List<CompanyData>) {
        val jsonString = json.encodeToString(companyDataList)
        context.myCompanyDataStore.edit { preferences ->
            preferences.clear() // 기존 데이터 삭제
            preferences[COMPANY_KEY] = jsonString
        }
        logSavedData(context.myCompanyDataStore, "MY_DATA_STORE")
    }

    // 전체 통계 데이터 저장 및 로그 출력
    suspend fun savecategoryData(CategoryDataList: List<CategoryData>) {
        val jsonString = json.encodeToString(CategoryDataList)
        context.totalCategoryDataStore.edit { preferences ->
            preferences.clear() // 기존 데이터 삭제
            preferences[CATEGORY_KEY] = jsonString
        }
        logSavedCategoryData(context.totalCategoryDataStore, "TOTAL_DATA_STORE")
    }

    // 내 통계 데이터 저장 및 로그 출력
    suspend fun saveMycategoryData(CategoryDataList: List<CategoryData>) {
        val jsonString = json.encodeToString(CategoryDataList)
        context.myCategoryDataStore.edit { preferences ->
            preferences.clear() // 기존 데이터 삭제
            preferences[CATEGORY_KEY] = jsonString
        }
        logSavedCategoryData(context.myCategoryDataStore, "MY_DATA_STORE")
    }

    private suspend fun logSavedData(dataStore: DataStore<Preferences>, tag: String) {
        val savedData = dataStore.data.first()
        val jsonString = savedData[COMPANY_KEY] ?: "[]"
        val companyDataList = json.decodeFromString<List<CompanyData>>(jsonString)
        companyDataList.forEach { companyData ->
            Log.d(tag, "Company: ${companyData.target}, Introduce: ${companyData.introduceTextCategory}, Count: ${companyData.introduceTextCategoryCount}")
        }
    }
    private suspend fun logSavedCategoryData(dataStore: DataStore<Preferences>, tag: String) {
        val savedData = dataStore.data.first()
        val jsonString = savedData[CATEGORY_KEY] ?: "[]"
        val categoryDataList = json.decodeFromString<List<CategoryData>>(jsonString)
        categoryDataList.forEach { categoryData ->
            Log.d(tag, "Category: ${categoryData.target}, Introduce: ${categoryData.introduceTextCategory}, Count: ${categoryData.introduceTextCategoryCount}")
        }
    }
    private suspend fun logSavedCompanyDetailData(dataStore: DataStore<Preferences>, tag: String) {
        val savedData = dataStore.data.first()
        val jsonString = savedData[COMPANY_DETAIL_KEY] ?: "{}"
        val companyDetail = json.decodeFromString<CompanyDetail>(jsonString)

        companyDetail.data.forEach { companyDetails ->
            Log.d(tag, "Company: ${companyDetails.target}")
            companyDetails.data.forEach { detailData ->
                Log.d(tag, "  Introduce: ${detailData.introduceTextCategory}, Count: ${detailData.introduceTextCategoryCount}")
            }
        }
    }

    private suspend fun logSavedCategoryDetailData(dataStore: DataStore<Preferences>, tag: String) {
        val savedData = dataStore.data.first()
        val jsonString = savedData[CATEGORY_DETAIL_KEY] ?: "{}"
        val companyDetail = json.decodeFromString<CategoryDetail>(jsonString)

        companyDetail.data.forEach { categoryDetails ->
            Log.d(tag, "Company: ${categoryDetails.target}")
            categoryDetails.data.forEach { detailData ->
                Log.d(tag, "  Introduce: ${detailData.introduceTextCategory}, Count: ${detailData.introduceTextCategoryCount}")
            }
        }
    }

    suspend fun saveCompanyDetailData(companyDetail: CompanyDetail) {
        val jsonString = json.encodeToString(CompanyDetail.serializer(), companyDetail)
        context.companyDetailDataStore.edit { preferences ->
            preferences.clear() // 기존 데이터 삭제
            preferences[COMPANY_DETAIL_KEY] = jsonString
        }
        logSavedCompanyDetailData(context.companyDetailDataStore, "COMPANY_DETAIL_DATA_STORE")
    }
    suspend fun saveMyDetailData(companyDetail: CompanyDetail) {
        val jsonString = json.encodeToString(CompanyDetail.serializer(), companyDetail)
        context.myCompanyDetailDataStore.edit { preferences ->
            preferences.clear() // 기존 데이터 삭제
            preferences[COMPANY_DETAIL_KEY] = jsonString
        }
        logSavedCompanyDetailData(context.myCompanyDetailDataStore, "MY_COMPANY_DETAIL_DATA_STORE")
    }

    suspend fun saveCategoryDetailData(categoryDetail: CategoryDetail) {
        try {
            val jsonString = json.encodeToString(CategoryDetail.serializer(), categoryDetail)
            context.categoryDetailDataStore.edit { preferences ->
                preferences.clear() // 기존 데이터 삭제
                preferences[CATEGORY_DETAIL_KEY] = jsonString
            }
            logSavedCategoryDetailData(context.categoryDetailDataStore, "CATEGORY_DETAIL_DATA_STORE")
        } catch (e: Exception) {
            Log.e("DataStore", "Error saving data: ${e.message}")
        }
    }

    suspend fun saveMyCategoryDetailData(categoryDetail: CategoryDetail) {
        try {
            val jsonString = json.encodeToString(CategoryDetail.serializer(), categoryDetail)
            context.myCategoryDetailDataStore.edit { preferences ->
                preferences.clear() // 기존 데이터 삭제
                preferences[CATEGORY_DETAIL_KEY] = jsonString
            }
            logSavedCategoryDetailData(context.myCategoryDetailDataStore, "MY_CATEGORY_DETAIL_DATA_STORE")
        } catch (e: Exception) {
            Log.e("DataStore", "Error saving data: ${e.message}")
        }
    }


    val companyDetailData: Flow<CompanyDetail> = context.companyDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_DETAIL_KEY] ?: "{}"
            Log.d("DataStore", "Stored JSONtotal: $jsonString")
            try {
                json.decodeFromString(CompanyDetail.serializer(), jsonString)
            } catch (e: Exception) {
                Log.e("DataStore", "Error decoding JSONerr: ${e.message}")
                CompanyDetail(emptyList())
            }
        }

    val mycompanyDetailData: Flow<CompanyDetail> = context.myCompanyDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_DETAIL_KEY] ?: "{}"
            Log.d("DataStore", "Stored JSONMy: $jsonString")
            try {
                json.decodeFromString(CompanyDetail.serializer(), jsonString)
            } catch (e: Exception) {
                Log.e("DataStore", "Error decoding JSONerr: ${e.message}")
                CompanyDetail(emptyList())
            }
        }
    val categoryDetailData: Flow<CategoryDetail> = context.categoryDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_DETAIL_KEY] ?: "{}"
            Log.d("DataStore", "Stored JSONtotal: $jsonString")
            try {
                json.decodeFromString(CategoryDetail.serializer(), jsonString)
            } catch (e: Exception) {
                Log.e("DataStore", "Error decoding JSONerr: ${e.message}")
                CategoryDetail(emptyList())
            }
        }

    val mycategoryDetailData: Flow<CategoryDetail> = context.myCategoryDetailDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_DETAIL_KEY] ?: "{}"
            Log.d("DataStore", "Stored JSONMy: $jsonString")
            try {
                json.decodeFromString(CategoryDetail.serializer(), jsonString)
            } catch (e: Exception) {
                Log.e("DataStore", "Error decoding JSONerr: ${e.message}")
                CategoryDetail(emptyList())
            }
        }

    // 전체 통계 데이터 읽기
    val totalData: Flow<List<CompanyData>> = context.companyDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_KEY] ?: "[]"
            json.decodeFromString(jsonString)
        }


    // 내 통계 데이터 읽기
    val myData: Flow<List<CompanyData>> = context.myCompanyDataStore.data
        .map { preferences ->
            val jsonString = preferences[COMPANY_KEY] ?: "[]"
            json.decodeFromString(jsonString)
        }
    val totalCategoryData: Flow<List<CategoryData>> = context.totalCategoryDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_KEY] ?: "[]"
            json.decodeFromString(jsonString)
        }

    val myCategoryData: Flow<List<CategoryData>> = context.myCategoryDataStore.data
        .map { preferences ->
            val jsonString = preferences[CATEGORY_KEY] ?: "[]"
            json.decodeFromString(jsonString)
        }
}

