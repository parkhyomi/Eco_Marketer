package com.example.digital_contest.Market_Price

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class MarketPriceResponse(
    val data: List<MarketPriceData>
)

data class MarketPriceData(
    val company: String,
    val image: String,
    val title: String,
    val price: String
)

interface MarketPriceService {
    @GET("/quotation")
    fun getMarketPrices(
        @Header("Authorization") accessToken: String,
        @Query("item") query: String
    ): Call<MarketPriceResponse>
}