package com.example.digital_contest.Main

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

// 거래 횟수 데이터 응답을 위한 데이터 클래스
data class tradeDataResponse(
    @SerializedName("data")
    val data: tradeData
)

data class tradeData(
    @SerializedName("count")
    val count: Int
)

data class colorDataResponse(
    @SerializedName("data")
    val data: colorData
)

data class colorData(
    @SerializedName("color")
    val color: String
)

//레벨 및 경험치 데이터 클래스
data class LevelExperience(
    @SerializedName("data")
    val data: levelexperiencedata
)

//레벨 및 경험치 데이터 자료
data class levelexperiencedata(
    @SerializedName("levelExperience")
    val levelExperience: Int,
    @SerializedName("myLevelExperience")
    val myLevelExperience: Int,
    @SerializedName("myLevel")
    val myLevel: Int
)

interface MainService {
    //거래 횟수 조회에 대한 API 연결
    @GET("/level/secondhand-trade/count")
    suspend fun getTradeCountData(
        @Header("Authorization") accessToken: String
    ): Response<tradeDataResponse>

    //레벨 및 경험치에 대한 API 연결
    @GET("/level/information")
    suspend fun levelexperience(
        @Header("Authorization") accessToken: String
    ): Response<LevelExperience>

    @GET("/level/color")
    suspend fun getlevelcolor(
        @Header("Authorization") accessToken: String
    ): Response<colorDataResponse>
}

