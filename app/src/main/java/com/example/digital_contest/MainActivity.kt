package com.example.digital_contest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        handleIntent(intent)

        setContent {
            AppNavigation()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            val action = it.action
            val data = it.data

            Log.d(TAG, "Intent received - Action: $action, Data: $data")

            // 딥링크로 돌아온 경우
            if (action == Intent.ACTION_VIEW && data != null) {
                Log.d(TAG, "딥링크로 복귀: ${data.scheme}://${data.host}")
            }
        }
    }
}