package com.example.digital_contest.Main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainView(
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val state = viewModel.screenState.value

    // 토큰 가져오기
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", "") ?: ""

        if (accessToken.isNotEmpty()) {
            viewModel.loadMainData(accessToken)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(242, 242, 247)),
        contentAlignment = Alignment.BottomCenter
    ) {
        val maxHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TradeCountCard(
                tradeCount = state.tradeCount,
                maxHeight = maxHeight
            )

            Spacer(modifier = Modifier.height(30.dp))

            LevelCard(
                myExperience = state.myExperience,
                levelExperience = state.levelExperience,
                myLevel = state.myLevel,
                maxHeight = maxHeight,
                onWriteClick = {
                    navController.navigate("write") { popUpTo("main") { inclusive = true } }
                }
            )
        }
        BottomNavigationBar(
            navController = navController,
            currentRoute = "main"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    MainView(navController = rememberNavController())
}