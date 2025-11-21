package com.example.digital_contest.Chart

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.example.digital_contest.API.Data.StatisDataStore
import com.example.digital_contest.R
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
private fun BaseBarChart(
    entries: List<BarEntry>,
    barColor: String,
    context: Context,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val categories = listOf("100도체", "둥글둥글체", "단호박체", "성냥팔이체", "귀욤체", "궁서체", "요점만체")
        val typeface = Typeface.create(
            ResourcesCompat.getFont(context, R.font.pretendard_medium),
            Typeface.NORMAL
        )

        AndroidView(
            modifier = modifier.fillMaxWidth().height(200.dp),
            factory = { BarChart(it).apply { configureChart(entries, categories, barColor, typeface) } }
        )
    }
}

// 차트 설정 확장 함수
private fun BarChart.configureChart(
    entries: List<BarEntry>,
    categories: List<String>,
    barColor: String,
    typeface: Typeface?
) {
    val dataSet = BarDataSet(entries, "Data").apply {
        setDrawValues(false)
        color = Color.parseColor(barColor)
    }

    data = BarData(dataSet).apply { barWidth = 0.7f }

    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        setDrawAxisLine(true)
        axisLineColor = Color.parseColor("#999999")
        axisLineWidth = 0.5f
        granularity = 1f
        valueFormatter = IndexAxisValueFormatter(categories)
        textSize = 10f
        yOffset = 5f
        axisMinimum = -0.5f
        axisMaximum = categories.size - 0.5f
        this.typeface = typeface
        textColor = Color.parseColor("#D3D3D3")
    }

    axisLeft.isEnabled = false
    axisRight.isEnabled = false
    setDrawBarShadow(false)
    setDrawValueAboveBar(false)
    description.isEnabled = false
    legend.isEnabled = false
    setDrawGridBackground(false)
    setDrawBorders(false)
    extraBottomOffset = 10f
    setViewPortOffsets(20f, 10f, 20f, 50f)
    setFitBars(true)
    setScaleEnabled(false)
    setDoubleTapToZoomEnabled(false)
    setTouchEnabled(false)
    animateY(1000)
    renderer = CustomBarChartRenderer(this, ChartAnimator(), viewPortHandler)
    invalidate()
}

// ============== 플랫폼 차트 (Strategy Pattern) ==============

@Composable
fun PlatformChart(platform: String, barColor: String, isMine: Boolean = false) {
    val context = LocalContext.current
    val dataStore = remember { StatisDataStore(context) }
    val companyDetail by (if (isMine) dataStore.mycompanyDetailData else dataStore.companyDetailData)
        .collectAsState(initial = CompanyDetail(emptyList()))

    val categories = listOf("100도체", "둥글둥글체", "단호박체", "성냥팔이체", "귀욤체", "궁서체", "요점만체")
    val entries = remember(companyDetail, platform) {
        val platformData = companyDetail.data.find { it.target == platform }?.data ?: emptyList()
        categories.mapIndexed { index, category ->
            val count = platformData.find { it.introduceTextCategory == category }
                ?.introduceTextCategoryCount?.toFloat() ?: 0f
            BarEntry(index.toFloat(), count)
        }
    }

    BaseBarChart(
        entries = entries,
        barColor = barColor,
        context = context,
        isLoading = companyDetail.data.isEmpty()
    )
}

// 개별 플랫폼 차트 (Factory Pattern)
@Composable
fun jongonara() = PlatformChart("중고나라", "#14AE5C", false)

@Composable
fun my_jongonara() = PlatformChart("중고나라", "#14AE5C", true)

@Composable
fun danggun() = PlatformChart("당근", "#FF8329", false)

@Composable
fun my_danggun() = PlatformChart("당근", "#FF8329", true)

@Composable
fun thunder() = PlatformChart("번개장터", "#FF0000", false)

@Composable
fun my_thunder() = PlatformChart("번개장터", "#FF0000", true)

// ============== 카테고리 차트 ==============

@Composable
fun CategoryChart(category: String, categoryDetail: CategoryDetail) {
    val context = LocalContext.current
    val categories = listOf("100도체", "둥글둥글체", "단호박체", "성냥팔이체", "귀욤체", "궁서체", "요점만체")

    val entries = remember(category, categoryDetail) {
        val data = categoryDetail.data.find { it.target == category }?.data ?: emptyList()
        categories.mapIndexed { index, style ->
            val count = data.find { it.introduceTextCategory == style }
                ?.introduceTextCategoryCount?.toFloat() ?: 0f
            BarEntry(index.toFloat(), count)
        }
    }

    BaseBarChart(entries, "#14AE5C", context)
}

@Composable
fun my_CategoryChart(category: String, categoryDetail: CategoryDetail) {
    CategoryChart(category, categoryDetail)
}

