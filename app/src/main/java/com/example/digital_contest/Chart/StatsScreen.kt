package com.example.digital_contest.Chart

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.digital_contest.API.CategoryData
import com.example.digital_contest.API.CategoryDetail
import com.example.digital_contest.API.CompanyData
import com.example.digital_contest.API.Data.StatisDataStore
import com.example.digital_contest.API.Static.callCategoryDetailStatis
import com.example.digital_contest.API.Static.callCategoryStatis
import com.example.digital_contest.API.Static.callPlatformDetailStatis
import com.example.digital_contest.API.Static.callPlatformStatis
import com.example.digital_contest.R
import com.example.digital_contest.ui.theme.Digital_ContestTheme


@Composable
fun StatsScreen() {
    val image= R.drawable.chevron_down_1  //아래버튼
    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("전체 통계", "내 판매 통계")
    val context=LocalContext.current
    val statisDataStore = remember { StatisDataStore(context) }

    val totalCompanyData by statisDataStore.totalData.collectAsState(initial = emptyList())
    val myCompanyData by statisDataStore.myData.collectAsState(initial = emptyList())
    val totalCategoryData by statisDataStore.totalCategoryData.collectAsState(initial = emptyList())
    val myCategoryData by statisDataStore.myCategoryData.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        callPlatformDetailStatis(context,"platform-whole")
        callCategoryDetailStatis(context,"category-whole")
        callPlatformDetailStatis(context,"platform-mine")
        callCategoryDetailStatis(context,"category-mine")
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val density = LocalDensity.current

        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding( start = screenWidth * 0.04f,
                top = screenHeight * 0.03f,
                end = screenWidth * 0.04f)) {
                Text(
                    text = "통계 \uD83D\uDCC8",
                    fontFamily = FontFamily(fontBold),
                    fontSize = 32.sp,
                    lineHeight = 48.sp,
                    color=Color(0xFF000000)
                )
                Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                Text(
                    text = "모든 수치를 한 눈에 확인해보세요!",
                    fontFamily = FontFamily(fontMedium),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
                contentColor = Color.Black,
                indicator = {tabPositions ->
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(3.dp)
                            .background(Color.Black)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, fontFamily = FontFamily(fontBold), fontSize = 16.sp, lineHeight = 24.sp
                            ,color = if (selectedTabIndex == index) Color(0xFF000000) else Color(0xFF999999))

                        },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 여기에 선택된 탭에 따른 내용을 표시할 수 있습니다.
            when (selectedTabIndex) {
                0 -> {
                    StatisticePage(context,screenWidth,screenHeight,totalCompanyData,
                        totalCategoryData)  //전체통계
                }
                1 -> {
                    myStatisticepage(context,screenWidth,screenHeight,myCompanyData,myCategoryData) //내통계
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticePage(context: Context, screenWidth:Dp, screenHeight:Dp,
                   companyData:List<CompanyData>,categoryData: List<CategoryData>){  //아래 스크롤이 되는 뷰.

    val backgroundColor = Color(0xFFF2F2F7)
    val padding =screenWidth * 0.04f
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomsheetcategory by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        callPlatformStatis(context,"platform-whole")
        //callPlatformDetailStatis(context,"platform-whole")
        callCategoryStatis(context, "category-whole")
        //callCategoryDetailStatis(context,"category-whole")
    }

    if (showBottomSheet) {

        ModalBottomSheet(

            sheetState = sheetState  ,
            scrimColor = Color.Black.copy(alpha=0.5f),
            onDismissRequest ={showBottomSheet = false},
            containerColor = Color.White
        ) {
            BottomSheetContent(
                ondismiss = {showBottomSheet = false}
                , screenWidth, screenHeight
            )
        }
    }
    if (showBottomsheetcategory) {
        ModalBottomSheet(
            sheetState = sheetState,
            scrimColor = Color.Black.copy(alpha = 0.5f),
            onDismissRequest = { showBottomsheetcategory = false },
            containerColor = Color.White
        ) {
            BottomSheetcategory(
                ondismiss = { showBottomsheetcategory = false}
            )

        }
    }
    LazyColumn(
        modifier= Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = padding)
    ) {
        item{
            Spacer(modifier=Modifier.height(padding))
        }

        item{
            StatisticBox(
                title="플랫폼 별 선호 말투",
                content= {
                    Column(verticalArrangement = Arrangement.spacedBy(screenHeight * 0.005f)){
                        val allPlatforms = listOf("중고나라", "당근", "번개장터")
                        allPlatforms.forEach { platform ->
                            val companyInfo = companyData.find { it.target == platform }
                            platformRow(
                                platform,
                                getColorForCompany(platform),
                                companyInfo?.introduceTextCategory ?: "데이터 없음",
                                screenWidth,
                                screenHeight
                            )
                        }

                    }

                },
                detailAction = {showBottomSheet=true },
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }
        item{
            Spacer(modifier=Modifier.height(padding))
        }

        item{

            StatisticBox(title = "카테고리별 선호 말투"
                , content = {
                    Column(verticalArrangement = Arrangement.spacedBy(screenHeight *0.005f)) {
                        val categories =listOf(
                            "디지털기기", "가구/인테리어", "여성패션/잡화", "남성패션/잡화", "생활가전",
                            "생활주방", "스포츠/레저", "취미/게임/음반", "뷰티/미용", "식물",
                            "가공식품", "건강기능식품", "반려동물용품", "티켓/교환권", "도서", "기타중고"
                        )
                        categories.forEach{category ->
                            val categoryInfo = categoryData.find{ it.target == category }
                            CategoryRow(
                                category,
                                categoryInfo?.introduceTextCategory?:"데이터 없음",
                                screenWidth,screenHeight
                            )
                        }
                    }
                },

                 detailAction = {
                     showBottomsheetcategory = true
                 },
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }
        item {
            Spacer(modifier=Modifier.height(padding))
        }
    }
}

@Composable
fun platformRow(platform:String,dotColor: Color,styleText:String,screenWidth: Dp, screenHeight: Dp){  //각각 하나씩 스크롤뷰마다 의 요소
    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Box(
                modifier= Modifier
                    .size(screenWidth * 0.02f)
                    .background(dotColor, CircleShape)
            )
            Spacer(modifier=Modifier.width(screenWidth * 0.02f))
            Text(text=platform, fontFamily =FontFamily(fontMedium), fontSize = 16.sp, lineHeight = 24.sp)
        }
        Text(text=styleText, fontFamily =FontFamily(fontBold), fontSize = 16.sp, lineHeight = 24.sp)
    }
}

@Composable
fun CategoryRow(category:String,styleText:String, screenWidth: Dp, screenHeight: Dp){ //아래 카테고리 쪽 하나하나 담기는곳.
    val fontMedium = Font(R.font.pretendard_medium)
    val fontBold = Font(R.font.pretendard_bold)
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text=category, fontFamily =FontFamily(fontMedium), fontSize = 16.sp, lineHeight = 24.sp)
        Text(text=styleText, fontFamily =FontFamily(fontBold), fontSize = 16.sp, lineHeight = 24.sp)
    }
}


