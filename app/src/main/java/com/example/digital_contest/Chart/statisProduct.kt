package com.example.digital_contest.Chart

import android.content.Context
import android.util.Log
import com.example.digital_contest.API.APIRetrofit.retrofit
import com.example.digital_contest.API.Data.StatisDataStore
import com.example.digital_contest.Login.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// 플랫폼 통계 호출
suspend fun callPlatformStatis(context: Context, kind: String) {
    val dataStore = StatisDataStore(context)
    fetchAndSave(
        context = context,
        apiCall = { service, token ->
            service.platformStatis(token, kind).execute()
        },
        onSuccess = { data ->
            when (kind) {
                "platform-whole" -> dataStore.saveTotalData(data.data)
                "platform-mine" -> dataStore.saveMyData(data.data)
            }
        },
        logTag = "PlatformStatis"
    )
}

// 카테고리 통계 호출
suspend fun callCategoryStatis(context: Context, kind: String) {
    val dataStore = StatisDataStore(context)
    fetchAndSave(
        context = context,
        apiCall = { service, token ->
            service.categoryStatis(token, kind).execute()
        },
        onSuccess = { data ->
            when (kind) {
                "category-whole" -> dataStore.saveTotalCategoryData(data.data)
                "category-mine" -> dataStore.saveMyCategoryData(data.data)
            }
        },
        logTag = "CategoryStatis"
    )
}

// 플랫폼 상세 통계 호출
suspend fun callPlatformDetailStatis(context: Context, kind: String) {
    val dataStore = StatisDataStore(context)
    fetchAndSaveDetail(
        context = context,
        apiCall = { service, token ->
            service.platformdetail(token, kind).execute()
        },
        onSuccess = { data ->
            when (kind) {
                "platform-whole" -> dataStore.saveCompanyDetailData(data)
                "platform-mine" -> dataStore.saveMyCompanyDetailData(data)
            }
        },
        logTag = "PlatformDetail"
    )
}

// 카테고리 상세 통계 호출
suspend fun callCategoryDetailStatis(context: Context, kind: String) {
    val dataStore = StatisDataStore(context)
    fetchAndSaveDetail(
        context = context,
        apiCall = { service, token ->
            service.categorydetail(token, kind).execute()
        },
        onSuccess = { data ->
            when (kind) {
                "category-whole" -> dataStore.saveCategoryDetailData(data)
                "category-mine" -> dataStore.saveMyCategoryDetailData(data)
            }
        },
        logTag = "CategoryDetail"
    )
}


private suspend fun <T> fetchAndSave(
    context: Context,
    apiCall: (StaticService, String) -> retrofit2.Response<T>,
    onSuccess: suspend (T) -> Unit,
    logTag: String
) {
    val tokenManager = TokenManager(context)
    val token = tokenManager.getAccessToken() ?: run {
        Log.e(logTag, "토큰 없음")
        return
    }

    try {
        val service = retrofit.create(StaticService::class.java)
        val response = withContext(Dispatchers.IO) {
            apiCall(service, token)
        }
        if (response.isSuccessful) {
            response.body()?.let { data ->
                onSuccess(data)
                Log.d(logTag, "성공")
            } ?: Log.e(logTag, "데이터 없음")
        } else {
            Log.e(logTag, "에러: ${response.code()}")
        }
    } catch (e: Exception) {
        Log.e(logTag, "예외: ${e.message}")
    }
}

private suspend fun <T> fetchAndSaveDetail(
    context: Context,
    apiCall: (StaticService, String) -> retrofit2.Response<T>,
    onSuccess: suspend (T) -> Unit,
    logTag: String
) {
    fetchAndSave(context, apiCall, onSuccess, logTag)
}


