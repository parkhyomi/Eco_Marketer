package com.example.digital_contest.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.digital_contest.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Onboarding(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModelFactory(LocalContext.current)
    )
) {
    val pages = getOnboardingPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        Box(modifier = Modifier.fillMaxSize()) {

            // 페이지 인디케이터
            PageIndicator(
                currentPage = pagerState.currentPage,
                totalPages = pages.size,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = screenHeight * 0.065f)
            )

            // 페이지 콘텐츠
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                OnboardingPage(
                    pageData = pages[page],
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    isLastPage = page == pages.size - 1
                )
            }

            if (pagerState.currentPage == pages.size - 1) {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.saveCompleted()
                        }
                        navController.navigate("login") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF14AE5C)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("시작하기")
                }
            }
        }
    }
}

@Composable
private fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(90.dp)
            .height(20.dp)
            .background(Color(0xFFBFBFBF).copy(0.44f), RoundedCornerShape(50.dp))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(totalPages) { index ->
                val color = if (currentPage == index) {
                    Color(0xFF0D0D0D)
                } else {
                    Color.Gray
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    pageData: OnboardingPageData,
    screenWidth: Dp,
    screenHeight: Dp,
    isLastPage: Boolean
) {
    val fontBold = FontFamily(Font(R.font.pretendard_bold))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = screenHeight * 0.065f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 텍스트 영역
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.072f))

            Text(
                text = pageData.title,
                color = pageData.titleColor,
                fontSize = with(LocalDensity.current) {
                    (screenWidth * if (isLastPage) 0.058f else 0.064f).toSp()
                },
                lineHeight = with(LocalDensity.current) {
                    (screenWidth * 0.096f).toSp()
                },
                fontFamily = fontBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.047f))

            Text(
                text = pageData.description,
                fontSize = with(LocalDensity.current) {
                    (screenWidth * 0.0427f).toSp()
                },
                lineHeight = with(LocalDensity.current) {
                    (screenWidth * 0.064f).toSp()
                },
                fontFamily = fontBold,
                textAlign = TextAlign.Center
            )
        }

        // 이미지 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.52f)
        ) {
            Image(
                painter = painterResource(id = pageData.imageRes),
                contentDescription = "Onboarding Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
