package com.example.digital_contest

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

data class CategoryItem(
    @DrawableRes val imageRes: Int,
    val text: String
)

@Composable
fun Category(
    onItemClick: (String) -> Unit,
    onCloseClick: () -> Unit,
    titleTextStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        lineHeight = 25.sp,
        color = Color(0xFF000000)
    ),
    itemTextStyle: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        lineHeight = 18.sp,
        color = Color(0xFF000000)
    )
) {
    val categoryItems = listOf(
        CategoryItem(R.drawable.icon1, "디지털 기기"),
        CategoryItem(R.drawable.ticket_icon, "티켓/교환권"),
        CategoryItem(R.drawable.icon3, "여성 패션/잡화"),
        CategoryItem(R.drawable.icon4, "남성 패션/잡화"),
        CategoryItem(R.drawable.icon5, "생활 가전"),
        CategoryItem(R.drawable.icon6, "생활 주방"),
        CategoryItem(R.drawable.icon7, "스포츠/레저"),
        CategoryItem(R.drawable.icon8, "취미/게임/음악"),
        CategoryItem(R.drawable.icon9, "뷰티/미용"),
        CategoryItem(R.drawable.icon10, "식물"),
        CategoryItem(R.drawable.icon11, "가공식품"),
        CategoryItem(R.drawable.icon12, "건강기능식품"),
        CategoryItem(R.drawable.icon13, "반려동물"),
        CategoryItem(R.drawable.icon14, "도서"),
        CategoryItem(R.drawable.icon15, "기타 중고")
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val density = LocalDensity.current

        val horizontalPadding = 22.5.dp
        val verticalSpacing = 12.dp
        val itemSpacing = 12.dp

        val availableWidth = screenWidth - (horizontalPadding * 2)
        val itemWidth = (availableWidth - (itemSpacing * 2)) / 3

        val scaleFactor = availableWidth.value / (108f * 3 + 12f * 2)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { (42.dp.toPx() * scaleFactor).toDp() })
                    .padding(top = with(density) { (14.dp.toPx() * scaleFactor).toDp() }),
                contentAlignment = Alignment.CenterStart
            ) {
                Text("카테고리", style = titleTextStyle.copy(
                    fontSize = with(density) { (20.sp.toPx() * scaleFactor).toSp() }
                ))
                Image(
                    painter = painterResource(id = R.drawable.close_button),
                    contentDescription = "Close",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(with(density) { (24.dp.toPx() * scaleFactor).toDp() })
                        .clickable { onCloseClick() },
                    colorFilter = ColorFilter.tint(Color(0xFF7F7F7F))
                )
            }

            Spacer(modifier = Modifier.height(verticalSpacing))

            repeat(5) { rowIndex ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing)
                ) {
                    repeat(3) { columnIndex ->
                        val itemIndex = rowIndex * 3 + columnIndex
                        if (itemIndex < categoryItems.size) {
                            val item = categoryItems[itemIndex]
                            Box(
                                modifier = Modifier
                                    .width(itemWidth)
                                    .aspectRatio(108f / 88f)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                    .clickable { onItemClick(item.text) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = item.imageRes),
                                        contentDescription = item.text,
                                        modifier = Modifier.size(with(density) { (48.dp.toPx() * scaleFactor).toDp() })
                                    )
                                    Spacer(modifier = Modifier.height(with(density) { (4.dp.toPx() * scaleFactor).toDp() }))
                                    Text(item.text, style = itemTextStyle.copy(
                                        fontSize = with(density) { (12.sp.toPx() * scaleFactor).toSp() }
                                    ))
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.width(itemWidth))
                        }
                    }
                }
                if (rowIndex < 4) {
                    Spacer(modifier = Modifier.height(verticalSpacing))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryPreview() {

    Category(
        onItemClick = {},  // 빈 람다 함수를 전달
        onCloseClick = {}
    )
}