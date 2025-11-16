package com.example.digital_contest.Write.Dialog

import androidx.compose.ui.graphics.Color
import com.example.digital_contest.BuildConfig


data class Platform(
    val name: String,           // 플랫폼 이름
    val deepLink: String,       // 앱 딥링크 URL
    val webUrl: String,         // 웹사이트 URL
    val buttonColor: Color,     // 버튼 텍스트 색상
    val displayText: String     // 버튼에 표시될 텍스트
)

object PlatformConfig {

    // 중고나라 플랫폼 정보
    private val JOONGNA = Platform(
        name = "중고나라",
        deepLink = "${BuildConfig.JOONGNA_SCHEME}://?applink=main",
        webUrl = "https://${BuildConfig.JOONGNA}/",
        buttonColor = Color(0xFF14AE5C),
        displayText = "중고나라 글쓰러가기"
    )

    // 당근 플랫폼 정보
    private val KARROT = Platform(
        name = "당근",
        deepLink = "${BuildConfig.DAANGN_SCHEME}://",
        webUrl = "https://${BuildConfig.DAANGN}/",
        buttonColor = Color(0xFFFF8329),
        displayText = "당근 글쓰러가기"
    )

    // 번개장터 플랫폼 정보
    private val BUNJANG = Platform(
        name = "번개장터",
        deepLink = "${BuildConfig.BUNGANG_SCHEME}:/",
        webUrl = "https://${BuildConfig.BUNGANG}",
        buttonColor = Color(0xFFFF0000),
        displayText = "번개장터 글쓰러가기"
    )

    /**
     * 사용 가능한 모든 플랫폼 목록 반환
     */
    fun getAllPlatforms(): List<Platform> {
        return listOf(JOONGNA, KARROT, BUNJANG)
    }
}

