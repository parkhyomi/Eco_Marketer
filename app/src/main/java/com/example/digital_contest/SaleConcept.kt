package com.example.digital_contest

import androidx.compose.runtime.Composable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaleConcept(
    onConceptClick: (String) -> Unit,
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
    val saleConceptItems = listOf(
        CategoryItem(R.drawable.sale_icon1, "당근100도체"),
        CategoryItem(R.drawable.sale_icon2, "둥글둥글체"),
        CategoryItem(R.drawable.sale_icon3, "단호박체"),
        CategoryItem(R.drawable.sale_icon4, "성냥팔이체"),
        CategoryItem(R.drawable.sale_icon5, "귀욤체"),
        CategoryItem(R.drawable.sale_icon6, "궁서체"),
        CategoryItem(R.drawable.sale_icon7, "요점만체")
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
                Text("판매 컨셉", style = titleTextStyle.copy(
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

            repeat(3) { rowIndex ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing)
                ) {
                    repeat(if (rowIndex < 2) 3 else 1) { columnIndex ->
                        val itemIndex = rowIndex * 3 + columnIndex
                        if (itemIndex < saleConceptItems.size) {
                            val item = saleConceptItems[itemIndex]
                            Box(
                                modifier = Modifier
                                    .width(itemWidth)
                                    .aspectRatio(108f / 88f)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                    .clickable { onConceptClick(item.text) },
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
                        } else if (rowIndex == 2) {
                            Spacer(modifier = Modifier.width(itemWidth))
                        }
                    }
                    if (rowIndex == 2) {
                        Spacer(modifier = Modifier.weight(2f))
                    }
                }
                if (rowIndex < 2) {
                    Spacer(modifier = Modifier.height(verticalSpacing))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SaleConceptPreview() {

    SaleConcept(
        onConceptClick = {},  // 빈 람다 함수를 전달
        onCloseClick = {}
    )
}