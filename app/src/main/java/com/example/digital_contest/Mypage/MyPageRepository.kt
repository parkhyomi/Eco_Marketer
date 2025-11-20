package com.example.digital_contest.Mypage

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.digital_contest.API.APIRetrofit
import com.example.digital_contest.Login.AuthDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.awaitResponse


interface MyPageRepository {
    suspend fun getProductsByStatus(status: ProductStatus): List<ProductData>
    suspend fun updateProductStatus(productId: Int, isCompleted: Boolean)
    suspend fun getUserNickname(): String
    suspend fun saveUserNickname(nickname: String)
    suspend fun getLevelExperience(): Int
}

class MyPageRepositoryImpl(
    private val context: Context,
    private val authDataStore: AuthDataStore
) : MyPageRepository {

    private val apiService: MyPageApiInterface = APIRetrofit.createService()
    private val json = Json { ignoreUnknownKeys = true }

    // DataStore 접근
    private val Context.productDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_product_manager")
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "name_data")

    companion object {
        private val NICKNAME_KEY = stringPreferencesKey("nickname")
    }

    // 상품 관련
    override suspend fun getProductsByStatus(status: ProductStatus): List<ProductData> {
        return try {
            val token = authDataStore.accessToken.first() ?: throw Exception("No access token")
            val response = apiService.getProducts("Bearer $token", status.toApiValue()).awaitResponse()

            if (response.isSuccessful) {
                val products = response.body()?.let { listOf(it) } ?: emptyList()
                saveProductsToLocal(products, status)
                products.map { ProductData.fromProduct(it) }
            } else {
                Log.e("Repository", "API failed, loading from cache")
                getProductsFromLocal(status)
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error: ${e.message}, loading from cache")
            getProductsFromLocal(status)
        }
    }

    override suspend fun updateProductStatus(productId: Int, isCompleted: Boolean) {
        val token = authDataStore.accessToken.first() ?: throw Exception("No access token")
        val request = ProductStatusRequest(productId, isCompleted)
        val response = apiService.updateProductStatus("Bearer $token", request).awaitResponse()

        if (!response.isSuccessful) {
            throw Exception("Failed to update product status: ${response.code()}")
        }
    }

    // 사용자 관련
    override suspend fun getUserNickname(): String {
        return try {
            val token = authDataStore.accessToken.first() ?: throw Exception("No access token")
            val response = apiService.getUserNickname("Bearer $token").awaitResponse()

            if (response.isSuccessful) {
                val nickname = response.body()?.nickname ?: "사용자"
                saveUserNickname(nickname)
                nickname
            } else {
                getNicknameFromLocal()
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching nickname: ${e.message}")
            getNicknameFromLocal()
        }
    }

    override suspend fun saveUserNickname(nickname: String) {
        context.userDataStore.edit { preferences ->
            preferences[NICKNAME_KEY] = nickname
        }
    }

    override suspend fun getLevelExperience(): Int {
        return 0
    }


    private suspend fun saveProductsToLocal(products: List<Product>, status: ProductStatus) {
        withContext(Dispatchers.IO) {
            context.productDataStore.edit { preferences ->
                val stateKey = getStateKey(status)
                val productStrings = products.map { json.encodeToString(it) }.toSet()
                preferences[stringSetPreferencesKey("${stateKey}_products")] = productStrings
            }
        }
    }

    private suspend fun getProductsFromLocal(status: ProductStatus): List<ProductData> {
        return context.productDataStore.data.map { preferences ->
            val stateKey = getStateKey(status)
            val productStrings = preferences[stringSetPreferencesKey("${stateKey}_products")] ?: emptySet()
            productStrings.mapNotNull { productString ->
                try {
                    json.decodeFromString<Product>(productString).let { ProductData.fromProduct(it) }
                } catch (e: Exception) {
                    null
                }
            }
        }.first()
    }

    private suspend fun getNicknameFromLocal(): String {
        return context.userDataStore.data.map { preferences ->
            preferences[NICKNAME_KEY] ?: "사용자"
        }.first()
    }

    private fun getStateKey(status: ProductStatus): String = when (status) {
        ProductStatus.ON_SALE -> "onSale"
        ProductStatus.COMPLETED -> "completed"
        ProductStatus.FAILED -> "failed"
    }
}

