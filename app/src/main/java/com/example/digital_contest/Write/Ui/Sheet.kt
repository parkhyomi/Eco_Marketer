package com.example.digital_contest.Write.Ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_contest.R

interface SheetItem {
    val text: String
    val color: Color
        get() = Color.Black
}

data class IconSheetItem(
    @DrawableRes val imageRes: Int,
    override val text: String,
    override val color: Color = Color.Black
) : SheetItem

data class TextOnlySheetItem(
    override val text: String,
    override val color: Color
) : SheetItem

data class SheetLayoutConfig(
    val columns: Int = 3,
    val horizontalPadding: Dp = 22.5.dp,
    val verticalSpacing: Dp = 12.dp,
    val itemSpacing: Dp = 12.dp
) {
    companion object {
        val DEFAULT = SheetLayoutConfig()
        val SINGLE_COLUMN = SheetLayoutConfig(columns = 1)
    }
}

data class SheetStyleConfig(
    val titleTextStyle: TextStyle,
    val itemTextStyle: TextStyle
) {
    companion object {
        val DEFAULT = SheetStyleConfig(
            titleTextStyle = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                lineHeight = 25.sp,
                color = Color(0xFF000000)
            ),
            itemTextStyle = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                lineHeight = 18.sp,
                color = Color(0xFF000000)
            )
        )

        val BOLD_ITEMS = SheetStyleConfig(
            titleTextStyle = DEFAULT.titleTextStyle,
            itemTextStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                lineHeight = 24.sp
            )
        )
    }
}

