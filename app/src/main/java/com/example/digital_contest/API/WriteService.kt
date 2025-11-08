//package com.example.digital_contest.API
//
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//
////import okhttp3.Response
//import retrofit2.http.Body
//import retrofit2.http.Header
//import retrofit2.http.POST
//import retrofit2.http.Part
//import retrofit2.Response
//import retrofit2.http.Multipart
//import java.io.File
//
//interface WriteService {
//    @Multipart
//    @POST("/api/introduce/text")  //소개글 생성
//    suspend fun uploadProduct(
//        @Header("Authorization") token: String,
//        @Part files:MultipartBody.Part,//List<MultipartBody.Part>
//        @Part("introduceCategory") introduceCategory:RequestBody ,
//        @Part("price") price: RequestBody,
//        @Part("product") product:RequestBody,
//        @Part("productCategory") productCategory:RequestBody
//    ): Response <WritepostcreateData>
//
//    @Multipart
//    @POST("/api/product") //마이페이지에 추가.
//    suspend fun productplus(
//        @Header("Authorization") token:String,
//        @Part files: MultipartBody.Part,
//        @Part("introduceCategory") introduceCategory:RequestBody ,
//        @Part("productCategory") productCategory: RequestBody,
//        @Part("price") price: RequestBody,
//        @Part("product") product:RequestBody,
//        @Part("introduceText") introduceText:RequestBody,
//        @Part companys: List<MultipartBody.Part>
//    ):Response<Void>
//}