package com.example.digital_contest.API

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    val BASE_URL: String = "http://52.78.105.220:3000/api/"
    var gson = GsonBuilder().setLenient().create()

    fun getRetrofitInstance(java: Class<WriteService>): Retrofit {
        val client=OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5,TimeUnit.MINUTES)
            .writeTimeout(5,TimeUnit.MINUTES)
                .build()
        val builder: Retrofit.Builder = Retrofit.Builder()
        val retrofit = builder.baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        return retrofit
    }
}