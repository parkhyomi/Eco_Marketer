package com.example.digital_contest.Main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.digital_contest.R

// 거래 횟수 카드
@Composable
fun TradeCountCard(
    tradeCount: Int,
    maxHeight: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxHeight * 0.45f)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TradeCountText(tradeCount)
            Text(
                text = "탄소 절감에 기여했어요!",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 18.sp
            )
            TreeImage()
            Text(
                text = "더 참여하여 나무를 키워주세요!",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun TradeCountText(count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "물건 거래 횟수",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 18.sp
        )
        Text(
            text = "  ${count}번 ",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            color = Color(20, 174, 92),
            fontSize = 24.sp
        )
        Text(
            text = "만큼",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 18.sp
        )
    }
}

@Composable
private fun TreeImage() {
    Box(modifier = Modifier.size(200.dp)) {
        Image(
            painter = painterResource(id = R.drawable.mainview_1_lv),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        )
    }
}

// 레벨 카드
@Composable
fun LevelCard(
    myExperience: Int,
    levelExperience: Int,
    myLevel: Int,
    maxHeight: Dp,
    onWriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(maxHeight * 0.26f)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            LevelHeader(myExperience, levelExperience)
            Text(text = "LEVEL $myLevel", fontSize = 12.sp)
            Spacer(modifier = Modifier.padding(2.dp))
            LevelContent(myExperience, levelExperience, maxHeight, onWriteClick)
        }
    }
}

@Composable
private fun LevelHeader(myExperience: Int, levelExperience: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "나의 환경 지킴 레벨🌱",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 18.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$myExperience",
                color = Color(20, 174, 92),
                fontSize = 32.sp
            )
            Text(text = " / ${levelExperience}P", fontSize = 20.sp)
        }
    }
}

@Composable
private fun LevelContent(
    myExperience: Int,
    levelExperience: Int,
    maxHeight: Dp,
    onWriteClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBar(myExperience, levelExperience)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "에코 포인트로 등급을 올려 환경 지킴 레벨을 올려요!",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = onWriteClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(20, 174, 92)),
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight * 0.06f)
        ) {
            Text(text = "AI와 함께 새로운 글 작성하기", fontSize = 17.sp)
        }
    }
}

@Composable
private fun ProgressBar(current: Int, target: Int) {
    val progress = if (target > 0) (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f

    Box(
        modifier = Modifier
            .height(8.dp)
            .fillMaxWidth()
            .background(Color(0xFFA9A9A9), RoundedCornerShape(99.dp))
            .clip(RoundedCornerShape(99.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(8.dp)
                .background(Color(0xFF14AE5C), RoundedCornerShape(99.dp))
        )
    }
}

// 하단 네비게이션 바
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String = "main"
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val navBarHeight = (maxHeight * 0.2f).coerceAtLeast(60.dp).coerceAtMost(88.dp)

        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight)
                .border(BorderStroke(1.dp, Color.LightGray), shape = RectangleShape)
        ) {
            NavigationItem(
                iconRes = R.drawable.symbol_main,
                title = "메인",
                navBarHeight = navBarHeight,
                isSelected = currentRoute == "main",
                onClick = { navController.navigate("main"){popUpTo("main"){inclusive = true} }}
            )
            NavigationItem(
                iconRes = R.drawable.state_symbol,
                title = "통계",
                navBarHeight = navBarHeight,
                isSelected = currentRoute == "stats",
                onClick = { navController.navigate("stats") { popUpTo("main"){inclusive = false} } }
            )
            NavigationItem(
                iconRes = R.drawable.price_symbol,
                title = "시세",
                navBarHeight = navBarHeight,
                isSelected = currentRoute == "market",
                onClick = { navController.navigate("market") { popUpTo("main"){inclusive = false} } }
            )
            NavigationItem(
                iconRes = R.drawable.mypage_symbol,
                title = "마이페이지",
                navBarHeight = navBarHeight,
                isSelected = currentRoute == "mypage",
                onClick = {
                    navController.navigate("mypage") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.NavigationItem(
    iconRes: Int,
    title: String,
    navBarHeight: Dp,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = Color(0xFF14AE5C)
    val unselectedColor = Color(0xFF999999)

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size((navBarHeight * 0.3f).coerceAtMost(24.dp)),
                tint = if (isSelected) selectedColor else unselectedColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = with(LocalDensity.current) {
                    minOf(navBarHeight * 0.12f, 10.dp).toSp()
                },
                color = if (isSelected) selectedColor else unselectedColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
