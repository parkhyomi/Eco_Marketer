//package com.example.digital_contest.API.Login
//
//import com.example.digital_contest.API.LevelExperience
//import com.example.digital_contest.API.LoginBackendResponse
//import com.example.digital_contest.API.UserName
//import com.example.digital_contest.API.colorDataResponse
//import com.example.digital_contest.API.tradeDataResponse
//import retrofit2.http.POST
//import retrofit2.Call
//import retrofit2.http.DELETE
//import retrofit2.http.GET
//import retrofit2.http.Header
//
//interface LoginService {
//    @DELETE("/api/auth/logout")
//    fun deleteToken(
//        @Header("Authorization") token:String
//    ): Call<Void>
//
//    @POST("/api/auth/login/kakao")
//    fun postAccessToken(
//        @Header("Authorization") token: String
//        //@Body jsonParams : UserModel
//
//    ): Call<LoginBackendResponse>
//
//    //거래 횟수 조회에 대한 API 연결
//    @GET("/api/level/secondhand-trade/count")
//    fun getTradeCountData(
//        @Header("Authorization") accessToken: String
//    ): Call<tradeDataResponse>
//
//    //레벨 및 경험치에 대한 API 연결
//    @GET("/api/level/information")
//    fun levelexperience(
//        @Header("Authorization") accessToken: String
//    ):Call<LevelExperience>
//
//    @GET("/api/level/color")
//    fun getlevelcolor(
//        @Header("Authorization") accessToken: String
//    ): Call<colorDataResponse>
//
//    @GET("/api/user/nickname")
//    fun username(
//    @Header("Authorization")accessToken : String
//    ): Call<UserName>
//
//}