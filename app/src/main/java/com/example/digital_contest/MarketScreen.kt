//package com.example.digital_contest
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.Icon
//
//import androidx.compose.ui.Alignment
//
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawWithContent
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Density
//import androidx.compose.ui.unit.Dp
//import coil.compose.AsyncImage
//
//@Composable
//fun MarketScreen(modifier: Modifier = Modifier) {
//    val fontBold = FontFamily(Font(R.font.pretendard_bold))
//    val fontMedium = FontFamily(Font(R.font.pretendard_medium))
//    val fontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
//
//    var searchText = remember { mutableStateOf("") }
//    var searchResults = remember { mutableStateOf(generateInitialDummyData()) }
//
//    BoxWithConstraints(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.White)
//    ) {
//        val screenWidth = maxWidth
//        val screenHeight = maxHeight
//        val density = LocalDensity.current
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = (screenHeight.value * 0.03f).dp, start = (screenWidth.value * 0.04f).dp, end = (screenWidth.value * 0.04f).dp)
//        ) {
//            Text(
//                text = "오늘의 시세 \uD83D\uDCCA",
//                fontFamily = fontBold,
//                fontSize = 32.sp,
//                lineHeight = 48.sp
//            )
//
//            Spacer(modifier = Modifier.height((screenHeight.value * 0.02f).dp))
//
//            Text(
//                text = "나의 물건의 시세가 궁금하신가요?",
//                fontFamily = fontMedium,
//                fontSize = 16.sp,
//                lineHeight = 24.sp
//            )
//
//            Text(
//                text = "지금 바로 검색해보세요!",
//                fontFamily = fontMedium,
//                fontSize = 16.sp,
//                lineHeight = 24.sp
//            )
//
//            Spacer(modifier = Modifier.height((screenHeight.value * 0.03f).dp))
//
//            SearchBox(
//                value = searchText.value,
//                onValueChange = { newValue ->
//                    searchText.value = newValue
//                    searchResults.value = if (newValue.isNotEmpty()) {
//                        generateSearchResults(newValue)
//                    } else {
//                        generateInitialDummyData()
//                    }
//                },
//                screenWidth = screenWidth,
//                fontMedium = Font(R.font.pretendard_medium)
//            )
//
//            Spacer(modifier = Modifier.height((screenHeight.value * 0.03f).dp))
//
//            LazyColumn {
//                items(searchResults.value.size) { index ->
//                    MarketResultItem(searchResults.value[index], screenWidth, screenHeight, fontBold, fontMedium, fontSemiBold, density)
//                    Spacer(modifier = Modifier.height((screenHeight.value * 0.01f).dp))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SearchBox(
//    value: String,
//    onValueChange: (String) -> Unit,
//    screenWidth: Dp,
//    fontMedium: Font
//) {
//    var isFocused = remember { mutableStateOf(false) }
//    val focusManager = LocalFocusManager.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(end = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            BasicTextField(
//                value = value,
//                onValueChange = onValueChange,
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(16.dp)
//                    .onFocusChanged { isFocused.value = it.isFocused },
//                textStyle = TextStyle(fontSize = 16.sp, fontFamily = FontFamily(fontMedium)),
//                singleLine = true,
//                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
//            )
//
//            Image(
//                painter = painterResource(id = R.drawable.glasse_icon),
//                contentDescription = "Search",
//                modifier = Modifier.size(24.dp)
//            )
//        }
//
//        if (value.isEmpty() && !isFocused.value) {
//            Text(
//                text = "제품 이름을 입력해주세요",
//                color = Color.Gray,
//                modifier = Modifier.padding(16.dp),
//                fontFamily = FontFamily(fontMedium)
//            )
//        }
//    }
//}
//
//@Composable
//fun MarketResultItem(product: Map<String, Any>, screenWidth: Dp, screenHeight: Dp, fontBold: FontFamily, fontMedium: FontFamily, fontSemiBold: FontFamily, density: Density) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(393f / 160f)
//            .background(color = Color.White)
//            .drawWithContent {
//                drawContent()
//                drawLine(
//                    color = Color(0xFFE0E0E0),
//                    start = Offset(0f, size.height),
//                    end = Offset(size.width, size.height),
//                    strokeWidth = 1.dp.toPx()
//                )
//            }
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(94.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = product["imageRes"] as? Int ?: R.drawable.dummydata),
//                    contentDescription = "Product image",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(4.dp)),
//                    contentScale = ContentScale.Crop
//                )
//                MarketTag(
//                    text = getMarketName(product["product"] as? String ?: ""),
//                    fontMedium = fontMedium,
//                    modifier = Modifier
//                        .align(Alignment.TopStart)
//                     .offset(x = -8.dp, y = -8.dp)
//                )
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(94.dp),
//                verticalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = getCleanProductName(product["product"] as? String ?: ""),
//                    fontFamily = fontSemiBold,
//                    fontSize = 16.sp,
//                    color = Color(0xFF000000),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Text(
//                    text = product["productCategory"] as? String ?: "",
//                    fontFamily = fontMedium,
//                    fontSize = 14.sp,
//                    color = Color(0xFF000000),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Text(
//                    text = when (val price = product["price"]) {
//                        is Int -> String.format("%,d원", price)
//                        is String -> try {
//                            String.format("%,d원", price.toInt())
//                        } catch (e: NumberFormatException) {
//                            price // 숫자로 변환할 수 없는 경우 원본 문자열 표시
//                        }
//                        else -> "가격 정보 없음"
//                    },
//                    fontFamily = fontBold,
//                    fontSize = 16.sp,
//                    color = Color(0xFF000000)
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun MarketTag(text: String, fontMedium: FontFamily, modifier: Modifier = Modifier) {
//    val backgroundColor = getMarketBackgroundColor(text)
//    val fontBold = FontFamily(Font(R.font.pretendard_bold))
//
//    Box(
//        modifier = modifier
//            .size(width = 58.dp, height = 22.dp)
//            .background(backgroundColor, RoundedCornerShape(4.dp)),  // 알파 값을 FF로 변경하여 완전 불투명하게 만듦
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            color = Color(0xFFFFFFFF),
//            fontSize = 12.sp,
//            lineHeight = 18.sp,
//            fontFamily = fontBold,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//fun generateInitialDummyData(): List<Map<String, Any>> {
//    return listOf(
//        mapOf(
//            "createdAt" to "2023-07-16",
//            "imageRes" to R.drawable.dummydata,  // 로컬 이미지 리소스를 사용
//            "product" to "중고나라 아이폰",
//            "productCategory" to "전자기기",
//            "price" to 500000
//        ),
//        mapOf(
//            "createdAt" to "2023-07-16",
//            "imageRes" to R.drawable.dummydata,  // 로컬 이미지 리소스를 사용
//            "product" to "당근마켓 아이폰",
//            "productCategory" to "전자기기",
//            "price" to 520000
//        ),
//        mapOf(
//            "createdAt" to "2023-07-16",
//            "imageRes" to R.drawable.dummydata,  // 로컬 이미지 리소스를 사용
//            "product" to "번개장터 아이폰",
//            "productCategory" to "전자기기",
//            "price" to 510000
//        ),
//        // 추가 더미 데이터...
//    )
//}
//
//fun generateSearchResults(searchTerm: String): List<Map<String, Any>> {
//    return listOf(
//        mapOf(
//            "createdAt" to "2023-07-16",
//            "imageRes" to R.drawable.dummydata,  // 로컬 이미지 리소스 사용
//            "product" to "중고나라 $searchTerm",
//            "productCategory" to "검색결과",
//            "price" to 100000
//        ),
//        mapOf(
//            "createdAt" to "2023-07-16",
//            "imageRes" to R.drawable.dummydata,  // 로컬 이미지 리소스 사용
//            "product" to "중고나라 $searchTerm",
//            "productCategory" to "검색결과",
//            "price" to 100000
//        ),
//        mapOf(
//            "createdAt" to "2023-07-16",
//            "imageRes" to R.drawable.dummydata,  // 로컬 이미지 리소스 사용
//            "product" to "중고나라 $searchTerm",
//            "productCategory" to "검색결과",
//            "price" to 100000
//        )
//    )
//}
//
//fun getMarketName(productName: String): String {
//    return when {
//        productName.contains("중고나라") -> "중고나라"
//        productName.contains("당근") -> "당근"
//        productName.contains("번개장터") -> "번개장터"
//        else -> ""
//    }
//}
//
//fun getMarketBackgroundColor(marketName: String): Color {
//    return when (marketName) {
//        "중고나라" -> Color(0xFF14AE5C)
//        "당근" -> Color(0xFFFF8329)
//        "번개장터" -> Color(0xFFFF0000)
//        else -> Color.LightGray
//    }
//}
//
//fun getCleanProductName(productName: String): String {
//    val marketplaces = listOf("중고나라", "당근마켓", "당근마켓", "번개장터")
//    var cleanName = productName
//    for (marketplace in marketplaces) {
//        cleanName = cleanName.replace(marketplace, "").trim()
//    }
//    return cleanName
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MarketScreenPreviewLight() {
//    MarketScreen()
//}
