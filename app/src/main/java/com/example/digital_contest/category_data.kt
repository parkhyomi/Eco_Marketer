package com.example.digital_contest

import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.example.digital_contest.API.CategoryData
import com.example.digital_contest.API.CategoryDetail
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun CategoryChart(category: String, categoryDetail: CategoryDetail) {
    val context = LocalContext.current
    val categoryData = remember(category, categoryDetail) {
        categoryDetail.data.find { it.target == category }
    }

    val categories = listOf("100도체", "둥글둥글체", "단호박체", "성냥팔이체", "귀욤체", "궁서체", "요점만체")

    val entries = remember(categoryData) {
        categories.mapIndexed { index, style ->
            val count = categoryData?.data?.find { it.introduceTextCategory == style }?.introduceTextCategoryCount?.toFloat() ?: 0f
            BarEntry(index.toFloat(), count)
        }
    }

    val typeface = Typeface.create(ResourcesCompat.getFont(context, R.font.pretendard_medium), Typeface.NORMAL)

    val chartRef = remember { mutableStateOf<BarChart?>(null) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { ctx ->
            BarChart(ctx).apply {
                chartRef.value = this

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
                    xOffset = 0f
                    axisMinimum = -0.5f
                    axisMaximum = categories.size - 0.5f
                    setCenterAxisLabels(false)
                    labelRotationAngle = 0f
                    this.typeface = typeface
                    textColor = Color.parseColor("#D3D3D3")
                }

                axisLeft.apply {
                    isEnabled = false
                }
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

                renderer = CustomBarChartRenderer(this, ChartAnimator(), viewPortHandler)
            }
        },
        update = { chart ->
            chartRef.value = chart
        }
    )

    LaunchedEffect(entries) {
        chartRef.value?.let { chart ->
            val dataSet = BarDataSet(entries, "Sample Data").apply {
                setDrawValues(false)
                color = Color.parseColor("#14AE5C")
            }

            val data = BarData(dataSet)
            data.barWidth = 0.7f
            chart.data = data

            chart.animateY(1000)
            chart.invalidate()
        }
    }
}