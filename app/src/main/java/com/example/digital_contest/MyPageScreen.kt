package com.example.digital_contest

import android.annotation.SuppressLint
import android.content.Context

import android.util.Log
import androidx.compose.animation.animateColorAsState

import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Divider

import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.runtime.setValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.digital_contest.API.LeverExperience
import com.example.digital_contest.API.Manager.MyPageManager
import com.example.digital_contest.Viewmodel.MyPageViewModel
import com.example.digital_contest.API.Manager.TokenManager
import com.example.digital_contest.API.Manager.UserManager
import com.example.digital_contest.API.UserNickName
import com.example.digital_contest.ui.theme.Digital_ContestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MyPageScreen(navHostController: NavHostController,modifier: Modifier = Modifier,myPageViewModel: MyPageViewModel) {
    val context = LocalContext.current

    var productCount by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        UserNickName(context)
    }

    //Log.i("상품 불러오기","$products")
    Log.i("상품 수","$productCount")



    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val fontMedium = FontFamily(Font(R.font.pretendard_medium))
    val fontSemiBold = FontFamily(Font(R.font.pretendard_semibold))


    val userManager = remember { UserManager(context) }
    val userData = userManager.nickname.collectAsState(initial = "사용자")

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val density = LocalDensity.current


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenHeight * 0.03f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth * 0.04f),
                contentAlignment = Alignment.TopEnd
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${userData.value}",
                                fontFamily = fontBold,
                                fontSize = with(density) { (screenWidth * 0.08f).toSp() },
                                lineHeight = with(density) { (screenWidth * 0.12f).toSp() }
                            )
                            Text(
                                text = " 님!",
                                fontFamily = fontBold,
                                fontSize = with(density) { (screenWidth * 0.06f).toSp() },
                                lineHeight = with(density) { (screenWidth * 0.09f).toSp() }
                            )
                        }

                        Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                        Text(
                            text = "오늘도 즐거운 거래 하시길 바래요!",
                            fontFamily = fontMedium,
                            fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                            lineHeight = with(density) { (screenWidth * 0.06f).toSp() }
                        )
                        Spacer(modifier = Modifier.height(screenHeight * 0.03f))
                        Text(
                            text = "나의 판매 내역",
                            fontFamily = fontBold,
                            fontSize = with(density) { (screenWidth * 0.05f).toSp() },
                            lineHeight = with(density) { (screenWidth * 0.075f).toSp() }
                        )
                    }

                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                kakakoLogout(context)
                                val tokenManager = TokenManager(context)
                                val accessToken = tokenManager.accessToken.first()
                                if (accessToken.isNullOrEmpty()) {
                                    navHostController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                        .size(width = screenWidth * 0.165f, height = screenWidth * 0.065f)
                            .offset(y = -screenHeight * 0.06f),//.offset(y=-screenHeight*0.04f)
                        colors = ButtonDefaults.buttonColors(Color(230, 230, 230)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "로그아웃",
                            fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                            fontFamily = fontMedium,
                            color = Color.Black,
                            lineHeight = with(density) { (screenWidth * 0.045f).toSp() }
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            MyPageMiddle(myPageViewModel)
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyPageMiddle(viewModel: MyPageViewModel =viewModel()) {
    val context= LocalContext.current
    val myPageManager=remember{ MyPageManager(context) }

    val tabss=listOf("판매중" to null,"판매완료" to true,"판매실패" to false)
    val tabs = listOf("판매중", "판매완료", "판매실패")


    var selectedTabIndex by remember { mutableStateOf(0) }

    val pagerState = rememberPagerState { tabss.size }
    val coroutineScope = rememberCoroutineScope()


    val fontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
    val fontRegular = FontFamily(Font(R.font.pretendard_regular))
    val fontemibold = FontFamily(Font(R.font.pretendard_semibold))
    var products by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    val onSaleProducts by viewModel.onSaleProducts.collectAsState()
    val completedProduts by viewModel.completedProducts.collectAsState()
    val failedProducts by viewModel.failedProducts.collectAsState()
    val onSaleproductCount by viewModel.onSaleproductCount.collectAsState()
    val completedproductCount by viewModel.completedproductCount.collectAsState()
    val failedproductCount by viewModel.failedproductCount.collectAsState()

    Log.d("판매목록","$onSaleProducts")
    Log.d("완료 데이터","$completedProduts")
    Log.d("실패목록","$failedProducts")
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val tabHeight = screenHeight * 0.08f
        val density = LocalDensity.current

        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(tabHeight),
                containerColor = Color.White,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(2.dp)
                            .background(Color.Black)
                    )
                },
                divider = {
                    Divider(
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = title,
                                fontFamily = fontemibold,
                                fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                                color = if (selectedTabIndex == index) Color.Black else Color.Gray
                            )
                        },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }

            when (selectedTabIndex) {
                0 ->{
                    SalePage(context,screenWidth, screenHeight,onSaleProducts,onSaleproductCount,viewModel)
                }
                1 ->{
                    SaleCompletePage(screenWidth, screenHeight,completedProduts,completedproductCount)
                }
                2 ->{
                    SaleFailPage(screenWidth, screenHeight,failedProducts,failedproductCount)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SalePage(context:Context,screenWidth: Dp, screenHeight: Dp,products:List<Map<String,Any>>,productCount:Int,viewModel: MyPageViewModel) {
    //val items = remember { mutableStateListOf<Int>().apply { addAll(0 until 20) } }
    val context= LocalContext.current
    LaunchedEffect(Unit) {
        tradeData(context)
        LeverExperience(context)
    }
    LazyColumn {
        items(
            count = products.size,
            key = { index -> products[index]["productId"] as Int}
        ) { index ->
            val dismissState = rememberDismissState()

            // 이 부분을 추가합니다
            LaunchedEffect(dismissState.currentValue) {
                when (dismissState.currentValue) {
                    DismissValue.DismissedToStart -> {
                        // 판매 실패로 변경 로직
                        val productId = products[index]["productId"] as Int
                        viewModel.updateProductStatus(productId,false)
                        viewModel.sortFailedProductsByIdAscending()
                        tradeData(context)
                        LeverExperience(context)
                        //items.removeAt(index)
                    }
                    DismissValue.DismissedToEnd -> {
                        // 판매 완료로 변경 로직
                        val productId = products[index]["productId"] as Int
                        viewModel.updateProductStatus(productId,true)
                        //items.removeAt(index)

                        viewModel.sortCompletedProductsByIdAscending()
                        delay(100)
                        tradeData(context)
                        LeverExperience(context)

                    }
                    DismissValue.Default -> { /* Do nothing */ }
                }
            }

            SwipeToDismiss(
                state = dismissState,
                background = {
                    SwipeBackground(dismissState)
                },
                dismissContent = {
                    Sale(products[index],screenWidth, screenHeight,)
                },
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return

    val color by animateColorAsState(
        when (direction) {
            DismissDirection.StartToEnd -> Color.Green
            DismissDirection.EndToStart -> Color.Red
        }
    )
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val text = when (direction) {
        DismissDirection.StartToEnd -> "판매 완료"
        DismissDirection.EndToStart -> "판매 실패"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Sale(product: Map<String, Any>, screenWidth: Dp, screenHeight: Dp) {
    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val fontMedium = FontFamily(Font(R.font.pretendard_medium))
    val fontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(393f / 160f)
            .background(color = Color.White)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 3.dp.toPx()
                )
            }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidth * 0.04f, vertical = screenHeight * 0.015f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = screenHeight * 0.02f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product["createdAt"] as? String ?: "",
                    fontFamily = fontMedium,
                    fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                    color = Color(0xFF999999)
                )
                SaleTag(screenWidth)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = product["imageUrl"] as? String,
                    contentDescription = "Product image",
                    modifier = Modifier.size(screenWidth * 0.24f)
                )
                Spacer(modifier = Modifier.width(screenWidth * 0.04f))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product["product"] as? String ?: "",
                        fontFamily = fontSemiBold,
                        fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                        color = Color(0xFF000000)
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Text(
                        text = product["productCategory"] as? String ?: "",
                        fontFamily = fontMedium,
                        fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                        color = Color(0xFF000000)
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = (product["company"] as? List<String>)?.joinToString(", ") ?: "",
                            fontFamily = fontMedium,
                            fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                            color = Color(0xFF999999)
                        )
                        Text(
                            text = (product["price"] as? Int)?.let { price ->
                                String.format("%,d원", price)
                            } ?: "",
                            fontFamily = fontBold,
                            fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                            color = Color(0xFF000000)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SaleCompletePage(screenWidth: Dp, screenHeight: Dp,products:List<Map<String,Any>>,productCount: Int) {
    val coroutineScope = rememberCoroutineScope()
    val context= LocalContext.current
    LaunchedEffect(Unit) {
        tradeData(context)
        LeverExperience(context)
    }
    LazyColumn {
        items(productCount) {index ->
            SaleComplete(screenWidth, screenHeight,products[index])
        }
    }
}


@Composable
fun SaleFailPage(screenWidth: Dp, screenHeight: Dp,products:List<Map<String,Any>>,productCount: Int) {
    val context= LocalContext.current
    LaunchedEffect(Unit) {
        tradeData(context)
        LeverExperience(context)
    }
    LazyColumn {
        items(productCount) {index ->
            SaleFail(products[index],screenWidth, screenHeight)
        }
    }
}

@Composable
fun SaleComplete(screenWidth: Dp, screenHeight: Dp, product: Map<String, Any>) {
    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val fontMedium = FontFamily(Font(R.font.pretendard_medium))
    val fontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(393f / 160f)
            .background(color = Color.White)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 3.dp.toPx()
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidth * 0.04f, vertical = screenHeight * 0.015f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = screenHeight * 0.01f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product["createdAt"] as? String ?: "",
                    fontFamily = fontMedium,
                    fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                    color = Color(0xFF999999)
                )
                SaleCompleteTag(screenWidth)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = product["imageUrl"] as? String,
                    contentDescription = "Product image",
                    modifier = Modifier.size(screenWidth * 0.24f)
                )
                Spacer(modifier = Modifier.width(screenWidth * 0.04f))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product["product"] as? String ?: "",
                        fontFamily = fontSemiBold,
                        fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                        color = Color(0xFF000000),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Text(
                        text = product["productCategory"] as? String ?: "",
                        fontFamily = fontMedium,
                        fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                        color = Color(0xFF000000),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = (product["company"] as? List<String>)?.joinToString(", ") ?: "",
                            fontFamily = fontMedium,
                            fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                            color = Color(0xFF999999),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = (product["price"] as? Int)?.let { price ->
                                String.format("%,d원", price)
                            } ?: "",
                            fontFamily = fontBold,
                            fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                            color = Color(0xFF000000)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun SaleFail(product: Map<String, Any>, screenWidth: Dp, screenHeight: Dp) {
    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val fontMedium = FontFamily(Font(R.font.pretendard_medium))
    val fontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(393f / 160f)
            .background(color = Color.White)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 3.dp.toPx()
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidth * 0.04f, vertical = screenHeight * 0.015f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = screenHeight * 0.01f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product["createdAt"] as? String ?: "",
                    fontFamily = fontMedium,
                    fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                    color = Color(0xFF999999)
                )
                SaleFailTag(screenWidth)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = product["imageUrl"] as? String,
                    contentDescription = "Product image",
                    modifier = Modifier.size(screenWidth * 0.24f)
                )
                Spacer(modifier = Modifier.width(screenWidth * 0.04f))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product["product"] as? String ?: "",
                        fontFamily = fontSemiBold,
                        fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                        color = Color(0xFF000000),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Text(
                        text = product["productCategory"] as? String ?: "",
                        fontFamily = fontMedium,
                        fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                        color = Color(0xFF000000),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = (product["company"] as? List<String>)?.joinToString(", ") ?: "",
                            fontFamily = fontMedium,
                            fontSize = with(density) { (screenWidth * 0.03f).toSp() },
                            color = Color(0xFF999999),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = (product["price"] as? Int)?.let { price ->
                                String.format("%,d원", price)
                            } ?: "",
                            fontFamily = fontBold,
                            fontSize = with(density) { (screenWidth * 0.04f).toSp() },
                            color = Color(0xFF000000)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun SaleCompleteTag(screenWidth: Dp) {
    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val tagWidth = screenWidth * 0.168f
    val tagHeight = screenWidth * 0.066f
    val cornerRadius = 6.dp

    Box(
        modifier = Modifier
            .size(width = tagWidth, height = tagHeight)
            .background(
                color = Color(0xFF14AE5C),
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "판매완료",
            color = Color(0xFFFFFFFF),
            fontSize = with(LocalDensity.current) { (screenWidth * 0.03f).toSp() },
            fontFamily = fontBold
        )
    }
}


@Composable
fun SaleTag(screenWidth: Dp) {
    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val tagWidth = screenWidth * 0.168f
    val tagHeight = screenWidth * 0.066f
    val cornerRadius = 6.dp
    val borderWidth = 1.dp

    Box(
        modifier = Modifier
            .size(width = tagWidth, height = tagHeight)
            .background(
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                width = borderWidth,
                color = Color(0xFF14AE5C),
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "판매중",
            color = Color(0xFF14AE5C),
            fontSize = with(LocalDensity.current) { (screenWidth * 0.03f).toSp() },
            fontFamily = fontBold
        )
    }
}

@Composable
fun SaleFailTag(screenWidth: Dp) {
    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val tagWidth = screenWidth * 0.168f
    val tagHeight = screenWidth * 0.066f
    val cornerRadius = 6.dp

    Box(
        modifier = Modifier
            .size(width = tagWidth, height = tagHeight)
            .background(
                color = Color(0xFFFFAF00),
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "판매실패",
            color = Color(0xFFFFFFFF),
            fontSize = with(LocalDensity.current) { (screenWidth * 0.03f).toSp() },
            fontFamily = fontBold
        )
    }
}



@Preview
@Composable
fun mypagepreview(){
    Digital_ContestTheme {
        //MyPageScreen(navHostController = rememberNavController())
    }
}

fun launch(){

}

