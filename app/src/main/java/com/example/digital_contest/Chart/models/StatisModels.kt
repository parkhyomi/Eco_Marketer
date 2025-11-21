package com.example.digital_contest.Chart.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

// 통합된 통계 데이터 모델
@Serializable
data class StatisData(
    val target: String,
    val introduceTextCategory: String,
    val introduceTextCategoryCount: String
)

@Serializable
data class StatisResponse(
    val data: List<StatisData>
)

@Serializable
data class StatisDetail(
    val data: List<StatisDetailItem>
)

@Serializable
data class StatisDetailItem(
    val target: String,
    val data: List<StatisDetailData>
)

@Serializable
data class StatisDetailData(
    val introduceTextCategory: String,
    val introduceTextCategoryCount: String
)

// 플랫폼 설정을 위한 enum
enum class Platform(
    val displayName: String,
    val color: Color
) {
    JOONGONARA("중고나라", Color(0xFF14AE5C)),
    DANGGEUN("당근", Color(0xFFFFA629)),
    BUNGAE("번개장터", Color(0xFFFF0000));
}

// 카테고리 목록
object CategoryConstants {
    val ALL_CATEGORIES = listOf(
        "디지털기기", "가구/인테리어", "여성패션/잡화", "남성패션/잡화", "생활가전",
        "생활주방", "스포츠/레저", "취미/게임/음반", "뷰티/미용", "식물",
        "가공식품", "건강기능식품", "반려동물용품", "티켓/교환권", "도서", "기타중고"
    )
}

