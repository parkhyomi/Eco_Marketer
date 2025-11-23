package com.example.digital_contest.Write.Dialog

import androidx.compose.ui.graphics.Color
import com.example.digital_contest.BuildConfig


data class Platform(
    val name: String,
    val deepLink: String,
    val webUrl: String,
    val packageName: String,
    val buttonColor: Color,
    val displayText: String
)

object PlatformConfig {

    // 중고나라 플랫폼 정보
    private val JOONGNA = Platform(
        name = "중고나라",
        deepLink = "${BuildConfig.JOONGNA_SCHEME}://?applink=main",
        webUrl = "https://${BuildConfig.JOONGNA}/",
        packageName = "com.fm.joonggonara",
        buttonColor = Color(0xFF14AE5C),
        displayText = "중고나라 글쓰러가기"
    )

    // 당근 플랫폼 정보
    private val KARROT = Platform(
        name = "당근",
        deepLink = "${BuildConfig.DAANGN_SCHEME}://",
        webUrl = "https://${BuildConfig.DAANGN}/",
        packageName = "com.towneers.www",
        buttonColor = Color(0xFFFF8329),
        displayText = "당근 글쓰러가기"
    )

    // 번개장터 플랫폼 정보
    private val BUNJANG = Platform(
        name = "번개장터",
        deepLink = "${BuildConfig.BUNGANG_SCHEME}:/",
        webUrl = "https://${BuildConfig.BUNGANG}",
        packageName = "com.bunjang.bundroid",
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

