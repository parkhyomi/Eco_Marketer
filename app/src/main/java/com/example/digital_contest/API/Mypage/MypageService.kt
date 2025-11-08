//package com.example.digital_contest.API.Mypage
//
//import com.example.digital_contest.API.ProductData
//import retrofit2.Call
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.Header
//import retrofit2.http.PATCH
//import retrofit2.http.Query
//
//data class productStatus(
//    val  productId:Int, //상품id작성
//    val status:Boolean //현 상태를 작성 (null-판매중,true-판매완료,false- 판매실패)
//)
//interface MypageService {
//    @PATCH("/api/product/status") //판매id로 상태바꾸기
//    fun productstatus(
//        @Header("Authorization") token:String, //토큰
//        @Body request: productStatus
//    ):Call<Void> //돌아오는 값이 없음
//
//    @GET("/api/product") //현재 상품 리스트를 상태별 전부 가져오기
//    fun getproduct(
//        @Header("Authorization") token:String, //토큰
//        @Query("status") state:Boolean? //null-판매중,true-판매완료,false- 판매실패
//    ):Call<ProductData>
//
//}