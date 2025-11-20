package com.example.digital_contest.Mypage

import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Query

data class ProductStatusRequest(
    val productId: Int,
    val status: Boolean
)

data class UserNicknameResponse(
    val nickname: String
)

@Serializable
data class Product(
    val productId: Int,
    val product: String,
    val productCategory: String,
    val imageUrl: String,
    val createdAt: String,
    val company: List<String>,
    val price: Int
)

data class ProductData(
    val productId: Int,
    val product: String,
    val productCategory: String,
    val imageUrl: String,
    val createdAt: String,
    val company: List<String>,
    val price: Int
) {
    companion object {
        fun fromProduct(product: Product): ProductData = ProductData(
            productId = product.productId,
            product = product.product,
            productCategory = product.productCategory,
            imageUrl = product.imageUrl,
            createdAt = product.createdAt,
            company = product.company,
            price = product.price
        )
    }
}

// 상품 상태
enum class ProductStatus {
    ON_SALE,
    COMPLETED,
    FAILED;

    fun toApiValue(): Boolean? = when (this) {
        ON_SALE -> null
        COMPLETED -> true
        FAILED -> false
    }
}

interface MyPageApiInterface {
    @PATCH("/api/product/status")
    fun updateProductStatus(
        @Header("Authorization") accessToken: String,
        @Body request: ProductStatusRequest
    ): Call<Void>

    @GET("/api/product")
    fun getProducts(
        @Header("Authorization") accessToken: String,
        @Query("status") status: Boolean?
    ): Call<Product>

    @GET("/api/user/nickname")
    fun getUserNickname(
        @Header("Authorization") accessToken: String
    ): Call<UserNicknameResponse>
}

