package com.example.digital_contest.API.Manager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.digital_contest.API.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MyPageManager(private val context: Context) {  //마이페이지 품목들에 대한 데이터스토어
    companion object{
        private val Context.datastore:DataStore<Preferences> by preferencesDataStore(name="My_product_manager")  //데이터스토어 context추가
    }

    suspend fun saveAllProductindex(products: List<Product>, state: Boolean?) {  //api에서 불러온 데이터 저장.
        withContext(Dispatchers.IO) {
            context.datastore.edit { preferences ->
                val statekey = when (state) {
                    null -> "onSale"
                    true -> "completed"
                    false -> "failed"
                }
                // 현재 상태의 데이터만 삭제
                preferences.remove(stringSetPreferencesKey("${statekey}_products"))  //한 상태에 해당하는 부분을 싹 지워버리고
                val Json=Json{ignoreUnknownKeys=true}   //json형태로 받아버림.
                val productString = products.map { product ->  //api에 있는 데이터를 map형태로 저장.
                    Json.encodeToString(product)

                }.toSet()
                preferences[stringSetPreferencesKey("${statekey}_products")] = productString  //해당하는 데이터들을 한 상태에 저장.
            }
            Log.d("마이페이지 저장", "${products.size},${state}")//어떤상태의 데이터가 저장되었는 확인.
        }
    }

    suspend fun getProductData(state: Boolean?): List<Map<String, Any>> {  //정보 불러오기.
        return context.datastore.data.map { preferences ->
            val stateKey = when (state) {  //여기서 판매중,판매완료,판매종료 부분.
                null -> "onSale"
                true -> "completed"
                false -> "failed"
            }
            //해당 상태에 해당하는 데이터 불러오기
            val productStrings = preferences[stringSetPreferencesKey("${stateKey}_products")] ?: emptySet()
            productStrings.map { productString -> //리스트형태로 불러오기.
                val product = Json.decodeFromString<Product>(productString)
                mapOf(
                    "productId" to product.productId,
                    "product" to product.product,
                    "productCategory" to product.productCategory,
                    "imageUrl" to product.imageUrl,
                    "createdAt" to product.createdAt,
                    "company" to product.company,
                    "price" to product.price
                )
            }
        }.first().also { products ->
            Log.d("getProductData", "Retrieved ${products.size} products for state: $state")
        }
    }
}

