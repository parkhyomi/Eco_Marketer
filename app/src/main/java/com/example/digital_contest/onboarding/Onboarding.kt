package com.example.digital_contest

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.digital_contest.onboarding.OnboardingPageData
import com.example.digital_contest.onboarding.OnboardingViewModel
import com.example.digital_contest.onboarding.getOnboardingPages
import kotlinx.coroutines.launch

/**
 * Onboarding 메인 화면
 *
 * 적용된 OOP & SOLID 원칙:
 * 1. SRP (단일 책임): UI 렌더링만 담당, 비즈니스 로직은 ViewModel에
 * 2. OCP (개방-폐쇄): 페이지 추가 시 데이터만 추가하면 됨
 * 3. DIP (의존성 역전): ViewModel을 주입받아 사용
 *
 * @param navController 화면 전환용
 * @param viewModel 온보딩 로직 관리 (의존성 주입)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Onboarding(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel()
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

            // 시작 버튼 (첫 페이지가 아닐 때만)
            if (pagerState.currentPage != 0) {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.saveOnboardingCompleted()
                        }
                        navController.navigate("Login") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF14AE5C)
                    )
                ) {
                    Text("시작하기")
                }
            }
        }
    }
}

/**
 * 페이지 인디케이터 컴포넌트
 *
 * SRP: 페이지 인디케이터 표시만 담당
 */
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

/**
 * 개별 온보딩 페이지 컴포넌트
 *
 * SRP: 단일 페이지 렌더링만 담당
 * OCP: OnboardingPageData를 받아서 확장 가능
 */
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

            // 타이틀
            Text(
                text = pageData.title,
                color = pageData.titleColor,
                fontSize = with(LocalDensity.current) {
                    // 마지막 페이지는 폰트 조금 작게
                    (screenWidth * if (isLastPage) 0.058f else 0.064f).toSp()
                },
                lineHeight = with(LocalDensity.current) {
                    (screenWidth * 0.096f).toSp()
                },
                fontFamily = fontBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.047f))

            // 설명
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
