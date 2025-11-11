package com.example.digital_contest

//import android.content.Context
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
////import com.example.digital_contest.API.Manager.CountManager
////import com.example.digital_contest.API.Manager.LevelManager
////import com.example.digital_contest.API.Mypage.LeverExperience
////import com.example.digital_contest.API.Login.LoginService
//import com.example.digital_contest.Viewmodel.MyPageViewModel
////import com.example.digital_contest.API.RetrofitHelper
////import com.example.digital_contest.API.Manager.TokenManager
////import com.example.digital_contest.API.Main.UtilModel
////import com.example.digital_contest.API.WriteService
////import com.example.digital_contest.API.tradeDataResponse
//import com.example.digital_contest.Chart.StatsScreen
//import com.example.digital_contest.Write.WriteView
//import com.example.digital_contest.ui.theme.Digital_ContestTheme
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Response
//import retrofit2.Retrofit

//@Composable
//fun MainContent(navHostController: NavHostController,myPageViewModel: MyPageViewModel) {
//    val context = LocalContext.current
//    val navController = rememberNavController()
//
//    // 글꼴 변수 설정
//    val fontBold = Font(R.font.pretendard_bold)
//    val fontlight = Font(R.font.pretendard_light)
//    val fontMedium = Font(R.font.pretendard_medium)
//    val fontRegular = Font(R.font.pretendard_regular)
//    val fontSemiBold = Font(R.font.pretendard_semibold)
//
////    val LevelManager = remember { LevelManager(context) }
////    val countManager = remember { CountManager(context) }
//    val showBottomBar = remember { mutableStateOf(true) }
////    val countData = countManager.count.collectAsState(initial = 0)
////    val myexperience = LevelManager.myExperience.collectAsState(initial = 0)
////    val levelexperience = LevelManager.levelExperience.collectAsState(initial = 0)
////    val mylevel = LevelManager.myLevel.collectAsState(initial = 0)
////    val progress = myexperience.value.toFloat() / levelexperience.value.toFloat()
////    Log.i("TAG", "$progress")
//
//    var lastClickTime = 0L
//
//    val imageResource = when {
//        mylevel.value == 1 -> R.drawable.mainview_1_lv
//        mylevel.value == 2 -> R.drawable.lv_2_tree
//        mylevel.value == 3 -> R.drawable.lv_3_tree
//        mylevel.value == 4 -> R.drawable.lv_4_tree
//        else -> R.drawable.lv_5_tree
//    }
//
//    LaunchedEffect(Unit) {
//        tradeData(context)
////        LeverExperience(context)
////        UtilModel(context)
//        myPageViewModel.loadAndSortAllProducts()
//        //myPageViewModel.sortProductsByIdAscending()
//    }
//
//    LaunchedEffect(navController) {
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            showBottomBar.value = destination.route != "write"
//        }
//    }
//
//    Scaffold(
//        bottomBar = {  if (showBottomBar.value) {
//            BottomNavigationBar(navController)
//        } }
//    ) { innerPadding ->
//        BoxWithConstraints(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(242, 242, 247))
//        ) {
//            val maxWidth = maxWidth
//            val maxHeight = maxHeight
//            NavHost(
//                navController = navController,
//                startDestination = "main",
//                modifier = Modifier.padding(innerPadding)
//            ) {
//                composable("main") {
//                    Column(
//                        modifier = Modifier
//                            .widthIn(maxWidth)
//                            .height(maxHeight)
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        // 첫 번째 섹션
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(1f)
//                                .height(maxHeight * 0.45f)
//                                .background(Color.White, RoundedCornerShape(16.dp))
//                                .padding(16.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Column(
//                                verticalArrangement = Arrangement.Center,
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    Text(
//                                        text = "물건 거래 횟수",
//                                        fontFamily = FontFamily(fontBold),
//                                        fontSize = 18.sp
//                                    )
//                                    Text(
//                                        text = " ${countData.value} 번 ",
//                                        fontFamily = FontFamily(fontBold),
//                                        color = Color(20, 174, 92),
//                                        fontSize = 24.sp
//                                    )
//                                    Text(
//                                        text = "만큼",
//                                        fontFamily = FontFamily(fontBold),
//                                        fontSize = 18.sp
//                                    )
//                                }
//                                Text(
//                                    text = "탄소 절감에 기여했어요!",
//                                    fontFamily = FontFamily(fontBold),
//                                    fontSize = 18.sp
//                                )
//                                Box(modifier = Modifier.size(250.dp)) {
//                                    Image(
//                                        painter = painterResource(imageResource),
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .fillMaxSize()
//                                            .padding(vertical = 16.dp)
//                                    )
//                                }
//                                Text(
//                                    text = "더 참여하여 나무를 키워주세요!",
//                                    fontFamily = FontFamily(fontMedium),
//                                    fontSize = 14.sp
//                                )
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(30.dp))
//                        // 두 번째 섹션
//                        Box(
//                            modifier = Modifier
//                                .height(maxHeight * 0.26f)
//                                .background(Color.White, RoundedCornerShape(16.dp))
//                                .padding(16.dp),
//                        ) {
//                            Column(modifier = Modifier.height(maxHeight * 0.25f)) {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Text(
//                                        text = "나의 환경 지킴 레벨🌱",
//                                        fontFamily = FontFamily(fontBold),
//                                        fontSize = 18.sp
//                                    )
//                                    Row(verticalAlignment = Alignment.CenterVertically) {
//                                        Text(
//                                            text = "${myexperience.value}",
//                                            color = Color(20, 174, 92),
//                                            fontFamily = FontFamily(fontBold),
//                                            fontSize = 32.sp
//                                        )
//                                        Text(
//                                            text = " / ${levelexperience.value}P",
//                                            fontFamily = FontFamily(fontMedium),
//                                            fontSize = 20.sp
//                                        )
//                                    }
//                                }
//                                Text(
//                                    text = "LEVEL ${mylevel.value}",
//                                    fontFamily = FontFamily(fontMedium),
//                                    fontSize = 12.sp
//                                )
//                                Spacer(modifier = Modifier.padding(2.dp))
//                                Column(
//                                    verticalArrangement = Arrangement.Center,
//                                    horizontalAlignment = Alignment.CenterHorizontally
//                                ) {
//                                    val progressValue = (myexperience.value.toFloat() / levelexperience.value.toFloat())
//                                        .takeIf { !it.isNaN() && it in 0f..1f } ?: 0f
//                                    Log.i("hihi", "$progressValue")
//                                    Box(
//                                        modifier = Modifier
//                                            .height(8.dp)
//                                            .fillMaxWidth()
//                                            .background(Color(0xFFA9A9A9), RoundedCornerShape(99.dp))
//                                            .clip(RoundedCornerShape(99.dp))
//                                    ) {
//                                        Box(
//                                            modifier = Modifier
//                                                .fillMaxWidth(progressValue)
//                                                .height(8.dp)
//                                                .background(Color(0xFF14AE5C), RoundedCornerShape(99.dp))
//                                        )
//                                    }
//
//                                    Spacer(modifier = Modifier.padding(8.dp))
//                                    Text(
//                                        text = "에코 포인트로 등급을 올려 환경 지킴 레벨을 올려요!",
//                                        fontFamily = FontFamily(fontMedium),
//                                        fontSize = 12.sp,
//                                        textAlign = TextAlign.Center,
//                                    )
//                                    Spacer(modifier = Modifier.padding(8.dp))
//                                    Button(
//                                        onClick = {
//                                            navController.navigate("write") {
//                                                popUpTo("main"){inclusive=true}
//                                            }
//                                        },
//                                        shape = RoundedCornerShape(8.dp),
//                                        colors = ButtonDefaults.buttonColors(Color(20, 174, 92)),
//                                        modifier = Modifier.fillMaxWidth()
//                                            .height(maxHeight*0.06f)
//                                    ) {
//                                        Text(
//                                            text = "AI와 함께 새로운 글 작성하기",
//                                            fontFamily = FontFamily(fontSemiBold),
//                                            fontSize = 17.sp,
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                composable("write") {
//                    WriteView(navHostController,myPageViewModel)
//                }
//                composable("stats") {
//                    StatsScreen()
//                }
//                composable("market") {
//                    MarketScreen()
//                }
//                composable("mypage") {
//
//                    MyPageScreen(navHostController,myPageViewModel=myPageViewModel)
//
//
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun BottomNavigationBar(navController: NavHostController) {
//    val items = listOf(
//        BottomNavItem("main", "메인", R.drawable.symbol_main),
//        BottomNavItem("stats", "통계", R.drawable.state_symbol),
//        BottomNavItem("market", "시세", R.drawable.price_symbol),
//        BottomNavItem("mypage", "마이페이지", R.drawable.mypage_symbol)
//    )
//
//    val navBackStackEntry = navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry.value?.destination?.route
//
//    val selectedColor = Color(0xFF14AE5C)
//    val unselectedColor = Color(0xFF999999)
//    BoxWithConstraints(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        val screenHeight = maxHeight
//        val navBarHeight = (screenHeight * 0.2f).coerceAtLeast(60.dp).coerceAtMost(88.dp)
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(navBarHeight)
//                .align(Alignment.BottomCenter)
//        ) {
//            NavigationBar(
//                containerColor = Color(0xFFFFFFFF),
//                modifier = Modifier
//                    .fillMaxSize()
//                    .border(
//                        BorderStroke(1.dp, Color.LightGray),
//                        shape = RectangleShape
//                    )
//            ) {
//                items.forEach { item ->
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .clickable {
//                                navController.navigate(item.route) {
//                                    popUpTo(navController.graph.startDestinationId)
//                                    launchSingleTop = true
//                                }
//                            }
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center,
//                            modifier = Modifier.fillMaxSize()
//                        ) {
//                            Icon(
//                                painter = painterResource(id = item.iconResourceId),
//                                contentDescription = item.title,
//                                modifier = Modifier.size((navBarHeight * 0.3f).coerceAtMost(24.dp)),
//                                tint = if (currentRoute == item.route) selectedColor else unselectedColor
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text(
//                                text = item.title,
//                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
//                                fontSize = with(LocalDensity.current) {
//                                    minOf(navBarHeight * 0.12f, 10.dp).toSp()
//                                },
//                                color = if (currentRoute == item.route) selectedColor else unselectedColor,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun previewmain(){
//    Digital_ContestTheme {
//        //MainContent(navHostController = rememberNavController() )
//    }
//}
//
//// 네비게이션 아이템을 위한 데이터 클래스 정의
//data class BottomNavItem(val route: String, val title: String, val iconResourceId: Int)
//
//
//fun tradeData(context: Context) {
//    var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
//    val tokenManager = TokenManager(context)
//    val countManager = CountManager(context)
//    var api: LoginService = retrofit.create(LoginService::class.java)
//
//
//    CoroutineScope(Dispatchers.IO).launch {
//        val authToken = tokenManager.getAccessToken()
//        if (authToken != null) {
//            api.getTradeCountData(authToken)
//                .enqueue(object : retrofit2.Callback<tradeDataResponse> {
//                    override fun onResponse(
//                        call: Call<tradeDataResponse>,
//                        response: Response<tradeDataResponse>
//                    ) {
//                        when (response.code()) {
//                            200 -> {
//                                Log.d("success", "거래 횟수 조회 성공")
//                                val responseBody = response.body()
//                                if (responseBody != null) {
//                                    CoroutineScope(Dispatchers.IO).launch {
//                                        val contData = responseBody.data.count
//                                        countManager.saveCount(contData)
//                                        val savedTradeData = countManager.getCount()
//                                        Log.d("거래 횟수 저장 확인", "$savedTradeData")
//                                    }
//                                }
//                            }
//                            500 -> Log.d("fail", "거래 횟수 조회 실패(서버)")
//                            else -> Log.d("tradeData_fali", "로그실패 ${response.body()}")
//                        }
//                    }
//                    override fun onFailure(call: Call<tradeDataResponse>, t: Throwable) {
//                        Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
//                    }
//                })
//        }
//    }
//}

