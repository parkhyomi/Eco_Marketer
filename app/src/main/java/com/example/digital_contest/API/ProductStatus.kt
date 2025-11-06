package com.example.digital_contest.API

import android.content.Context
import android.util.Log
import com.example.digital_contest.API.Manager.TokenManager
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

suspend fun ProductStatus(context: Context, productid: Int, status: Boolean) {
    val retrofit: Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
    val api: MypageService = retrofit.create(MypageService::class.java)
    val tokenManager = TokenManager(context)
    val token = tokenManager.getAccessToken()
    if (token != null) {
        val request = productStatus(productid, status) // 요청 객체 이름 확인
        api.productstatus(token, request).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when (response.code()) {
                    200 -> {
                        Log.d("물품 id", "물품 id")
                    }

                    500 -> {
                        Log.d("물품 상태 실패(서버)", "물품 상태 실패(서버)")
                    }

                    else -> {
                        Log.d("상태확인", "${response.errorBody()?.string()}")
                        Log.d("물품 상태 실패", "실패${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("통신 오류", "오류 ${t.message}")
            }
        })
    }
}
