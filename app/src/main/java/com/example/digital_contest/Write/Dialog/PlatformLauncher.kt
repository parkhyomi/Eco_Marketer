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
        val appIntent = createDeepLinkIntent(platform.deepLink)

        if (canResolveIntent(appIntent)) {
            // 앱이 설치되어 있는 경우
            Log.d(TAG, "${platform.name} 앱 실행")
            activityLauncher.launch(appIntent)
        } else {
            // 앱이 없는 경우 웹으로 이동
            Log.d(TAG, "${platform.name} 앱이 없어 웹으로 이동")
            val webIntent = createWebIntent(platform.webUrl)
            activityLauncher.launch(webIntent)
        }
    }

    /**
     * 딥링크 Intent 생성
     */
    private fun createDeepLinkIntent(deepLink: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
    }

    /**
     * 웹사이트 Intent 생성
     */
    private fun createWebIntent(url: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    /**
     * Intent를 처리할 수 있는 앱이 있는지 확인
     */
    private fun canResolveIntent(intent: Intent): Boolean {
        return intent.resolveActivity(context.packageManager) != null
    }
}

