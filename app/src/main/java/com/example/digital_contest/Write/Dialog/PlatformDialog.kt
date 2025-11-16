package com.example.digital_contest.Write.Dialog

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.digital_contest.R

@Composable
fun PlatformDialog(
    navController: NavController,
    dismiss: () -> Unit
) {
    val context = LocalContext.current
    val fontBold = Font(R.font.pretendard_bold)

    // Activity Result Launcher 설정
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                Log.d("PlatformDialog", "외부 앱에서 정상 복귀")
            }
            else -> {
                Log.d("PlatformDialog", "외부 앱에서 취소 또는 에러로 복귀")
            }
        }
        navController.navigate("main")
    }

    val platformLauncher = AndroidPlatformLauncher(context, launcher)

    Dialog(onDismissRequest = dismiss) {
        Surface(
            modifier = Modifier
                .height(290.dp)
                .width(312.dp),
            color = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 다이얼로그 아이콘
                Image(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .size(36.dp),
                    painter = painterResource(R.drawable.dialog),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.padding(4.dp))

                // 제목
                Text(
                    text = "어디에 글을 올려볼까요?",
                    fontFamily = FontFamily(fontBold),
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.padding(4.dp))

                PlatformConfig.getAllPlatforms().forEach { platform ->
                    PlatformButton(
                        platform = platform,
                        onClick = {
                            platformLauncher.launch(platform)
                            dismiss()
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}