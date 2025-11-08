//package com.example.digital_contest
//
//import android.content.ContentValues.TAG
//import android.content.Context
//import android.graphics.BitmapFactory
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.unit.dp
//
//import androidx.navigation.NavHostController
//import com.example.digital_contest.API.LoginBackendResponse
//import com.example.digital_contest.API.Login.LoginService
//import com.example.digital_contest.API.RetrofitHelper
//import com.example.digital_contest.API.Manager.TokenManager
//import com.example.digital_contest.API.WriteService
//
//import com.kakao.sdk.auth.model.OAuthToken
//import com.kakao.sdk.common.model.ClientError
//import com.kakao.sdk.common.model.ClientErrorCause
//import com.kakao.sdk.user.UserApiClient
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import retrofit2.Call
//import retrofit2.Response
//import retrofit2.Retrofit
//
//@Composable
//fun LoginView(navHostController: NavHostController) {
//    val context = LocalContext.current
//    val tokenManager = TokenManager(context)
//    var accessToken: String? = null
//
//    LaunchedEffect(Unit) {
//        accessToken = tokenManager.accessToken.first()
//        if (!accessToken.isNullOrEmpty()) {
//            navHostController.navigate("main") {
//                popUpTo("login") { inclusive = true }
//            }
//        }
//    }
//    BoxWithConstraints(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        val screenWidth=maxWidth
//        val screenHeight=maxHeight
//        val isPortarait = screenHeight > screenWidth
//
//        val imageBitmap= remember {
//            BitmapFactory.decodeResource(context.resources,R.drawable.eco_marketer_logo2)
//        }
//        val imageHeight1=imageBitmap.height
//        val imageWidth1=imageBitmap.width
//        val imageAspectRational =imageWidth1.toFloat()/imageHeight1.toFloat()
//        val density= LocalDensity.current
//        val imageSize = with(density) {
//            if (isPortarait) {
//                minOf(screenWidth * 0.8f, imageWidth1.toDp()) // 0.8f에서 0.9f로 증가
//            } else {
//                minOf(screenHeight * 0.6f * imageAspectRational, imageWidth1.toDp()) // 0.5f에서 0.6f로 증가
//            }
//        }
//        val spacing = screenHeight * 0.3f // 화면 높이의 10%를 간격으로 사용
////    val api=LoginService.create()
//        Column(
//            modifier = Modifier.fillMaxSize(), //화면 전체크기
//            horizontalAlignment = Alignment.CenterHorizontally, //요소들 정렬
//
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Box(modifier = Modifier
//                    .padding(bottom = 16.dp)
//                    .fillMaxWidth()) {
////                Text(text = stringResource(id = R.string.login_help_with_logo)
////                    , fontFamily = namun, color = Color(0xFF0F7E00),modifier=Modifier ) //제일위 텍스트
//                    Image(
//                        painter = painterResource(id = R.drawable.eco_mark_ment),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .padding(top = imageSize * 0.17f, end = imageSize * 0.05f)
//                            .size(imageSize * 0.65f)
//                    )
//                    Image(
//                        painter = painterResource(id = R.drawable.eco_marketer_logo2) //로고 이미지를의미
//                        , contentDescription = null, modifier = Modifier
//                            .padding(top = 20.dp)
//                            .align(Alignment.Center)
//                            .size(imageSize)
//                    )
//                }
//
//                Text(
//                    text = stringResource(
//                        id = R.string.login_help_1,
//                    ),
//                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),//가운데 텍스트들과 정렬
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//
//                Text(
//                    text = stringResource(id = R.string.login_help_2),
//                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(spacing))
//            Column (horizontalAlignment=Alignment.CenterHorizontally,
//                modifier=Modifier.padding(bottom=32.dp)){
//                Text(
//                    text = stringResource(id = R.string.login_help_with_kakao),
//                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
//                    color = Color(0xFF0F7E00),
//                    modifier = Modifier.padding(bottom = 20.dp)
//                )
//                Spacer(modifier = Modifier.height(27.dp))
//                Box( //이미지를 감싸 누를시
//                    modifier = Modifier
//                        .height(50.dp)
//                        .fillMaxWidth()
//
//                        //.clickable(onClick = { kakaoLogin(context = context) }) //로그인 과정이 실행
//                        .clickable {
//                            kakaoLogin(context) { success ->
//                                if (success) {
//                                    navHostController.navigate("main") {
//                                        popUpTo("login") { inclusive = true }
//                                    }
//
//                                }
//                            }
//                        }
//
//
//                ) {
//                    Image( //카카오 이미지
//                        painter = painterResource(id = R.drawable.kakao_login_logo_1),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Fit
//                    )
//                }
//
//                //Spacer(modifier = Modifier.height(78.dp))
//
//            }
//        }
//    }
//}
//
//
//fun kakaoLogin(context: Context, onLoginResult: (Boolean) -> Unit) {
//    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//        if (error != null) {
//            Log.e(TAG, "카카오 로그인 실패", error)
//            onLoginResult(false)
//        } else if (token != null) {
//            Log.i(TAG, "카카오 로그인 성공 ${token.accessToken}")
//            Log.i(TAG, "RefreshToken: ${token.refreshToken}")
//            Log.i(TAG, "AccessTokenExpiresAt: ${token.accessTokenExpiresAt}")
//            Log.i(TAG, "RefreshTokenExpiresAt: ${token.refreshTokenExpiresAt}")
//            processKakaoToken(context, token, onLoginResult)
//
//            UserApiClient.instance.me { user, error ->
//                Log.i("유저 정보","$user")
//             }
//        }
//    }
//
//    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
//        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
//            if (error != null) {
//                Log.e(TAG, "카카오톡 로그인 실패", error)
//                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                    return@loginWithKakaoTalk
//                }
//                // 카카오톡 로그인 실패 시 카카오 계정으로 로그인 시도
//                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//            } else if (token != null) {
//                Log.i(TAG, "카카오톡 로그인 성공 ${token.accessToken}")
//                UserApiClient.instance.me { user, error ->
//                    Log.i("유저 정보","$user")
//                }
//                processKakaoToken(context, token, onLoginResult)
//                UserApiClient.instance.me { user, error -> Log.d("유저정보","$user") }
//            }
//        }
//    } else {
//        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//    }
//}
//
//private fun processKakaoToken(
//    context: Context,
//    token: OAuthToken,
//    onLoginResult: (Boolean) -> Unit
//) {
//    var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
//    var api: LoginService = retrofit.create(LoginService::class.java)
//
//
//    api.postAccessToken(token.accessToken)
//        .enqueue(object : retrofit2.Callback<LoginBackendResponse> {
//            override fun onResponse(
//                call: Call<LoginBackendResponse>,
//                response: Response<LoginBackendResponse>
//            ) {
//                Log.d("로그인 통신 성공", response.toString())
//                Log.d("로그인 통신 성공", response.body().toString())
//
//                when (response.code()) {
//                    200 -> {
//                        Log.d("로그인 성공", "ggg")
//
//                        val responseBody = response.body()
//                        if (responseBody != null) {
//                            val accessToken = responseBody.data.accessToken
//                            val refreshToken = responseBody.data.refreshToken
//
//                            val tokenManager = TokenManager(context)
//                            CoroutineScope(Dispatchers.IO).launch {
//                                tokenManager.saveTokens(accessToken, refreshToken)
//
//                                val savedAccessToken = tokenManager.accessToken.first()
//                                val savedRefreshToken = tokenManager.refreshToken.first()
//
//                                Log.d("TokenManager", "Saved Access Token: $savedAccessToken")
//                                Log.d("TokenManager", "Saved Refresh Token: $savedRefreshToken")
//
//                                withContext(Dispatchers.Main) {
//                                    onLoginResult(true)
//                                }
//                            }
//                        } else {
//                            onLoginResult(false)
//                        }
//                    }
//
//                    else -> {
//                        Log.d("로그인 실패", "로그인 실패: ${response.code()}")
//                        onLoginResult(false)
//                    }
//                }
//            }
//
//
//            override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
//                Log.d("통신 로그인..", "전송 실패")
//                onLoginResult(false)
//            }
//        })
//
//}
//
//suspend fun kakakoLogout(context: Context) {
//    var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
//    val tokenManager = TokenManager(context)
//    var api: LoginService = retrofit.create(LoginService::class.java)
//    val authToken = tokenManager.getAccessToken()
//    if (authToken != null) {
//        try {
//            val response = withContext(Dispatchers.IO) {
//                api.deleteToken(authToken).execute()
//            }
//            when (response.code()) {
//                200 -> {
//                    Log.d("Logout success", "로그아웃 성공")
//                    tokenManager.clearTokens()
//                    Log.d("삭제체크", "${tokenManager.getAccessToken()}")
//                    withContext(Dispatchers.Main) {
//                        UserApiClient.instance.logout { error ->
//                            if (error != null) {
//                                Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show()
//                            } else {
//                                Log.d("소셜로그인 성공", "성공")
//                                Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//                500 -> Log.d("Logout fail", "로그아웃 실패(서버)")
//                else -> Log.d("logout_fail", "로그실패 ${response.body()}")
//            }
//        } catch (t: Throwable) {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
//
//
//
//
//
//
//
