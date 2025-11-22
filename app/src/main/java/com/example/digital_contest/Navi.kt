package com.example.digital_contest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digital_contest.Chart.StatsScreen
import com.example.digital_contest.Login.LoginView
import com.example.digital_contest.Main.MainView
import com.example.digital_contest.Mypage.MyPageScreen
import com.example.digital_contest.Write.WriteView
import com.example.digital_contest.onboarding.Onboarding

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // AppViewModel로 시작 화면 결정
    val appViewModel: AppViewModel = viewModel(
        factory = AppViewModelFactory(context)
    )

    val startDestination by appViewModel.startDestination.collectAsState()

    NavigationGraph(
        navController = navController,
        startDestination =
            "main"
//            startDestination
    )
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 온보딩 화면
        composable("onboarding") {
            Onboarding(navController = navController)
        }

        // 로그인 화면
        composable("login") {
            LoginView(navController = navController)
        }

        // 메인 화면
        composable("main") {
            MainView(navController = navController)
        }

        // 글쓰기 화면
        composable("write") {
            WriteView(navController= navController)
        }

        //시세 화면
         composable("stats") {
             StatsScreen(navController = navController)
         }

        // 마이페이지
        composable("mypage") {
             MyPageScreen(navController = navController)
        }
    }
}
