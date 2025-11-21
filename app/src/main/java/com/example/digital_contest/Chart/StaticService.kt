package com.example.digital_contest.Chart

import com.example.digital_contest.Chart.models.StatisDetail
import com.example.digital_contest.Chart.models.StatisResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

// 통합된 Retrofit 서비스 인터페이스
interface StaticService {
    @GET("/api/satisfaction/platform/{kind}")
    fun platformStatis(
        @Header("Authorization") token: String,
        @Path("kind") kind: String
    ): Call<StatisResponse>

    @GET("/api/satisfaction/category/{kind}")
    fun categoryStatis(
        @Header("Authorization") token: String,
        @Path("kind") kind: String
    ): Call<StatisResponse>

    @GET("/api/satisfaction/platform/detail/{kind}")
    fun platformdetail(
        @Header("Authorization") token: String,
        @Path("kind") kind: String
    ): Call<StatisDetail>

    @GET("/api/satisfaction/category/detail/{kind}")
    fun categorydetail(
        @Header("Authorization") token: String,
        @Path("kind") kind: String
    ): Call<StatisDetail>
}

// 하위 호환성을 위한 타입 별칭
typealias CompanyData = com.example.digital_contest.Chart.models.StatisData
typealias CategoryData = com.example.digital_contest.Chart.models.StatisData
typealias CompanyDetail = StatisDetail
typealias CategoryDetail = StatisDetail