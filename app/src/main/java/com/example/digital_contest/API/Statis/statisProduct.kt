package com.example.digital_contest.API.Statis

import android.content.Context
import android.util.Log
import com.example.digital_contest.API.Manager.TokenManager
import com.example.digital_contest.API.RetrofitHelper
import com.example.digital_contest.API.WriteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


suspend fun callPlatformStatis(context: Context, kind: String) {
    val retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
    val service = retrofit.create(StaticService::class.java)
    val tokenManager = TokenManager(context)
    val accessToken = tokenManager.getAccessToken() ?: return

    val statisDataStore = StatisDataStore(context)

    try {
        val response = withContext(Dispatchers.IO) {
            service.platformStatis(accessToken, kind).execute()
        }
        if (response.isSuccessful) {
            Log.d("API_RESPONSE", "저장된 데이터: ${response.body()}")
            val data = response.body()?.data
            if (data != null) {
                Log.d("API_RESPONSE", "저장된 데이터: $data")
                when (kind) {
                    "platform-whole" -> statisDataStore.saveTotalData(data)
                    "platform-mine" -> statisDataStore.saveMyData(data)
                }
                val products = when (kind) {
                    "platform-whole" -> statisDataStore.totalData.first()
                    "platform-mine" -> statisDataStore.myData.first()
                    else -> emptyList()
                }
                Log.d("저장된내용", "저장된 데이터: $products")
            } else {
                Log.e("에러", "데이터 없음")
            }
        } else {
            Log.e("API_ERROR", "Error: ${response.code()}")
        }
    } catch (e: Exception) {
        Log.e("API_ERROR", "Error: ${e.message}")
    }
}

suspend fun callCategoryStatis(context: Context, kind: String) {
    val retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
    val service = retrofit.create(StaticService::class.java)
    val tokenManager = TokenManager(context)
    val accessToken = tokenManager.getAccessToken() ?: return

    val statisDataStore = StatisDataStore(context)

    try {
        val response = withContext(Dispatchers.IO) {
            service.categoryStatis(accessToken, kind).execute()
        }
        if (response.isSuccessful) {
            Log.d("API_RESPONSE", "저장된 데이터: ${response.body()}")
            val data = response.body()?.data
            if (data != null) {
                Log.d("API_RESPONSE", "저장된 데이터: $data")
                when (kind) {
                    "category-whole" -> statisDataStore.savecategoryData(data)
                    "category-mine" -> statisDataStore.saveMycategoryData(data)
                }
                val products = when (kind) {
                    "category-whole" -> statisDataStore.totalCategoryData.first()
                    "category-mine" -> statisDataStore.myCategoryData.first()
                    else -> emptyList()
                }
                Log.d("저장된내용", "저장된 데이터: $products")
            } else {
                Log.e("에러", "데이터 없음")
            }
        } else {
            Log.e("API_ERROR", "Error: ${response.code()}")
        }
    } catch (e: Exception) {
        Log.e("API_ERROR", "Error: ${e.message}")
    }
}

suspend fun callPlatformDetailStatis(context: Context, kind: String) {
    val retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
    val service = retrofit.create(StaticService::class.java)
    val tokenManager = TokenManager(context)
    val accessToken = tokenManager.getAccessToken() ?: return

    val statisDataStore = StatisDataStore(context)

    try {
        val response = withContext(Dispatchers.IO) {
            service.platformdetail(accessToken, kind).execute()
        }
        if (response.isSuccessful) {
            Log.d("API_RESPONSE_Detail", "저장된 데이터: ${response.body()}")
            val data = response.body()//?.data
            if (data != null) {

                Log.d("API_RESPONSE_Detail", "저장된 데이터: $data")
                when (kind) {
                    "platform-whole" -> statisDataStore.saveCompanyDetailData(data)
                    "platform-mine" -> statisDataStore.saveMyDetailData(data)
                }
                val products = when (kind) {
                    "platform-whole" -> statisDataStore.companyDetailData.first()
                    "platform-mine" -> statisDataStore.mycompanyDetailData.first()
                    else -> null
                }
                Log.d("저장된내용", "저장된 데이터: $products")
            } else {
                Log.e("에러", "데이터 없음")
            }
        } else {
            Log.e("API_ERROR", "Error: ${response.code()}")
        }
    } catch (e: Exception) {
        Log.e("API_ERROR", "Error: ${e.message}")
    }
}

suspend fun callCategoryDetailStatis(context: Context, kind: String) {
    val retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
    val service = retrofit.create(StaticService::class.java)
    val tokenManager = TokenManager(context)
    val accessToken = tokenManager.getAccessToken() ?: return

    val statisDataStore = StatisDataStore(context)

    try {
        val response = withContext(Dispatchers.IO) {
            service.categorydetail(accessToken, kind).execute()
        }
        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                Log.d("API_RESPONSE", "저장된 데이터: $data")
                when (kind) {
                    "category-whole" -> statisDataStore.saveCategoryDetailData(data)
                    "category-mine" -> statisDataStore.saveMyCategoryDetailData(data)
                }
                val products = when (kind) {
                    "category-whole" -> statisDataStore.categoryDetailData.first()
                    "category-mine" -> statisDataStore.mycategoryDetailData.first()
                    else -> null
                }
                Log.d("저장된내용", "저장된 데이터: $products")
            } else {
                Log.e("에러", "데이터 없음")
            }
        } else {
            Log.e("API_ERROR", "Error: ${response.code()}")
        }
    } catch (e: Exception) {
        Log.e("API_ERROR", "Error: ${e.message}")
    }
}