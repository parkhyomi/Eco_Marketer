package com.example.digital_contest.Write

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class WriteRepository(
    private val writeService: WriteService,
) {

    suspend fun uploadProduct(
        token: String,
        imageUri: Uri,
        title: String,
        price: String,
        platform: String,
        category: String,
        concept: String
    ): Result<String> {
        return try {
            // 이미지 파일 준비
            val imageFile = File(imageUri.path ?: throw Exception("이미지 경로 오류"))
            if (!imageFile.exists()) throw Exception("이미지를 찾을 수 없습니다")

            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imageBody = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)

            // API 호출
            val response = writeService.uploadProduct(
                token = "Bearer $token",
                files = imageBody,
                introduceCategory = concept.toRequestBody("text/plain".toMediaTypeOrNull()),
                price = price.toRequestBody("text/plain".toMediaTypeOrNull()),
                product = title.toRequestBody("text/plain".toMediaTypeOrNull()),
                productCategory = category.toRequestBody("text/plain".toMediaTypeOrNull())
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data.introduceText)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "오류 발생"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addProductToOtherPlatforms(
        token: String,
        imageUri: Uri,
        title: String,
        price: String,
        concept: String,
        category: String,
        generatedText: String,
        selectedPlatforms: List<String>
    ): Result<Unit> {
        return try {
            // 이미지 파일 준비
            val imageFile = File(imageUri.path ?: throw Exception("이미지 경로 오류"))
            if (!imageFile.exists()) throw Exception("이미지를 찾을 수 없습니다")

            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imageBody = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)

            // 플랫폼 리스트 변환
            val companyParts = selectedPlatforms.map {
                MultipartBody.Part.createFormData("companys", it)
            }

            // API 호출
            val response = writeService.productplus(
                token = "Bearer $token",
                files = imageBody,
                introduceCategory = concept.toRequestBody("text/plain".toMediaTypeOrNull()),
                productCategory = category.toRequestBody("text/plain".toMediaTypeOrNull()),
                price = price.toRequestBody("text/plain".toMediaTypeOrNull()),
                product = title.toRequestBody("text/plain".toMediaTypeOrNull()),
                introduceText = generatedText.toRequestBody("text/plain".toMediaTypeOrNull()),
                companys = companyParts
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "오류 발생"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