@Composable
fun StatisticBox(
    title:String,content:@Composable () -> Unit,
    detailAction: () ->Unit,
    modifier:Modifier = Modifier,screenWidth: Dp,
    screenHeight: Dp,){
    Box(modifier = modifier
        .fillMaxWidth()
        .background(Color.White, RoundedCornerShape(screenWidth * 0.04f))
        .padding(screenWidth * 0.04f))
    {
        Column {
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = detailAction)
                ){
                    Text(
                        text="자세히보기",
                        color=Color(0xFF999999),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        modifier=Modifier)//.clickable{}
                    Spacer(modifier=Modifier.width(screenWidth * 0.01f))
                    Image(painter= painterResource(id = R.drawable.chevron_down_1),
                        contentDescription = "자세히보기",modifier.size(screenWidth * 0.04f)
                    )
                }
                //detailAction()
            }
            Spacer(modifier=Modifier.height(screenHeight * 0.01f))
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myStatisticepage(context:Context, screenWidth:Dp, screenHeight:Dp
, companyData:List<CompanyData>,categoryData: List<CategoryData>){  //내 통계 페이지 부분.
    val backgroundColor = Color(0xFFF2F2F7)
    val boxColor =Color(0xFFFFFFFF)
    val cornerRadius = 16.dp
    val padding =screenWidth * 0.04f
    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomsheetcategory by remember { mutableStateOf(false) }
    LaunchedEffect (Unit){
        callPlatformStatis(context,"platform-mine")
        //callPlatformDetailStatis(context,"platform-mine")
        callCategoryStatis(context, "category-mine")
        //callCategoryDetailStatis(context,"category-mine")
    }

    if(showBottomSheet){
        ModalBottomSheet(
            sheetState = sheetState  ,
            scrimColor = Color.Black.copy(alpha=0.5f),
            onDismissRequest ={showBottomSheet = false},
            containerColor = Color.White
        ) {
            myBottomSheetContent(
                ondismiss = {showBottomSheet = false},
                screenWidth, screenHeight
            )
        }
    }
    if (showBottomsheetcategory) {
        ModalBottomSheet(
            sheetState = sheetState,
            scrimColor = Color.Black.copy(alpha = 0.5f),
            onDismissRequest = { showBottomsheetcategory = false },
            containerColor = Color.White
        ) {
            myBottomSheetcategory(
                ondismiss = { showBottomsheetcategory = false}
            )
        }
    }
    LazyColumn(
        modifier= Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = padding)
    ) {
        item{
            Spacer(modifier=Modifier.height(padding))
        }

        item{
            StatisticBox(
                title="플랫폼 별 선호 말투",
                content= {
                    Column(verticalArrangement = Arrangement.spacedBy(screenHeight * 0.005f)){
                        val allPlatforms = listOf("중고나라", "당근", "번개장터")
                        allPlatforms.forEach { platform ->
                            val companyInfo = companyData.find { it.target == platform }
                            myplatformRow(
                                platform,
                                getColorForCompany(platform),
                                companyInfo?.introduceTextCategory ?: "데이터 없음",
                                screenWidth,
                                screenHeight
                            )
                        }

                    }

                },
                detailAction = {showBottomSheet=true },
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }
        item{
            Spacer(modifier=Modifier.height(padding))
        }

        item{
            StatisticBox(title = "카테고리별 선호 말투"
                , content = {
                    Column(verticalArrangement = Arrangement.spacedBy(screenHeight *0.005f)) {
                        val categories =listOf(
                            "디지털기기", "가구/인테리어", "여성패션/잡화", "남성패션/잡화", "생활가전",
                            "생활주방", "스포츠/레저", "취미/게임/음반", "뷰티/미용", "식물",
                            "가공식품", "건강기능식품", "반려동물용품", "티켓/교환권", "도서", "기타중고"
                        )
                        categories.forEach{category ->
                            val categoryInfo = categoryData.find{ it.target == category }
                            myCategoryRow(
                                category,
                                categoryInfo?.introduceTextCategory?:"데이터 없음",
                                screenWidth,screenHeight
                            )
                        }
                    }
                },

                detailAction = {
                    showBottomsheetcategory = true
                },
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }
        item {
            Spacer(modifier=Modifier.height(padding))
        }
    }

}



@Composable
fun BottomSheetContent(ondismiss:()->Unit, screenWidth:Dp, screenHeight:Dp){

    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)
    val fontsemibold = Font(R.font.pretendard_semibold)
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, start = 14.dp, end = 14.dp)
    ) {

        item {
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "플랫폼 별 선호말투",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(fontsemibold),
                    lineHeight = 25.sp
                )
                Spacer(modifier = Modifier.width(190.dp))
                Image(
                    painter = painterResource(R.drawable.close_button),
                    contentDescription = null,
                    modifier = Modifier.clickable { ondismiss() })
            }
            Spacer(modifier = Modifier.height(26.dp))
            Box {
                my_test_chart()
                PlatformLabel(
                    "중고나라",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .zIndex(1f)
                    ,screenWidth,screenHeight
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box {
                danggun()
                PlatformLabel(
                    "당근",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .zIndex(1f)
                    ,screenWidth,screenHeight
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            Box {
                thunder()
                PlatformLabel(
                    "번개장터",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .zIndex(1f)
                    ,screenWidth,screenHeight
                )

            }

        }

    }
    Spacer(modifier = Modifier.padding(20.dp))
}

fun getColorForCompany(company: String): Color {
    return when (company) {
        "중고나라" -> Color(0xFF14AE5C)
        "당근" -> Color(0xFFFFA629)
        "번개장터" -> Color(0xFFFF0000)
        else -> Color.Gray
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ButtonList(
    statisDataStore: StatisDataStore,
    totalCategoryData: List<CategoryData>
) {
    val context = LocalContext.current
    val totalCategoryDetail by statisDataStore.categoryDetailData.collectAsState(initial = CategoryDetail(emptyList()))
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .background(Color.White)
    ) {
        val maxWidth = maxWidth
        val fontMedium = Font(R.font.pretendard_medium)

        Column {
            FlowRow {
                val buttonTexts = listOf(
                    "디지털기기", "가구/인테리어", "가공식품", "티켓/교환권", "여성패션/잡화",
                    "뷰티/미용", "남성패션/잡화", "생활가전", "생활주방", "취미/게임/음반",
                    "건강기능식품", "식물", "스포츠/레저", "도서", "반려동물용품", "기타중고"
                )

                buttonTexts.forEach { buttonText ->
                    val isSelected = buttonText == selectedCategory
                    Button(
                        onClick = { selectedCategory = if (isSelected) null else buttonText },
                        shape = RoundedCornerShape(99.dp),
                        modifier = Modifier
                            .padding(horizontal = maxWidth * 0.01f)
                            .wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isSelected) Color(0xFF14AE5C) else Color.White,
                        ),
                    ) {
                        Text(
                            text = buttonText,
                            fontFamily = FontFamily(fontMedium),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            if (selectedCategory != null) {
                val categoryDetail = totalCategoryDetail
                CategoryChart(selectedCategory!!, categoryDetail)
            }
        }
    }
}



@Composable
fun BottomSheetcategory(ondismiss:()->Unit) {
    val context = LocalContext.current
    val statisDataStore = remember { StatisDataStore(context) }
    val totalCategoryData by statisDataStore.totalCategoryData.collectAsState(initial = emptyList())
    val totalCategoryDetail by statisDataStore.categoryDetailData.collectAsState(initial = null)
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)
    val fontsemibold = Font(R.font.pretendard_semibold)

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(top = 16.dp, start = 14.dp, end = 14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "카테고리별 선호 말투",
                fontSize = 20.sp,
                fontFamily = FontFamily(fontsemibold),
                lineHeight = 25.sp
            )
            Spacer(modifier = Modifier.width(160.dp))
            Image(painter = painterResource(R.drawable.close_button),
                contentDescription = "categoryclose",
                modifier = Modifier.clickable { ondismiss() }
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        ButtonList(statisDataStore, totalCategoryData)

        Spacer(modifier=Modifier.height(15.dp))

        Spacer(modifier = Modifier.padding(12.dp))
        Text(
            text = "선호하는 말투를 사용하면 판매 확률이 올라가요!",
            fontSize = 14.sp,
            fontFamily = FontFamily(fontMedium),
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.padding(15.dp))
    }
}
@Composable
fun myplatformRow(platform:String,dotColor: Color,styleText:String,screenWidth: Dp, screenHeight: Dp){  //각각 하나씩 스크롤뷰마다 의 요소
    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Box(
                modifier= Modifier
                    .size(screenWidth * 0.02f)
                    .background(dotColor, CircleShape)
            )
            Spacer(modifier=Modifier.width(screenWidth * 0.02f))
            Text(text=platform, fontFamily =FontFamily(fontMedium), fontSize = 16.sp, lineHeight = 24.sp)
        }
        Text(text=styleText, fontFamily =FontFamily(fontBold), fontSize = 16.sp, lineHeight = 24.sp)
    }
}

@Composable
fun myCategoryRow(category:String,styleText:String, screenWidth: Dp, screenHeight: Dp){ //아래 카테고리 쪽 하나하나 담기는곳.
    val fontMedium = Font(R.font.pretendard_medium)
    val fontBold = Font(R.font.pretendard_bold)
    val fontsemibold = Font(R.font.pretendard_semibold)
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text=category, fontFamily =FontFamily(fontMedium), fontSize = 16.sp, lineHeight = 24.sp)
        Text(text=styleText, fontFamily =FontFamily(fontBold), fontSize = 16.sp, lineHeight = 24.sp)
    }
}

