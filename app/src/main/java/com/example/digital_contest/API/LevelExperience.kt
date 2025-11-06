package com.example.digital_contest.API

import android.content.Context
import android.util.Log
import com.example.digital_contest.API.Manager.LevelManager
import com.example.digital_contest.API.Manager.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

fun LeverExperience(context:Context){
    var retrofit : Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
    var api : LoginService = retrofit.create(LoginService::class.java)
    val tokenManager = TokenManager(context)
    val levelManager = LevelManager(context)

    CoroutineScope(Dispatchers.IO).launch{
        //val tokenManager = TokenManager(context)
        val accessToken = tokenManager.getAccessToken()
        //val levelManager = LevelManager(context)

        if(accessToken!=null){
        api.levelexperience(accessToken).enqueue(object :retrofit2.Callback<LevelExperience>{
            override fun onResponse(
                call: Call<LevelExperience>,
                response: Response<LevelExperience>,
            ) {
                Log.d("통신", response.toString())
                Log.d("통신완료","${response.body().toString()}")
            when (response.code()){
                200-> {
                    val response = response.body()
                    Log.d("정상호출","${response}")
                    if(response !=null) {
                            val levelexperience = response.data.levelExperience
                            val myexperience =  response.data.myLevelExperience
                            val mylevel = response.data.myLevel

                            CoroutineScope(Dispatchers.IO).launch {
                                levelManager.saveLevelExperience(levelexperience)
                                levelManager.saveMyExperience(myexperience)
                                levelManager.saveMyLevel(mylevel)

                                val a = levelManager.myExperience.first()
                                val b = levelManager.myLevel.first()
                                val c = levelManager.levelExperience.first()

                                Log.d("총 경험치", "${a}" )
                                Log.d("현재 경험치", "${b}" )
                                Log.d("현재 레벨", "${c}" )

                            }
                        }
                    }
                else -> {
                    Log.d("비정상","${response.body().toString()}")
                }
                }
            }

            override fun onFailure(call: Call<LevelExperience>, t: Throwable) {
                Log.d("통신 실패","실패")
            }
        })
        }
    }
}