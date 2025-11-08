package com.example.digital_contest.API.Mypage

import android.content.Context
import android.util.Log
import com.example.digital_contest.API.Manager.MyPageManager
import com.example.digital_contest.API.Manager.TokenManager
import com.example.digital_contest.API.RetrofitHelper
import com.example.digital_contest.API.WriteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

suspend fun MyPageProduct(context: Context, state: Boolean?) {

    val retrofit: Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)

    val api: MypageService = retrofit.create(MypageService::class.java)
    val tokenManager = TokenManager(context)
    val myPageManager = MyPageManager(context)

    val accessToken = tokenManager.getAccessToken() ?: return
    try {
        val response = withContext(Dispatchers.IO) {
            api.getproduct(accessToken, state).execute()
        }
        if (response.isSuccessful) {
            val productData = response.body()?.data
            if (productData != null) {
                Log.d("내용", "저장된 데이터: $productData")
                myPageManager.saveAllProductindex(productData, state)
                val products = myPageManager.getProductData(state)
                Log.d("저장된내용", "저장된 데이터: $products")
                //updateProductState(state, products)
            } else {
                Log.e("에러", "$productData")
            }

        } else {
            Log.e(
                "ViewModel",
                "Error loading products for state: $state, code: ${response.code()}"
            )
        }
    } catch (e: Exception) {
        Log.e("ViewModel", "Error loading products for state: $state", e)
    }
}