package com.example.digital_contest.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * 온보딩 페이지 데이터 클래스
 * title 페이지 제목
 * description 페이지 설명
 */
data class OnboardingPageData(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val titleColor: Color = Color(15, 126, 0)
)

fun getOnboardingPages(): List<OnboardingPageData> = listOf(
    OnboardingPageData(
        title = "중고거래 글 작성에도\n비법이 있다는거 아시나요?",
        description = "Eco-Marketer가 다양한 말투로\n알아서 만들어 드립니다.\n시세와 플랫폼 분석까지 한 번에 조사하실 필요없이\n간편히 사용해보세요.",
        imageRes = com.example.digital_contest.R.drawable.write_view_1
    ),
    OnboardingPageData(
        title = "글쓰기 힘들고\n글을 올려도 잘 팔리지 않나요?",
        description = "Eco-Marketer는 둥글둥글체,단호박체,성냥팔이체 등\n다양한 말투를 사용하여\nAI를 통해 구매자의 눈을 사로잡고\n판매율을 올릴 수 있는 글을 작성해줍니다.",
        imageRes = com.example.digital_contest.R.drawable.write_view_2
    ),
    OnboardingPageData(
        title = "왜 이 플랫폼만\n물건이 안 팔릴까요?",
        description = "플랫폼 마다 선호하는 느낌이 달라요!\nEco-Marketer가 다 분석해 놨으니\n더 효과적인 중고거래를 시작해보세요.",
        imageRes = com.example.digital_contest.R.drawable.write_view_3
    ),
    OnboardingPageData(
        title = "애증이 담긴 물건\n가격 측정 어렵죠?",
        description = "Eco-Marketerd가\n객관적인 시세를 알아볼 수 있도록 했습니다.\n여러 플랫폼의 가격을 검색하여\n같거나 비슷한 물건의 시세를\n빠르게 한눈에 볼 수 있어요!",
        imageRes = com.example.digital_contest.R.drawable.write_view_4
    ),
    OnboardingPageData(
        title = "중고 거래가\n 지구에 큰 도움이 된다는 걸 아시나요?",
        description = "매 횟수와 탄소 절감 기여도에 따라\n레벨이 증가하고 테마가 변해요!\n중고 거래를 통해 다양한 테마도 보고\n 탄소 절감도 도와봐요!",
        imageRes = com.example.digital_contest.R.drawable.write_view_5
    )
)

