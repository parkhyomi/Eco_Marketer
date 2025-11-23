package com.example.digital_contest.Main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_contest.API.APIRetrofit
import kotlinx.coroutines.launch

// 메인 화면 상태를 관리하는 데이터 클래스
data class MainScreenState(
    val tradeCount: Int = 0,
    val myExperience: Int = 0,
    val levelExperience: Int = 1,
    val myLevel: Int = 1,
    val levelColor: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class MainViewModel : ViewModel() {
    private val _screenState = mutableStateOf(MainScreenState())
    val screenState: State<MainScreenState> = _screenState

    private val mainService: MainService = APIRetrofit.createService<MainService>()

    fun loadMainData(accessToken: String) {
        viewModelScope.launch {
            _screenState.value = _screenState.value.copy(isLoading = true, error = null)

            try {
                // 모든 데이터를 순차적으로 로드
                loadTradeCount(accessToken)
                loadLevelExperience(accessToken)
                loadLevelColor(accessToken)

                _screenState.value = _screenState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _screenState.value = _screenState.value.copy(
                    error = "데이터 로드 실패: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private suspend fun loadTradeCount(accessToken: String) {
        try {
            val response = mainService.getTradeCountData("Bearer $accessToken")

            Log.d("MainViewModel", "거래 횟수 응답 코드: ${response.code()}")
            Log.d("MainViewModel", "거래 횟수 응답 성공: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val tradeCount = response.body()!!.data.count
                _screenState.value = _screenState.value.copy(tradeCount = tradeCount)
                Log.d("MainViewModel", "거래 횟수 조회 성공: $tradeCount")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("MainViewModel", "거래 횟수 조회 실패 (${response.code()}): $errorBody")
                _screenState.value = _screenState.value.copy(error = "거래 횟수 조회 실패")
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "거래 횟수 조회 에러: ${e.message}", e)
            _screenState.value = _screenState.value.copy(error = "네트워크 오류: ${e.message}")
        }
    }

    private suspend fun loadLevelExperience(accessToken: String) {
        try {
            val response = mainService.levelexperience("Bearer $accessToken")

            Log.d("MainViewModel", "레벨 정보 응답 코드: ${response.code()}")
            Log.d("MainViewModel", "레벨 정보 응답 성공: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.data
                _screenState.value = _screenState.value.copy(
                    myExperience = data.myLevelExperience,
                    levelExperience = data.levelExperience,
                    myLevel = data.myLevel
                )
                Log.d("MainViewModel", "레벨 정보 조회 성공: Level ${data.myLevel}, Exp ${data.myLevelExperience}/${data.levelExperience}")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("MainViewModel", "레벨 정보 조회 실패 (${response.code()}): $errorBody")
                _screenState.value = _screenState.value.copy(error = "레벨 정보 조회 실패")
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "레벨 정보 조회 에러: ${e.message}", e)
            _screenState.value = _screenState.value.copy(error = "네트워크 오류: ${e.message}")
        }
    }

    private suspend fun loadLevelColor(accessToken: String) {
        try {
            val response = mainService.getlevelcolor("Bearer $accessToken")

            Log.d("MainViewModel", "레벨 색상 응답 코드: ${response.code()}")
            Log.d("MainViewModel", "레벨 색상 응답 성공: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val color = response.body()!!.data.color
                _screenState.value = _screenState.value.copy(levelColor = color)
                Log.d("MainViewModel", " 레벨 색상 조회 성공: $color")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("MainViewModel", "레벨 색상 조회 실패 (${response.code()}): $errorBody")
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "레벨 색상 조회 에러: ${e.message}", e)
        }
    }
}