@Composable
fun myBottomSheetContent(ondismiss: () -> Unit, screenWidth:Dp, screenHeight:Dp,) {
    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)
    val fontsemibold = Font(R.font.pretendard_semibold)
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 14.dp, end = 14.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "플랫폼 별 선호말투",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(fontsemibold),
                    lineHeight = 25.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.close_button),
                    contentDescription = null,
                    modifier = Modifier.clickable { ondismiss() }
                )
            }
            Spacer(modifier = Modifier.height(26.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                my_test_chart()
                PlatformLabel(
                    "중고나라",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .zIndex(1f)
                    , screenWidth, screenHeight
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                my_danggun()
                PlatformLabel(
                    "당근",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .zIndex(1f)
                    , screenWidth, screenHeight
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                my_thunder()
                PlatformLabel(
                    "번개장터",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .zIndex(1f)
                    , screenWidth, screenHeight
                )
            }
        }
    }
    Spacer(modifier = Modifier.padding(20.dp))
}
@Composable
fun PlatformLabel(platformName: String, modifier: Modifier = Modifier, screenWidth:Dp, screenHeight:Dp) {
    val fontsemibold = Font(R.font.pretendard_semibold)
    val borderColor = when(platformName){
        "중고나라"->Color(0xFF14AE5C)
        "당근" ->Color(0xFFFF8329)
        "번개장터" -> Color(0xFFFF0000)
        else -> Color.Gray
    }
    Box(
        modifier = modifier
            .size(width = 82.dp, height = 30.dp)
            .background(Color.White, shape = RoundedCornerShape(4.dp))
            .border(1.dp,borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = platformName,
            fontSize = 18.sp,
            fontFamily = FontFamily(fontsemibold),
            textAlign = TextAlign.Center,
            color = borderColor,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun myBottomSheetcategory(ondismiss:()->Unit)
    {
        val context = LocalContext.current
        val statisDataStore = remember { StatisDataStore(context) }
        val totalCategoryData by statisDataStore.totalCategoryData.collectAsState(initial = emptyList())
        var selectedCategory by remember { mutableStateOf<String?>(null) }

        val fontBold = Font(R.font.pretendard_bold)
        val fontMedium = Font(R.font.pretendard_medium)
        val fontsemibold = Font(R.font.pretendard_semibold)
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 16.dp, start = 14.dp, end = 14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "카테고리별 선호 말투",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(fontsemibold),
                    lineHeight = 25.sp
                )
                Spacer(modifier = Modifier.width(160.dp))
                Image(painter = painterResource(R.drawable.close_button),
                    contentDescription = "categoryclose",
                    modifier = Modifier.clickable {ondismiss()}
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            myButtonList(statisDataStore)

            Spacer(modifier=Modifier.height(15.dp))

            Spacer(modifier = Modifier.padding(12.dp))
            Text(
                text = "선호하는 말투를 사용하면 판매 확률이 올라가요!",
                fontSize = 14.sp,
                fontFamily = FontFamily(fontMedium),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.padding(15.dp))
        }
    }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun myButtonList(statisDataStore: StatisDataStore) {
    val context = LocalContext.current
    val totalCategoryData by statisDataStore.mycategoryDetailData.collectAsState(initial = CategoryDetail(emptyList()))
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .background(Color.White)
    ) {
        val maxWidth = maxWidth
        val fontMedium = Font(R.font.pretendard_medium)

        Column {
            FlowRow {
                val buttonTexts = listOf(
                    "디지털기기", "가구/인테리어", "가공식품", "티켓/교환권", "여성패션/잡화",
                    "뷰티/미용", "남성패션/잡화", "생활가전", "생활주방", "취미/게임/음반",
                    "건강기능식품", "식물", "스포츠/레저", "도서", "반려동물용품", "기타중고"
                )

                buttonTexts.forEach { buttonText ->
                    val isSelected = buttonText == selectedCategory
                    Button(
                        onClick = { selectedCategory = if (isSelected) null else buttonText },
                        shape = RoundedCornerShape(99.dp),
                        modifier = Modifier
                            .padding(horizontal = maxWidth * 0.01f)
                            .wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isSelected) Color(0xFF14AE5C) else Color.White,
                        ),
                    ) {
                        Text(
                            text = buttonText,
                            fontFamily = FontFamily(fontMedium),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            if (selectedCategory != null) {
                my_CategoryChart(selectedCategory!!, totalCategoryData)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenpreview(){
    Digital_ContestTheme {
        StatsScreen()
    }
}