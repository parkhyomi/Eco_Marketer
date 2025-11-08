package com.example.digital_contest

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digital_contest.ui.theme.Digital_ContestTheme
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_contest.Viewmodel.MyPageViewModel
import com.example.digital_contest.Viewmodel.MyPageViewModelFactory
import com.example.digital_contest.API.Main.UserPreferencesRepository
import com.example.digital_contest.Write.WriteView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private const val USER_PREFERENCES_NAME = "user_preferences"
//확장 프로퍼티를 정의,datastore 이름정의
val Context.dataStore :DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME //datastore의 인스턴스를 생성한다.
)


class MainActivity : ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)
    private lateinit var myPageViewModel: MyPageViewModel

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isDarkModeEnabled()) {
            showLightModeRequiredDialog()
            return  // 다이얼로그를 표시한 후 onCreate 메서드를 종료합니다.
        }

        myPageViewModel = ViewModelProvider(this, MyPageViewModelFactory(this))
            .get(MyPageViewModel::class.java)

        val user = UserPreferencesRepository(dataStore)
        val viewModel = ViewModelProvider(this, TaskViewModelFactory(user))
            .get(MainActivityViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            val state by remember {
                mutableStateOf(false)
            }

            Digital_ContestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {//StatisTest(viewModel: StatisViewModel)
                        //MainContent(navHostController = navController, myPageViewModel)
                        composable("splash") { SplashView(navController, viewModel) }
                        composable("main") { MainContent(navHostController = navController, myPageViewModel) }
                        composable("login") { LoginView(navController) }
                        composable("onboarding") { onboarding(viewModel, navController) }
                        composable("write") { WriteView(navController,viewModel=myPageViewModel) }
                    }
                }
            }
        }
    }

    private fun isDarkModeEnabled(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    private fun showLightModeRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle("알림")
            .setMessage("이 앱은 라이트 모드에서만 실행할 수 있습니다. 시스템 설정에서 라이트 모드로 변경 후 다시 실행해 주세요.")
            .setPositiveButton("확인") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}

    @Preview(showBackground = true)
    @ExperimentalFoundationApi
    @Composable
    fun GreetingPreview() {
        Digital_ContestTheme {
            val navController = rememberNavController() //화면 이동을 위한 변수]
            LoginView(navHostController = navController)
        }
    }


class TaskViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository
):ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(userPreferencesRepository) as T

        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}

class MainActivityViewModel(private val userPreferencesRepository: UserPreferencesRepository):ViewModel() {
    val onboardingState: Flow<Boolean> = userPreferencesRepository.onboardingState

    fun saveOnboardIngState(completed:Boolean){
        viewModelScope.launch {
            userPreferencesRepository.saveOnboardingstate(completed)
        }
    }
}