package com.example.digital_contest.Write.Dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher

interface PlatformLauncher {
    fun launch(platform: Platform)
}

class AndroidPlatformLauncher(
    private val context: Context,
    private val activityLauncher: ActivityResultLauncher<Intent>
) : PlatformLauncher {

    companion object {
        private const val TAG = "PlatformLauncher"
    }

    override fun launch(platform: Platform) {
        Log.d(TAG, "플랫폼 실행 시도: ${platform.name}")
        Log.d(TAG, "  - 딥링크: ${platform.deepLink}")
        Log.d(TAG, "  - 패키지명: ${platform.packageName}")

        // 1. 먼저 패키지명으로 앱 실행 시도
        val packageIntent = createPackageLaunchIntent(platform.packageName)
        if (packageIntent != null) {
            Log.d(TAG, "${platform.name} 앱을 패키지명으로 실행")
            try {
                activityLauncher.launch(packageIntent)
                return
            } catch (e: Exception) {
                Log.e(TAG, "패키지명으로 실행 실패: ${e.message}", e)
            }
        }

        // 2. 딥링크로 앱 실행 시도
        val appIntent = createDeepLinkIntent(platform.deepLink)
        if (canResolveIntent(appIntent)) {
            Log.d(TAG, "${platform.name} 앱을 딥링크로 실행")
            try {
                activityLauncher.launch(appIntent)
                return
            } catch (e: Exception) {
                Log.e(TAG, "딥링크로 실행 실패: ${e.message}", e)
            }
        }

        // 3. 앱이 없거나 실패한 경우 웹으로 이동
        Log.d(TAG, "${platform.name} 앱이 없어 웹으로 이동")
        val webIntent = createWebIntent(platform.webUrl)
        activityLauncher.launch(webIntent)
    }

    /**
     * 패키지명으로 앱 실행 Intent 생성
     */
    private fun createPackageLaunchIntent(packageName: String): Intent? {
        return try {
            context.packageManager.getLaunchIntentForPackage(packageName)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        } catch (e: Exception) {
            Log.e(TAG, "패키지 Intent 생성 실패: ${e.message}")
            null
        }
    }

    /**
     * 딥링크 Intent 생성
     */
    private fun createDeepLinkIntent(deepLink: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(deepLink)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
    }

    /**
     * 웹사이트 Intent 생성
     */
    private fun createWebIntent(url: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    /**
     * Intent를 처리할 수 있는 앱이 있는지 확인
     */
    private fun canResolveIntent(intent: Intent): Boolean {
        return intent.resolveActivity(context.packageManager) != null
    }
}

