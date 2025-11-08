//package com.example.digital_contest.API.Main
//
//import android.content.Context
//import android.util.Log
//import com.example.digital_contest.API.Login.LoginService
//import com.example.digital_contest.API.Manager.CountManager
//import com.example.digital_contest.API.Manager.TokenManager
//import com.example.digital_contest.API.RetrofitHelper
//import com.example.digital_contest.API.WriteService
//import com.example.digital_contest.API.colorDataResponse
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Response
//import retrofit2.Retrofit
//
//fun UtilModel(context:Context) {
//    var retrofit:Retrofit= RetrofitHelper.getRetrofitInstance(WriteService::class.java)
//    var api: LoginService =retrofit.create(LoginService::class.java)
//
//    CoroutineScope(Dispatchers.IO).launch{
//        val tokenManager= TokenManager(context)
//        val accessToken=tokenManager.accessToken.first()
//        val countManager= CountManager(context)
//
//        if(accessToken != null){
//            api.getlevelcolor(accessToken)
//                .enqueue(object : retrofit2.Callback<colorDataResponse>{
//                    override fun onResponse(
//                        call: Call<colorDataResponse>,
//                        response: Response<colorDataResponse>
//                    ) {
//                        Log.d("통신 ok","${response.body().toString()}")
//                        when(response.code()){
//                            200->{
//                                Log.d("정상호출","yes")
//                                val response=response.body()
//                                if (response !=null){
//                                    val color=response.data.color
//                                    CoroutineScope(Dispatchers.IO).launch {
//                                        countManager.savecolor(color)
//                                    }
//
//                                }
//                            }else->{
//                            Log.d("비정상","${response.body().toString()}")
//                        }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<colorDataResponse>, t: Throwable) {
//                        Log.d("통신 실패","실패")
//                    }
//                })
//        }
//    }
//}