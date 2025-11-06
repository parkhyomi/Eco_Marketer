package com.example.digital_contest.Viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.digital_contest.API.Manager.MyPageManager
import com.example.digital_contest.API.MyPageProduct
import com.example.digital_contest.API.ProductStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPageViewModel(private val context: Context): ViewModel() {
    //판매중.판매완료.판매실패 받아오는 부분.
    private val myPageManager = MyPageManager(context)

    private val _onSaleProducts = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val onSaleProducts: StateFlow<List<Map<String, Any>>> = _onSaleProducts  //판매중 저장

    private val _completedProducts = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val completedProducts: StateFlow<List<Map<String, Any>>> = _completedProducts  //판매완료

    private val _failedProducts = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val failedProducts: StateFlow<List<Map<String, Any>>> = _failedProducts  //판매실패.

    private val _onSaleproductCount = MutableStateFlow(0)
    val onSaleproductCount: StateFlow<Int> = _onSaleproductCount  //이부분들은 이제 갯수를 가지는 부분.

    private val _completedproductCount = MutableStateFlow(0)
    val completedproductCount: StateFlow<Int> = _completedproductCount

    private val _failedproductCount = MutableStateFlow(0)
    val failedproductCount: StateFlow<Int> = _failedproductCount


    fun loadAllProducts() {  //

        Log.d("MyPageViewModel", "Loading all products...")
        viewModelScope.launch {
            val states = listOf(null, true, false)
            states.fastForEachIndexed { index, state ->
                try {
                    MyPageProduct(context, state)
                    delay(300) // API 호출 및 데이터 저장을 위한 대기 시간
                    val products = myPageManager.getProductData(state)
                    when (index) {
                        0 -> {
                            _onSaleProducts.value = products
                            _onSaleproductCount.value = products.size
                            Log.d("판매중", "${_onSaleProducts.value}")
                        }

                        1 -> {
                            _completedProducts.value = products
                            _completedproductCount.value = products.size
                            Log.d("판매완료", "${_completedProducts.value}")
                        }

                        2 -> {
                            _failedProducts.value = products
                            _failedproductCount.value = products.size
                            Log.d("판매실패", "${_failedProducts.value}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error loading products for state: $state", e)
                }

            }

        }
    }


    fun loadOnSaleProducts() {
        Log.d("MyPageViewModel", "Loading onSale products...")
        viewModelScope.launch {
            try {
                MyPageProduct(context, null)  // null은 onSale 상태를 나타냅니다
                delay(100) // API 호출 및 데이터 저장을 위한 대기 시간
                val products = myPageManager.getProductData(null)
                _onSaleProducts.value = products
                _onSaleproductCount.value = products.size
                Log.d("판매중", "${_onSaleProducts.value}")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading onSale products", e)
            }
        }
    }

    fun updateProductStatus(productId: Int, newStatus: Boolean) {
        viewModelScope.launch {
            ProductStatus(context, productId, newStatus)
            updateLocalProduct(productId, newStatus)
            if(newStatus==true){
                sortCompletedProductsByIdAscending()
            }else if(newStatus==false){
                sortFailedProductsByIdAscending()
            }
        }
    }

    fun updateLocalProduct(productId: Int, newStatus: Boolean) {
        val updateList = { list: List<Map<String, Any>> ->
            list.map { product ->
                if (product["productId"] as Int == productId) {
                    product + ("status" to newStatus)
                } else {
                    product
                }
            }
        }

        _onSaleProducts.update { updateList(it) }
        _completedProducts.update { updateList(it) }
        _failedProducts.update { updateList(it) }
        when (newStatus) {
            true -> {
                _completedProducts.update {
                    it + (_onSaleProducts.value.find { it["productId"] as Int == productId }
                        ?: emptyMap())
                }
                _onSaleProducts.update { it.filter { it["productId"] as Int != productId } }
                _failedProducts.update { it.filter { it["productId"] as Int != productId } }
            }

            false -> {
                _failedProducts.update {
                    it + (_onSaleProducts.value.find { it["productId"] as Int == productId }
                        ?: emptyMap())
                }
                _onSaleProducts.update { it.filter { it["productId"] as Int != productId } }
                _completedProducts.update { it.filter { it["productId"] as Int != productId } }
            }
        }

        // 카운트 업데이트
        updateProductCounts()
    }

    private fun updateProductCounts() {
        _onSaleproductCount.value = _onSaleProducts.value.size
        _completedproductCount.value = _completedProducts.value.size
        _failedproductCount.value = _failedProducts.value.size
    }

    fun sortProductsByIdAscending(){
        sortOnSaleProductsByIdAscending()
        sortCompletedProductsByIdAscending()
        sortFailedProductsByIdAscending()
    }

    fun sortOnSaleProductsByIdAscending() {
        viewModelScope.launch {
            val sortedProducts = _onSaleProducts.value.sortedBy { it["productId"] as Int }
            _onSaleProducts.value = sortedProducts
            Log.d("판매중정렬확인","${_onSaleProducts.value}")
        }
    }

   fun sortCompletedProductsByIdAscending() {
        viewModelScope.launch {
            val sortedProducts = _completedProducts.value.sortedBy { it["productId"] as Int }
            _completedProducts.value = sortedProducts
            Log.d("판매완료정렬확인","${_completedProducts.value }")
        }
    }

   fun sortFailedProductsByIdAscending() {
        viewModelScope.launch {
            val sortedProducts = _failedProducts.value.sortedBy { it["productId"] as Int }
            _failedProducts.value = sortedProducts
            Log.d("판매실패정렬확인","${_failedProducts.value}")
        }
   }
    fun loadAndSortAllProducts() {
        viewModelScope.launch {
            loadAllProducts()
            delay(1600) // 데이터 로딩을 위한 대기 시간
            sortProductsByIdAscending()
        }
    }
}

class MyPageViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}