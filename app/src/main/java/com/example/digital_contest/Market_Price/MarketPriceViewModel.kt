package com.example.digital_contest.Market_Price

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.digital_contest.API.APIRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class MarketPriceState(
    val products: List<MarketPriceData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class MarketPriceViewModel : ViewModel() {
    private val _state = mutableStateOf(MarketPriceState())
    val state: State<MarketPriceState> = _state

    private val marketPriceService: MarketPriceService = APIRetrofit.createService()

    fun searchMarketPrices(accessToken: String, query: String) {
        if (query.isBlank()) {
            _state.value = _state.value.copy(
                products = emptyList(),
                searchQuery = query,
                error = null
            )
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            searchQuery = query,
            error = null
        )

        marketPriceService.getMarketPrices(
            "Bearer $accessToken",
            query
        ).enqueue(object : Callback<MarketPriceResponse> {
            override fun onResponse(
                call: Call<MarketPriceResponse>,
                response: Response<MarketPriceResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    _state.value = _state.value.copy(
                        products = data,
                        isLoading = false,
                        error = null
                    )
                    Log.d("MarketPriceViewModel", "시세 조회 성공: ${data.size}개 상품")
                } else {
                    Log.e("MarketPriceViewModel", "시세 조회 실패: ${response.code()}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "시세 조회에 실패했습니다"
                    )
                }
            }

            override fun onFailure(call: Call<MarketPriceResponse>, t: Throwable) {
                Log.e("MarketPriceViewModel", "시세 조회 에러: ${t.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "네트워크 오류: ${t.message}"
                )
            }
        })
    }
}


