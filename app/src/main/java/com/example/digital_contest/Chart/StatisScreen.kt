package com.example.digital_contest.Chart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.digital_contest.API.Data.StatisDataStore
import com.example.digital_contest.Main.BottomNavigationBar
import com.example.digital_contest.R
import com.example.digital_contest.ui.theme.Digital_ContestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = remember { StatisDataStore(context) }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("전체 통계", "내 판매 통계")

    // 데이터 수집
    val totalCompanyData by dataStore.totalData.collectAsState(initial = emptyList())
    val myCompanyData by dataStore.myData.collectAsState(initial = emptyList())
    val totalCategoryData by dataStore.totalCategoryData.collectAsState(initial = emptyList())
    val myCategoryData by dataStore.myCategoryData.collectAsState(initial = emptyList())

    // 바텀시트
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showPlatformSheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }

    // 데이터 로드
    LaunchedEffect(Unit) {
        // 플랫폼 통계 (일반 + 상세)
        callPlatformStatis(context, "platform-whole")
        callPlatformDetailStatis(context, "platform-whole")
        callPlatformStatis(context, "platform-mine")
        callPlatformDetailStatis(context, "platform-mine")

        // 카테고리 통계 (일반 + 상세)
        callCategoryStatis(context, "category-whole")
        callCategoryDetailStatis(context, "category-whole")
        callCategoryStatis(context, "category-mine")
        callCategoryDetailStatis(context, "category-mine")
    }

    BoxWithConstraints(
        Modifier.fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
        ,contentAlignment = Alignment.BottomCenter
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        Column(Modifier.fillMaxSize().padding(bottom = 80.dp)) {
            // 헤더
            StatsHeader(screenWidth, screenHeight)
            Spacer(Modifier.height(screenHeight * 0.02f))

            // 탭
            StatsTabs(selectedTab, tabs) { selectedTab = it }

            // 컨텐츠
            StatisticPage(
                companyData = if (selectedTab == 0) totalCompanyData else myCompanyData,
                categoryData = if (selectedTab == 0) totalCategoryData else myCategoryData,
                onPlatformDetailClick = { showPlatformSheet = true },
                onCategoryDetailClick = { showCategorySheet = true },
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }

        // 하단 네비게이션 바
        BottomNavigationBar(
            navController = navController,
            currentRoute = "stats"
        )

        // 바텀시트
        if (showPlatformSheet) {
            PlatformBottomSheet(
                sheetState = sheetState,
                onDismiss = { showPlatformSheet = false },
                isMine = selectedTab == 1,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }

        if (showCategorySheet) {
            CategoryBottomSheet(
                sheetState = sheetState,
                onDismiss = { showCategorySheet = false },
                dataStore = dataStore,
                isMine = selectedTab == 1
            )
        }
    }
}

@Composable
private fun StatsHeader(screenWidth: androidx.compose.ui.unit.Dp, screenHeight: androidx.compose.ui.unit.Dp) {
    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)

    Column(Modifier.padding(start = screenWidth * 0.04f, top = screenHeight * 0.03f, end = screenWidth * 0.04f)) {
        Text("통계 📈", fontFamily = FontFamily(fontBold), fontSize = 32.sp, color = Color.Black)
        Spacer(Modifier.height(screenHeight * 0.005f))
        Text("모든 수치를 한 눈에 확인해보세요!", fontFamily = FontFamily(fontMedium), fontSize = 16.sp)
    }
}

@Composable
private fun StatsTabs(selectedTab: Int, tabs: List<String>, onTabSelected: (Int) -> Unit) {
    val fontBold = Font(R.font.pretendard_bold)

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        indicator = { tabPositions ->
            Box(Modifier.tabIndicatorOffset(tabPositions[selectedTab]).height(3.dp).background(Color.Black))
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        title,
                        fontFamily = FontFamily(fontBold),
                        fontSize = 16.sp,
                        color = if (selectedTab == index) Color.Black else Color(0xFF999999)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlatformBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    isMine: Boolean,
    screenWidth: androidx.compose.ui.unit.Dp,
    screenHeight: androidx.compose.ui.unit.Dp
) {
    val fontsemibold = Font(R.font.pretendard_semibold)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            // 헤더
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("플랫폼 별 선호말투", fontSize = 20.sp, fontFamily = FontFamily(fontsemibold))
                androidx.compose.foundation.Image(
                    painterResource(R.drawable.close_button),
                    "닫기",
                    Modifier.clickable { onDismiss() }
                )
            }

            Spacer(Modifier.height(26.dp))

            // 차트들
            LazyColumn {
                item {
                    ChartWithLabel("중고나라", Color(0xFF14AE5C)) {
                        if (isMine) my_jongonara() else jongonara() }
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ChartWithLabel("당근", Color(0xFFFFA629)) {
                        if (isMine) my_danggun() else danggun()
                    }
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ChartWithLabel("번개장터", Color(0xFFFF0000)) {
                        if (isMine) my_thunder() else thunder()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    dataStore: StatisDataStore,
    isMine: Boolean
) {
    val fontsemibold = Font(R.font.pretendard_semibold)
    val fontMedium = Font(R.font.pretendard_medium)
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth().background(Color.White)) {
            val screenWidth = maxWidth

            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                // 헤더
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("카테고리별 선호 말투", fontSize = 20.sp, fontFamily = FontFamily(fontsemibold))
                    androidx.compose.foundation.Image(
                        painterResource(R.drawable.close_button),
                        "닫기",
                        Modifier.clickable { onDismiss() }
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 카테고리 버튼
                CategoryButtons(selectedCategory, { selectedCategory = it }, screenWidth)

                Spacer(Modifier.height(15.dp))

                // 선택된 카테고리 차트
                selectedCategory?.let { category ->
                    if (isMine) {
                        val detail by dataStore.mycategoryDetailData.collectAsState(CategoryDetail(emptyList()))
                        my_CategoryChart(category, detail)
                    } else {
                        val detail by dataStore.categoryDetailData.collectAsState(CategoryDetail(emptyList()))
                        CategoryChart(category, detail)
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    "선호하는 말투를 사용하면 판매 확률이 올라가요!",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(fontMedium)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    val navController = androidx.navigation.compose.rememberNavController()
    Digital_ContestTheme { StatsScreen(navController) }
}



