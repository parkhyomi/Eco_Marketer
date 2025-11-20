package com.example.digital_contest.Main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainView(navController: NavHostController) {

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
                tradeCount = 0,
                maxHeight = maxHeight
            )

            Spacer(modifier = Modifier.height(30.dp))

            LevelCard(
                myExperience = 0,
                levelExperience = 1,
                myLevel = 1,
                maxHeight = maxHeight,
                onWriteClick = {
                    navController.navigate("write"){popUpTo("main"){inclusive = true} }
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