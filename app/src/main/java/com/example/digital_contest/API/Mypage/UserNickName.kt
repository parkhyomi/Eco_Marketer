//package com.example.digital_contest.API.Mypage
//
//import android.content.Context
//import android.util.Log
//import com.example.digital_contest.API.Login.LoginService
//import com.example.digital_contest.API.Manager.TokenManager
//import com.example.digital_contest.API.Manager.UserManager
//import com.example.digital_contest.API.RetrofitHelper
//import com.example.digital_contest.API.UserName
//import com.example.digital_contest.API.WriteService
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Response
//import retrofit2.Retrofit
//
//fun UserNickName (context: Context){
//    val retrofit:Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
//    val api : LoginService = retrofit.create(LoginService::class.java)
//    val tokenManager = TokenManager(context)
//    val UserManager = UserManager(context)
//
//    CoroutineScope(Dispatchers.IO).launch{
//        val accessToken = tokenManager.getAccessToken()
//
//        if(accessToken!=null){
//            api.username(accessToken).enqueue(object : retrofit2.Callback<UserName>{
//                override fun onResponse(call: Call<UserName>, response: Response<UserName>) {
//
//                    Log.d("통신 내용", response.toString())
//                    Log.d("통신 내용", response.body().toString())
//
//                when (response.code() ){
//                    200 -> {
//                        val response = response.body()
//                        Log.d("정상호출","${response}")
//                        if(response != null){
//                            val nickname = response.data.nickname
//
//                            CoroutineScope(Dispatchers.IO).launch {
//                                UserManager.saveNickname(nickname)
//
//                                val u = UserManager.nickname.first()
//                                Log.d("nick name","${u}")
//                            }
//                        }
//                    }else -> { Log.d("비정상 호출", "${response.body()}")}
//                }}
//
//                override fun onFailure(call: Call<UserName>, t: Throwable) {
//                    Log.d("통신 실패","실패")
//                }
//            })
//        }
//    }
//}
//
