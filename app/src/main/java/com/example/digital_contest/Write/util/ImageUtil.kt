package com.example.digital_contest.Write.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

interface ImageCompressor {
    fun compress(context: Context, bitmap: Bitmap, maxSizeBytes: Int): File
}

interface PermissionChecker {
    fun hasPermission(context: Context, permission: String): Boolean
}

/**
 * Bitmap 압축
 */
class BitmapCompressor : ImageCompressor {
    companion object {
        private const val DEFAULT_QUALITY = 100  // 초기 압축 품질 (최고 품질)
        private const val QUALITY_STEP = 5       // 품질 감소 단계
        private const val MIN_QUALITY = 5        // 최소 압축 품질
    }

    /**
     * 비트맵을 지정된 크기 이하로 압축
     */
    override fun compress(context: Context, bitmap: Bitmap, maxSizeBytes: Int): File {
        val fileName = generateFileName()
        val file = File(context.cacheDir, fileName)

        // 파일 크기가 maxSizeBytes 이하가 될 때까지 품질을 낮춰가며 압축
        var quality = DEFAULT_QUALITY
        do {
            compressToFile(bitmap, file, quality)
            quality -= QUALITY_STEP
        } while (file.length() > maxSizeBytes && quality > MIN_QUALITY)

        return file
    }

    /** 타임스탬프 기반 파일명 생성 */
    private fun generateFileName(): String = "JPEG_${System.currentTimeMillis()}.jpg"

    /** 비트맵을 파일로 압축 저장 */
    private fun compressToFile(bitmap: Bitmap, file: File, quality: Int) {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
    }
}

/**
 * Android 권한 체크 구현체
 */
class AndroidPermissionChecker : PermissionChecker {
    override fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * 이미지 관련 권한 관리자
 */
class ImagePermissionManager(private val permissionChecker: PermissionChecker) {
    fun hasImagePermissions(context: Context): Boolean {
        val readGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionChecker.hasPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissionChecker.hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val cameraGranted = permissionChecker.hasPermission(context, Manifest.permission.CAMERA)

        return readGranted && cameraGranted
    }
}

/**
 * 카메라 촬영 핸들러
 */
class CameraLauncherHandler {
    @Composable
    fun rememberLauncher(onCameraCaptured: (Bitmap) -> Unit): ManagedActivityResultLauncher<Void?, Bitmap?> {
        return rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let(onCameraCaptured)
        }
    }
}

object ImageUtil {
    private val compressor: ImageCompressor = BitmapCompressor()
    private val permissionManager = ImagePermissionManager(AndroidPermissionChecker())
    private val cameraLauncherHandler = CameraLauncherHandler()

    /**
     * 비트맵을 파일로 압축 저장
     */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, maxSizeBytes: Int = 5 * 1024 * 1024): File {
        return compressor.compress(context, bitmap, maxSizeBytes)
    }

    /**
     * 이미지 관련 권한 확인 (읽기 권한 + 카메라 권한)
     */
    fun hasImagePermissions(context: Context): Boolean {
        return permissionManager.hasImagePermissions(context)
    }

    /**
     * 카메라 촬영 런처 생성
     */
    @Composable
    fun rememberCameraLauncher(onCameraCaptured: (Bitmap) -> Unit): ManagedActivityResultLauncher<Void?, Bitmap?> {
        return cameraLauncherHandler.rememberLauncher(onCameraCaptured)
    }
}

