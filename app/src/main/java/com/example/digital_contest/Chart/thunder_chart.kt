//package com.example.digital_contest.Chart
//
//import android.graphics.Color
//import android.graphics.Typeface
//import android.util.Log
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.material.CircularProgressIndicator
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.res.ResourcesCompat
//import com.example.digital_contest.API.CompanyDetail
//import com.example.digital_contest.API.Data.StatisDataStore
//import com.example.digital_contest.R
//import com.github.mikephil.charting.animation.ChartAnimator
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
//
//@Composable
//fun my_thunder() {
//    val context = LocalContext.current
//    val companyDetailDataStore = remember { StatisDataStore(context) }
//    val companyDetailData by companyDetailDataStore.mycompanyDetailData.collectAsState(initial = CompanyDetail(emptyList()))
//    var isDataLoaded = remember { mutableStateOf(false) }
//
//    LaunchedEffect(companyDetailData) {
//        isDataLoaded.value = companyDetailData.data.isNotEmpty()
//    }
//
//    val categories = listOf("100도체", "둥글둥글체", "단호박체", "성냥팔이체", "귀욤체", "궁서체", "요점만체")
//
//    val entries = remember(companyDetailData) {
//        val danggunData = companyDetailData.data.find { it.target == "번개장터" }?.data ?: emptyList()
//        categories.mapIndexed { index, category ->
//            val count = danggunData.find { it.introduceTextCategory == category }?.introduceTextCategoryCount?.toFloat() ?: 0f
//            BarEntry(index.toFloat(), count)
//        }
//    }
//
//    LaunchedEffect(entries) {
//        Log.d("ChartData", "Entries: $entries")
//    }
//
//    val typeface = Typeface.create(ResourcesCompat.getFont(context, R.font.pretendard_medium), Typeface.NORMAL)
//
//    if (isDataLoaded.value) {
//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp),
//            factory = { ctx ->
//                BarChart(ctx).apply {
//                    val dataSet = BarDataSet(entries, "Sample Data").apply {
//                        setDrawValues(false)
//                        color = Color.parseColor("#FF0000")
//                    }
//
//                    val data = BarData(dataSet)
//                    data.barWidth = 0.7f
//                    this.data = data
//
//                    xAxis.apply {
//                        position = XAxis.XAxisPosition.BOTTOM
//                        setDrawGridLines(false)
//                        setDrawAxisLine(true)
//                        axisLineColor = Color.parseColor("#999999")
//                        axisLineWidth = 0.5f
//                        granularity = 1f
//                        valueFormatter = IndexAxisValueFormatter(categories)
//                        textSize = 10f
//                        yOffset = 5f
//                        xOffset = 0f
//                        axisMinimum = -0.5f
//                        axisMaximum = categories.size - 0.5f
//                        setCenterAxisLabels(false)
//                        labelRotationAngle = 0f
//
//                        this.typeface = typeface
//                        textColor = Color.parseColor("#D3D3D3")
//                    }
//
//                    axisLeft.isEnabled = false
//                    axisRight.isEnabled = false
//
//                    setDrawBarShadow(false)
//                    setDrawValueAboveBar(false)
//                    description.isEnabled = false
//                    legend.isEnabled = false
//                    setDrawGridBackground(false)
//                    setDrawBorders(false)
//
//                    extraBottomOffset = 10f
//                    setViewPortOffsets(20f, 10f, 20f, 50f)
//                    setFitBars(true)
//                    setScaleEnabled(false)
//                    setDoubleTapToZoomEnabled(false)
//                    setTouchEnabled(false)
//
//                    animateY(1000)
//
//                    renderer = CustomBarChartRenderer(this, ChartAnimator(), viewPortHandler)
//
//                    invalidate()
//                }
//            }
//        )
//    } else {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }
//}