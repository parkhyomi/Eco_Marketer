//package com.example.digital_contest.API.Static
//
//import com.example.digital_contest.API.CategoryDetail
//import com.example.digital_contest.API.CategoryStatisResponse
//import com.example.digital_contest.API.CompanyDetail
//import com.example.digital_contest.API.CompanyStatisResponse
//import retrofit2.Call
//import retrofit2.http.GET
//import retrofit2.http.Header
//import retrofit2.http.Path
//
//interface StaticService {
//    @GET("/api/satisfaction/platform/{kind}")
//    fun platformStatis(
//        @Header("Authorization") token: String,
//        @Path("kind") kind: String
//    ): Call<CompanyStatisResponse>
//
//    @GET("/api/satisfaction/category/{kind}")
//    fun categoryStatis(
//        @Header("Authorization") token:String,
//        @Path("kind") kind:String
//    ):Call<CategoryStatisResponse>
//
//    @GET("/api/satisfaction/platform/detail/{kind}")
//    fun platformdetail(
//        @Header("Authorization") token: String,
//        @Path("kind") kind: String
//    ): Call<CompanyDetail>
//
//    @GET("/api/satisfaction/category/detail/{kind}")
//    fun categorydetail(
//        @Header("Authorization") token:String,
//        @Path("kind") kind:String
//    ):Call<CategoryDetail>
//
//}

////거래 횟수 조회에 대한 API 연결
//@GET("/api/level/secondhand-trade/count")
//fun getTradeCountData(
//    @Header("Authorization") accessToken: String
//): Call<tradeDataResponse>
//
////레벨 및 경험치에 대한 API 연결
//@GET("/api/level/information")
//fun levelexperience(
//    @Header("Authorization") accessToken: String
//): Call<LevelExperience>
//
//@GET("/api/level/color")
//fun getlevelcolor(
//    @Header("Authorization") accessToken: String
//): Call<colorDataResponse>
//
//@GET("/api/user/nickname")
//fun username(
//    @Header("Authorization")accessToken : String
//): Call<UserName>