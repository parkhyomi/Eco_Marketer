package com.example.digital_contest.Write

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// 게시글 생성 응답 데이터
data class IntroduceCreateResponse(
    val data: IntroduceData
)

data class IntroduceData(
    val introduceText: String,
    val price: Int
)

// 게시글 추가 응답 데이터
data class IntroducePlusResponse(
    val message: String?,
    val success: Boolean
)

interface WriteService {
    // 게시글 생성 API
    @Multipart
    @POST("/api/introduce/text")
    suspend fun uploadProduct(
        @Header("Authorization") token: String,
        @Part files: MultipartBody.Part,
        @Part("introduceCategory") introduceCategory: RequestBody,
        @Part("price") price: RequestBody,
        @Part("product") product: RequestBody,
        @Part("productCategory") productCategory: RequestBody
    ): Response<IntroduceCreateResponse>

    // 물품 등록 API
    @Multipart
    @POST("/api/product")
    suspend fun productplus(
        @Header("Authorization") token: String,
        @Part files: MultipartBody.Part,
        @Part("introduceCategory") introduceCategory: RequestBody,
        @Part("productCategory") productCategory: RequestBody,
        @Part("price") price: RequestBody,
        @Part("product") product: RequestBody,
        @Part("introduceText") introduceText: RequestBody,
        @Part companys: List<MultipartBody.Part>
    ): Response<IntroducePlusResponse>
}