package com.example.digital_contest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.digital_contest.API.Manager.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

//
@Composable
fun SplashView(navController:NavHostController,viewModel: MainActivityViewModel) {


    BoxWithConstraints(
        modifier = Modifier  //부모 컴포저블에 의해 크기 조절을 위해 사용
            .fillMaxSize() //최대 크기
            .wrapContentSize(Alignment.Center)
    ) { //가운데 정렬

        val maxHeight = maxHeight //이미지 크기에 따른 높이
        val maxWidth = maxWidth    //이미지 크기에 폭
        val logoHeight = maxHeight * 0.7f     //크기 계산
        val logoWidth = maxWidth * 0.7f
        val smlogoHeight = maxHeight * 0.3f
        val smlogoWidth = maxWidth * 0.3f
        val toppadding_2 = maxHeight * 0.1f


        Column( //이미지를 차례대로 아래로 내리기 위해 사용
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.height(toppadding_2)) //첫번째 사진의 위에 크기를 조절해주기 위해 사용
            Image( //첫번째 이미지 삽입
                painter = painterResource(id = R.drawable.eco_marketer_logo1),
                contentDescription = null,
                modifier = Modifier //사진 크기 조절해주는 부분
                    .height(logoHeight)
                    .width(logoWidth)
            )

            Image( //아래 이미지 //아래 이미지
                painter = painterResource(id = R.drawable.eco_marketer_logo2),
                contentDescription = null,
                modifier = Modifier //가운데 정렬후 크기 조절
                    .align(Alignment.CenterHorizontally)
                    .height(smlogoHeight)
                    .width(smlogoWidth)
            )
        }
        val context = LocalContext.current
        LaunchedEffect(Unit) { //실행하는 부분
            delay(2000) //딜레이 후
            val onboardingCompleted = viewModel.onboardingState.first()
            val tokenManager = TokenManager(context)
            val accessToken = tokenManager.accessToken.first()

            when {
                !onboardingCompleted -> {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                !accessToken.isNullOrEmpty() -> {
                    Log.d("확인","${accessToken}")
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                else -> {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }

                    }
                }

            }
        }
    }
}


