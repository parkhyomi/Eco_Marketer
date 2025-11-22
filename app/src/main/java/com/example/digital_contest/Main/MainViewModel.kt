package com.example.digital_contest.Main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_contest.API.APIRetrofit
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class MainScreenState(
    val tradeCount: Int = 0,
    val myExperience: Int = 0,
    val levelExperience: Int = 1,
    val myLevel: Int = 1,
    val levelColor: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)

class MainViewModel : ViewModel() {
    private val _screenState = mutableStateOf(MainScreenState())
    val screenState: State<MainScreenState> = _screenState

    private val mainService: MainService = APIRetrofit.createService<MainService>()

    fun loadMainData(accessToken: String) {
        viewModelScope.launch {
            _screenState.value = _screenState.value.copy(isLoading = true, error = null)

            // 거래 횟수 조회
            loadTradeCount(accessToken)

            // 레벨 및 경험치 조회
            loadLevelExperience(accessToken)

            // 레벨 색상 조회
            loadLevelColor(accessToken)
        }
    }

    private fun loadTradeCount(accessToken: String) {
        mainService.getTradeCountData("Bearer $accessToken").enqueue(object : Callback<tradeDataResponse> {
            override fun onResponse(
                call: Call<tradeDataResponse>,
                response: Response<tradeDataResponse>
            ) {
                if (response.isSuccessful) {
                    val tradeCount = response.body()?.data?.count ?: 0
                    _screenState.value = _screenState.value.copy(
                        tradeCount = tradeCount,
                        isLoading = false
                    )
                    Log.d("MainViewModel", "거래 횟수 조회 성공: $tradeCount")
                } else {
                    Log.e("MainViewModel", "거래 횟수 조회 실패: ${response.code()}")
                    _screenState.value = _screenState.value.copy(
                        error = "거래 횟수 조회 실패",
                        isLoading = false
                    )
                }
            }

            override fun onFailure(call: Call<tradeDataResponse>, t: Throwable) {
                Log.e("MainViewModel", "거래 횟수 조회 에러: ${t.message}")
                _screenState.value = _screenState.value.copy(
                    error = "네트워크 오류: ${t.message}",
                    isLoading = false
                )
            }
        })
    }

    private fun loadLevelExperience(accessToken: String) {
        mainService.levelexperience("Bearer $accessToken").enqueue(object : Callback<LevelExperience> {
            override fun onResponse(
                call: Call<LevelExperience>,
                response: Response<LevelExperience>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    _screenState.value = _screenState.value.copy(
                        myExperience = data?.myLevelExperience ?: 0,
                        levelExperience = data?.levelExperience ?: 1,
                        myLevel = data?.myLevel ?: 1,
                        isLoading = false
                    )
                    Log.d("MainViewModel", "레벨 정보 조회 성공: Level ${data?.myLevel}, Exp ${data?.myLevelExperience}/${data?.levelExperience}")
                } else {
                    Log.e("MainViewModel", "레벨 정보 조회 실패: ${response.code()}")
                    _screenState.value = _screenState.value.copy(
                        error = "레벨 정보 조회 실패",
                        isLoading = false
                    )
                }
            }

            override fun onFailure(call: Call<LevelExperience>, t: Throwable) {
                Log.e("MainViewModel", "레벨 정보 조회 에러: ${t.message}")
                _screenState.value = _screenState.value.copy(
                    error = "네트워크 오류: ${t.message}",
                    isLoading = false
                )
            }
        })
    }

    private fun loadLevelColor(accessToken: String) {
        mainService.getlevelcolor("Bearer $accessToken").enqueue(object : Callback<colorDataResponse> {
            override fun onResponse(
                call: Call<colorDataResponse>,
                response: Response<colorDataResponse>
            ) {
                if (response.isSuccessful) {
                    val color = response.body()?.data?.color ?: ""
                    _screenState.value = _screenState.value.copy(
                        levelColor = color,
                        isLoading = false
                    )
                    Log.d("MainViewModel", "레벨 색상 조회 성공: $color")
                } else {
                    Log.e("MainViewModel", "레벨 색상 조회 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<colorDataResponse>, t: Throwable) {
                Log.e("MainViewModel", "레벨 색상 조회 에러: ${t.message}")
            }
        })
    }
}