@Composable
fun GridSelectionSheet(
    title: String,
    items: List<SheetItem>,
    onItemClick: (String) -> Unit,
    layoutConfig: SheetLayoutConfig = SheetLayoutConfig.DEFAULT,
    styleConfig: SheetStyleConfig = SheetStyleConfig.DEFAULT
) {
    val refColumns = 3
    val iconItemWidth = with(LocalDensity.current) {
        ((350.dp - (layoutConfig.itemSpacing * (refColumns - 1))) / refColumns)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = layoutConfig.horizontalPadding)
            .padding(bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(42.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = title, style = styleConfig.titleTextStyle)
        }
        Spacer(modifier = Modifier.height(layoutConfig.verticalSpacing))

        val rowCount = (items.size + layoutConfig.columns - 1) / layoutConfig.columns
        repeat(rowCount) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(layoutConfig.itemSpacing)
            ) {
                repeat(layoutConfig.columns) { columnIndex ->
                    val itemIndex = rowIndex * layoutConfig.columns + columnIndex
                    if (itemIndex < items.size) {
                        val item = items[itemIndex]
                        when (item) {
                            is IconSheetItem -> {
                                Box(
                                    modifier =
                                        if (layoutConfig.columns == 1)
                                            Modifier.width(iconItemWidth) // 1컬럼 -> 3컬럼 기준 카드 크기
                                                .aspectRatio(1f)
                                        else
                                            Modifier.weight(1f).aspectRatio(1f)
                                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                                .clickable { onItemClick(item.text) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = item.imageRes),
                                            contentDescription = item.text,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.text,
                                            style = styleConfig.itemTextStyle,
                                            color = item.color
                                        )
                                    }
                                }
                            }
                            is TextOnlySheetItem -> {
                                Box(
                                    modifier = Modifier.weight(1f)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                        .padding(vertical = 12.dp)
                                        .clickable { onItemClick(item.text) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.text,
                                        style = styleConfig.itemTextStyle,
                                        color = item.color
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            if (rowIndex < rowCount - 1) {
                Spacer(modifier = Modifier.height(layoutConfig.verticalSpacing))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

interface SheetDataProvider {
    val title: String
    val items: List<SheetItem>
    val layoutConfig: SheetLayoutConfig
    val styleConfig: SheetStyleConfig
}


object PlatformDataProvider : SheetDataProvider {
    override val title: String = "플랫폼 선택"

    override val items: List<SheetItem> = listOf(
        TextOnlySheetItem(text = "중고나라", color = Color(0xFF14AE5C)),
        TextOnlySheetItem(text = "당근", color = Color(0xFFFF8329)),
        TextOnlySheetItem(text = "번개장터", color = Color(0xFFFF0000))
    )

    override val layoutConfig: SheetLayoutConfig = SheetLayoutConfig.SINGLE_COLUMN

    override val styleConfig: SheetStyleConfig = SheetStyleConfig.BOLD_ITEMS
}


object CategoryDataProvider : SheetDataProvider {
    override val title: String = "카테고리"

    override val items: List<SheetItem> = listOf(
        IconSheetItem(R.drawable.icon1, "디지털 기기"),
        IconSheetItem(R.drawable.ticket_icon, "티켓/교환권"),
        IconSheetItem(R.drawable.icon3, "여성 패션/잡화"),
        IconSheetItem(R.drawable.icon4, "남성 패션/잡화"),
        IconSheetItem(R.drawable.icon5, "생활 가전"),
        IconSheetItem(R.drawable.icon6, "생활 주방"),
        IconSheetItem(R.drawable.icon7, "스포츠/레저"),
        IconSheetItem(R.drawable.icon8, "취미/게임/음악"),
        IconSheetItem(R.drawable.icon9, "뷰티/미용"),
        IconSheetItem(R.drawable.icon10, "식물"),
        IconSheetItem(R.drawable.icon11, "가공식품"),
        IconSheetItem(R.drawable.icon12, "건강기능식품"),
        IconSheetItem(R.drawable.icon13, "반려동물"),
        IconSheetItem(R.drawable.icon14, "도서"),
        IconSheetItem(R.drawable.icon15, "기타 중고")
    )

    override val layoutConfig: SheetLayoutConfig = SheetLayoutConfig.DEFAULT

    override val styleConfig: SheetStyleConfig = SheetStyleConfig.DEFAULT
}

object SaleConceptDataProvider : SheetDataProvider {
    override val title: String = "판매 컨셉"

    override val items: List<SheetItem> = listOf(
        IconSheetItem(R.drawable.sale_icon1, "당근100도체"),
        IconSheetItem(R.drawable.sale_icon2, "둥글둥글체"),
        IconSheetItem(R.drawable.sale_icon3, "단호박체"),
        IconSheetItem(R.drawable.sale_icon4, "성냥팔이체"),
        IconSheetItem(R.drawable.sale_icon5, "귀욤체"),
        IconSheetItem(R.drawable.sale_icon6, "궁서체"),
        IconSheetItem(R.drawable.sale_icon7, "요점만체")
    )

    override val layoutConfig: SheetLayoutConfig = SheetLayoutConfig.DEFAULT

    override val styleConfig: SheetStyleConfig = SheetStyleConfig.DEFAULT
}

@Composable
fun PlatformSheet(
    onItemClick: (String) -> Unit,
    dataProvider: SheetDataProvider = PlatformDataProvider
) {
    GridSelectionSheet(
        title = dataProvider.title,
        items = dataProvider.items,
        onItemClick = onItemClick,
        layoutConfig = dataProvider.layoutConfig,
        styleConfig = dataProvider.styleConfig
    )
}

@Composable
fun CategorySheet(
    onItemClick: (String) -> Unit,
    dataProvider: SheetDataProvider = CategoryDataProvider
) {
    GridSelectionSheet(
        title = dataProvider.title,
        items = dataProvider.items,
        onItemClick = onItemClick,
        layoutConfig = dataProvider.layoutConfig,
        styleConfig = dataProvider.styleConfig
    )
}

@Composable
fun SaleConceptSheet(
    onItemClick: (String) -> Unit,
    dataProvider: SheetDataProvider = SaleConceptDataProvider
) {
    GridSelectionSheet(
        title = dataProvider.title,
        items = dataProvider.items,
        onItemClick = onItemClick,
        layoutConfig = dataProvider.layoutConfig,
        styleConfig = dataProvider.styleConfig
    )
}