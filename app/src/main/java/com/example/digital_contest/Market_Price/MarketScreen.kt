package com.example.digital_contest.Market_Price

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.digital_contest.Main.BottomNavigationBar
import com.example.digital_contest.R
import java.util.Locale

@Composable
fun MarketScreen(
    viewModel: MarketPriceViewModel = viewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val state = viewModel.state.value
    val searchText = remember { mutableStateOf("") }

    val fontBold = FontFamily(Font(R.font.pretendard_bold))
    val fontMedium = FontFamily(Font(R.font.pretendard_medium))
    val fontSemiBold = FontFamily(Font(R.font.pretendard_semibold))

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(color = Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = (screenHeight.value * 0.03f).dp,
                    start = (screenWidth.value * 0.04f).dp,
                    end = (screenWidth.value * 0.04f).dp
                )
        ) {
            Text(
                text = "오늘의 시세 📊",
                fontFamily = fontBold,
                fontSize = 32.sp,
                lineHeight = 48.sp
            )

            Spacer(modifier = Modifier.height((screenHeight.value * 0.02f).dp))

            Text(
                text = "나의 물건의 시세가 궁금하신가요?",
                fontFamily = fontMedium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Text(
                text = "지금 바로 검색해보세요!",
                fontFamily = fontMedium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height((screenHeight.value * 0.03f).dp))

            SearchBox(
                value = searchText.value,
                onValueChange = { newValue ->
                    searchText.value = newValue
                },
                onSearch = {
                    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""
                    if (accessToken.isNotEmpty()) {
                        viewModel.searchMarketPrices(accessToken, searchText.value)
                    }
                },
                fontMedium = fontMedium
            )

            Spacer(modifier = Modifier.height((screenHeight.value * 0.03f).dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF14AE5C))
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error,
                            fontFamily = fontMedium,
                            fontSize = 16.sp,
                            color = Color.Red
                        )
                    }
                }
                state.products.isEmpty() && state.searchQuery.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "검색 결과가 없습니다",
                            fontFamily = fontMedium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
                state.products.isNotEmpty() -> {
                    LazyColumn {
                        items(state.products) { product ->
                            MarketResultItem(
                                product = product,
                                fontBold = fontBold,
                                fontSemiBold = fontSemiBold
                            )
                            Spacer(modifier = Modifier.height((screenHeight.value * 0.01f).dp))
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "검색어를 입력해주세요",
                            fontFamily = fontMedium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        BottomNavigationBar(
            navController = navController,
            currentRoute = "market"
        )
    }
}

@Composable
fun SearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    fontMedium: FontFamily
) {
    val isFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .onFocusChanged { isFocused.value = it.isFocused },
                textStyle = TextStyle(fontSize = 16.sp, fontFamily = fontMedium),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
            )

            Image(
                painter = painterResource(id = R.drawable.glasse_icon),
                contentDescription = "Search",
                modifier = Modifier.size(24.dp)
            )
        }

        if (value.isEmpty() && !isFocused.value) {
            Text(
                text = "제품 이름을 입력해주세요",
                color = Color.Gray,
                modifier = Modifier.padding(16.dp),
                fontFamily = fontMedium
            )
        }
    }
}

@Composable
fun MarketResultItem(
    product: MarketPriceData,
    fontBold: FontFamily,
    fontSemiBold: FontFamily
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = Color.White)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(94.dp)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = "Product image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.dummydata)
                )
                MarketTag(
                    text = getMarketName(product.company),
                    fontBold = fontBold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-8).dp, y = (-8).dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(94.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.title,
                    fontFamily = fontSemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF000000),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatPrice(product.price),
                    fontFamily = fontBold,
                    fontSize = 16.sp,
                    color = Color(0xFF000000)
                )
            }
        }
    }
}

@Composable
fun MarketTag(text: String, fontBold: FontFamily, modifier: Modifier = Modifier) {
    val backgroundColor = getMarketBackgroundColor(text)

    Box(
        modifier = modifier
            .size(width = 58.dp, height = 22.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFFFFFFFF),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            fontFamily = fontBold,
            textAlign = TextAlign.Center
        )
    }
}

fun getMarketName(company: String): String {
    return when {
        company.contains("중고나라", ignoreCase = true) -> "중고나라"
        company.contains("당근", ignoreCase = true) -> "당근"
        company.contains("번개장터", ignoreCase = true) -> "번개장터"
        else -> company.take(4)
    }
}

fun getMarketBackgroundColor(marketName: String): Color {
    return when {
        marketName.contains("중고나라", ignoreCase = true) -> Color(0xFF14AE5C)
        marketName.contains("당근", ignoreCase = true) -> Color(0xFFFF8329)
        marketName.contains("번개장터", ignoreCase = true) -> Color(0xFFFF0000)
        else -> Color(0xFF14AE5C)
    }
}

fun formatPrice(price: String): String {
    return try {
        val numericPrice = price.replace(Regex("[^0-9]"), "").toLongOrNull()
        if (numericPrice != null) {
            String.format(Locale.getDefault(), "%,d원", numericPrice)
        } else {
            price
        }
    } catch (e: Exception) {
        price
    }
}

@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    val navController = rememberNavController()
    MarketScreen(navController = navController)
}

