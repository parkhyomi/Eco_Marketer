package com.example.digital_contest.Write

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_contest.R

@Composable
fun Platform(
    onPlatformClick: (String) -> Unit,
    onCloseClick: () -> Unit,
    titleTextStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
        lineHeight = 25.sp,
        color = Color(0xFF000000)
    )
) {
    val platformItems = listOf(
        "중고나라" to TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            lineHeight = 24.sp,
            color = Color(0xFF14AE5C)
        ),
        "당근" to TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            lineHeight = 24.sp,
            color = Color(0xFFFF8329)
        ),
        "번개장터" to TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            lineHeight = 24.sp,
            color = Color(0xFFFF0000)
        )
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val boxWidth = 362.dp
        val boxHeight = 48.dp
        val horizontalPadding = 16.dp
        val verticalSpacing = 8.dp
        val topSpacing = 12.dp

        val density = LocalDensity.current
        val scaleFactor = with(density) {
            screenWidth.toPx() / (boxWidth.toPx() + 2 * horizontalPadding.toPx())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = with(density) { (horizontalPadding.toPx() * scaleFactor).toDp() })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { (42.dp.toPx() * scaleFactor).toDp() })
                    .padding(top = with(density) { (14.dp.toPx() * scaleFactor).toDp() }),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    "플랫폼 선택",
                    style = titleTextStyle.copy(fontSize = with(density) { (20.sp.toPx() * scaleFactor).toSp() })
                )
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

            Spacer(modifier = Modifier.height(with(density) { (topSpacing.toPx() * scaleFactor).toDp() }))

            platformItems.forEach { (item, style) ->
                Box(
                    modifier = Modifier
                        .width(with(density) { (boxWidth.toPx() * scaleFactor).toDp() })
                        .height(with(density) { (boxHeight.toPx() * scaleFactor).toDp() })
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .clickable { onPlatformClick(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        item,
                        style = style.copy(fontSize = with(density) { (16.sp.toPx() * scaleFactor).toSp() })
                    )
                }
                Spacer(modifier = Modifier.height(with(density) { (verticalSpacing.toPx() * scaleFactor).toDp() }))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlatformPreview() {
    Platform(
        onPlatformClick = {},  // 빈 람다 함수를 전달
        onCloseClick = {}  // 빈 람다 함수를 전달
    )
}