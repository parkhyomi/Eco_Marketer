package com.example.digital_contest.Main

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

// 거래 횟수 데이터 응답을 위한 데이터 클래스
data class tradeDataResponse(
    val data: tradeData
)

data class tradeData(
    val count: Int
)

data class colorDataResponse(
    val data: colorData
)

data class colorData(
    val color: String
)

//레벨 및 경험치 데이터 클래스
data class LevelExperience(
    val data: levelexperiencedata
)

//레벨 및 경험치 데이터 자료
data class levelexperiencedata(
    val levelExperience: Int,
    val myLevelExperience: Int,
    val myLevel: Int
)

interface MainService {
    //거래 횟수 조회에 대한 API 연결
    @GET("/api/level/secondhand-trade/count")
    fun getTradeCountData(
        @Header("Authorization") accessToken: String
    ): Call<tradeDataResponse>

    //레벨 및 경험치에 대한 API 연결
    @GET("/api/level/information")
    fun levelexperience(
        @Header("Authorization") accessToken: String
    ): Call<LevelExperience>

    @GET("/api/level/color")
    fun getlevelcolor(
        @Header("Authorization") accessToken: String
    ): Call<colorDataResponse>
}

