package com.example.digital_contest.Chart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.digital_contest.R
import com.example.digital_contest.Chart.models.Platform
import com.example.digital_contest.Chart.models.CategoryConstants
import com.example.digital_contest.Chart.models.StatisData


@Composable
fun StatisticBox(
    title: String,
    content: @Composable () -> Unit,
    onDetailClick: () -> Unit,
    screenWidth: Dp,
    screenHeight: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(screenWidth * 0.04f))
            .padding(screenWidth * 0.04f)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onDetailClick)
                ) {
                    Text("자세히보기", color = Color(0xFF999999), fontSize = 14.sp)
                    Spacer(Modifier.width(screenWidth * 0.01f))
                    Image(
                        painterResource(R.drawable.chevron_down_1),
                        "자세히보기",
                        Modifier.size(screenWidth * 0.04f)
                    )
                }
            }
            Spacer(Modifier.height(screenHeight * 0.01f))
            content()
        }
    }
}

@Composable
fun DataRow(
    label: String,
    value: String,
    dotColor: Color? = null,
    screenWidth: Dp
) {
    val fontMedium = Font(R.font.pretendard_medium)
    val fontBold = Font(R.font.pretendard_bold)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            dotColor?.let {
                Box(Modifier.size(screenWidth * 0.02f).background(it, CircleShape))
                Spacer(Modifier.width(screenWidth * 0.02f))
            }
            Text(label, fontFamily = FontFamily(fontMedium), fontSize = 16.sp)
        }
        Text(value, fontFamily = FontFamily(fontBold), fontSize = 16.sp)
    }
}

@Composable
fun PlatformLabel(platformName: String, borderColor: Color, zIndex: Modifier) {
    val fontsemibold = Font(R.font.pretendard_semibold)
    Box(
        modifier = Modifier
            .size(82.dp, 30.dp)
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            platformName,
            fontSize = 18.sp,
            fontFamily = FontFamily(fontsemibold),
            textAlign = TextAlign.Center,
            color = borderColor,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ChartWithLabel(
    platformName: String,
    borderColor: Color,
    chart: @Composable () -> Unit
) {
    Box(Modifier.fillMaxWidth()) {
        chart()
        PlatformLabel(
            platformName,
            borderColor,
            Modifier.align(Alignment.TopStart).padding(8.dp).zIndex(1f)
        )
    }
}

@Composable
fun CategoryButtons(
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    screenWidth: Dp
) {
    val fontMedium = Font(R.font.pretendard_medium)

    Column {
        CategoryConstants.ALL_CATEGORIES.chunked(3).forEach { rowCategories ->
            Row(Modifier.fillMaxWidth()) {
                rowCategories.forEach { category ->
                    val isSelected = category == selectedCategory
                    Button(
                        onClick = { onCategorySelected(if (isSelected) null else category) },
                        shape = RoundedCornerShape(99.dp),
                        modifier = Modifier.padding(horizontal = screenWidth * 0.01f, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isSelected) Color(0xFF14AE5C) else Color.White
                        )
                    ) {
                        Text(
                            category,
                            fontFamily = FontFamily(fontMedium),
                            fontSize = 16.sp,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}

// 통계 페이지 공통 레이아웃
@Composable
fun StatisticPage(
    companyData: List<StatisData>,
    categoryData: List<StatisData>,
    onPlatformDetailClick: () -> Unit,
    onCategoryDetailClick: () -> Unit,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val padding = screenWidth * 0.04f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .padding(horizontal = padding)
    ) {
        item { Spacer(Modifier.height(padding)) }

        // 플랫폼 별 선호 말투
        item {
            StatisticBox(
                title = "플랫폼 별 선호 말투",
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(screenHeight * 0.005f)) {
                        Platform.values().forEach { platform ->
                            val data = companyData.find { it.target == platform.displayName }
                            DataRow(
                                label = platform.displayName,
                                value = data?.introduceTextCategory ?: "데이터 없음",
                                dotColor = platform.color,
                                screenWidth = screenWidth
                            )
                        }
                    }
                },
                onDetailClick = onPlatformDetailClick,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }

        item { Spacer(Modifier.height(padding)) }

        // 카테고리별 선호 말투
        item {
            StatisticBox(
                title = "카테고리별 선호 말투",
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(screenHeight * 0.005f)) {
                        CategoryConstants.ALL_CATEGORIES.forEach { category ->
                            val data = categoryData.find { it.target == category }
                            DataRow(
                                label = category,
                                value = data?.introduceTextCategory ?: "데이터 없음",
                                screenWidth = screenWidth
                            )
                        }
                    }
                },
                onDetailClick = onCategoryDetailClick,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }

        item { Spacer(Modifier.height(padding)) }
    }
}

