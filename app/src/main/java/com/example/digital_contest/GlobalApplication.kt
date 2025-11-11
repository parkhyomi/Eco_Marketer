package com.example.digital_contest

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication:Application() {
    override fun onCreate(){
        super.onCreate()
        //카카오sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY) //키는 바꿀예정
    }
}