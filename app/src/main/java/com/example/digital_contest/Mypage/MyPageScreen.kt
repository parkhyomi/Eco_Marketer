package com.example.digital_contest.Mypage

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.digital_contest.Login.AuthDataStore
import com.example.digital_contest.Login.TokenManager
import com.example.digital_contest.Main.BottomNavigationBar
import kotlinx.coroutines.launch
import kotlin.math.abs


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MyPageScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authDataStore = remember { AuthDataStore(context) }
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()
    val viewModel: MyPageViewModel = viewModel(
        factory = MyPageViewModelFactory(context, authDataStore)
    )

    // 데이터 로드
    LaunchedEffect(Unit) {
        viewModel.loadUserNickname()
        viewModel.loadAllProducts()
    }

    // 상태 구독
    val nickname by viewModel.userNickname.collectAsState()
    val onSaleProducts by viewModel.onSaleProducts.collectAsState()
    val completedProducts by viewModel.completedProducts.collectAsState()
    val failedProducts by viewModel.failedProducts.collectAsState()

    // 탭
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf(
        "판매중",
        "판매완료",
        "판매실패"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {

            UserInfoSection(
                nickname = nickname.ifEmpty { "사용자" },
                onLogout = {
                    scope.launch {
                        // TokenManager를 통해 로그아웃 (API 호출 + 로컬 데이터 삭제)
                        tokenManager.logout()

                        // 로그인 화면으로 이동
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "나의 판매 내역",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            // 탭
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                indicator = { tabPositions ->
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(2.dp)
                            .background(Color(0xFF000000))
                    )
                },
                divider = {
                    Divider(thickness = 1.dp, color = Color.LightGray)
                }
            ) {
                tabTitles.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = {
                            Text(
                                title,
                                fontSize = 14.sp,
                                color = if (selectedTab == idx) Color(0xFF000000) else Color(0xFF999999)
                            )
                        },
                        selectedContentColor = Color(0xFF000000),
                        unselectedContentColor = Color(0xFF999999)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 상품 목록 (스와이프 카드)
            when (selectedTab) {
                0 -> SwipeableProductList(
                    products = onSaleProducts,
                    onSwipeRight = { viewModel.updateProductStatus(it, true) },
                    onSwipeLeft = { viewModel.updateProductStatus(it, false) }
                )
                1 -> ProductList(products = completedProducts, showActions = false)
                2 -> ProductList(products = failedProducts, showActions = false)
            }
        }

        // 하단 네비게이션 바
        BottomNavigationBar(
            navController = navController,
            currentRoute = "mypage"
        )
    }
}

/**
 * 사용자 정보 섹션
 */
@Composable
private fun UserInfoSection(
    nickname: String,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "$nickname 님!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "오늘도 즐거운 거래 하시길 바래요!",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE6E6E6)
            ),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text("로그아웃", fontSize = 14.sp, color = Color(0xFF000000))
        }
    }
}

/**
 * 스와이프 가능한 상품 목록 (판매중)
 */
@Composable
private fun SwipeableProductList(
    products: List<ProductData>,
    onSwipeRight: (Int) -> Unit,
    onSwipeLeft: (Int) -> Unit
) {
    if (products.isEmpty()) {
        EmptyProductState()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F7)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products, key = { it.productId }) { product ->
                SwipeableProductCard(
                    product = product,
                    onSwipeRight = { onSwipeRight(product.productId) },
                    onSwipeLeft = { onSwipeLeft(product.productId) }
                )
            }
        }
    }
}

/**
 * 일반 상품 목록 (완료/실패)
 */
@Composable
private fun ProductList(
    products: List<ProductData>,
    showActions: Boolean = false
) {
    if (products.isEmpty()) {
        EmptyProductState()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F7)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                StaticProductCard(product = product)
            }
        }
    }
}

/**
 * 스와이프 가능한 상품 카드
 */
@Composable
private fun SwipeableProductCard(
    product: ProductData,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    var isDismissed by remember { mutableStateOf(false) }

    if (!isDismissed) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            Color(0xFFFF5722).copy(alpha = (abs(offsetX.value) / 300f).coerceIn(0f, 0.3f)),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (offsetX.value < -50f) {
                        Column(
                            modifier = Modifier.padding(start = 20.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("❌", fontSize = 32.sp)
                            Text(
                                "판매실패",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            Color(0xFF4CAF50).copy(alpha = (abs(offsetX.value) / 300f).coerceIn(0f, 0.3f)),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (offsetX.value > 50f) {
                        Column(
                            modifier = Modifier.padding(end = 20.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text("✅", fontSize = 32.sp)
                            Text(
                                "판매완료",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .graphicsLayer {
                        translationX = offsetX.value
                        rotationZ = offsetX.value / 40f
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                scope.launch {
                                    when {
                                        offsetX.value > 200f -> {
                                            offsetX.animateTo(1000f, animationSpec = tween(300))
                                            isDismissed = true
                                            onSwipeRight()
                                        }
                                        offsetX.value < -200f -> {
                                            offsetX.animateTo(-1000f, animationSpec = tween(300))
                                            isDismissed = true
                                            onSwipeLeft()
                                        }
                                        else -> {
                                            offsetX.animateTo(0f, animationSpec = tween(300))
                                        }
                                    }
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    offsetX.snapTo((offsetX.value + dragAmount).coerceIn(-400f, 400f))
                                }
                            }
                        )
                    },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                ProductCardContent(product = product)
            }
        }
    }
}

/**
 * 정적 상품 카드 (완료/실패)
 */
@Composable
private fun StaticProductCard(product: ProductData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        ProductCardContent(product = product)
    }
}

/**
 * 상품 카드 내용
 */
@Composable
private fun ProductCardContent(product: ProductData) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이미지
        Card(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.product,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 상품 정보
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = product.product,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1A1A),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = product.productCategory,
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 가격
            Text(
                text = "%,d원".format(java.util.Locale.KOREA, product.price),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

/**
 * 빈 상태
 */
@Composable
private fun EmptyProductState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📦",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "상품이 없습니다",
                color = Color(0xFF999999),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "새로운 상품을 등록해보세요!",
                color = Color(0xFFCCCCCC),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}