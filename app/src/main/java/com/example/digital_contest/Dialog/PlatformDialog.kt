package com.example.digital_contest.Dialog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun PlatformDialog(navController:NavController,dismiss:()->Unit) {
    val context = LocalContext.current

    val fontBold = Font(R.font.pretendard_bold)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 딥링크에서 돌아왔을 때의 처리
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("PlatformDialog", "Returned from external app.")
        } else {
            Log.d("PlatformDialog", "Returned cancelled or with an error.")
        }
        navController.navigate("main")
    }
    Dialog(onDismissRequest = dismiss) {
        Surface (
            modifier = Modifier
                .height(290.dp)
                .width(312.dp),
            color = Color.White,
            shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .size(36.dp),
                    painter = painterResource(R.drawable.dialog),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = "어디에 글을 올려볼까요?",
                    fontFamily = FontFamily(fontBold),
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        val app = "jnapps3://?applink=main"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app))
                        if (intent.resolveActivity(context.packageManager) != null) {
                            // context.startActivity(intent)
                            // navController.navigate("main")
                            launcher.launch(intent)
                        } else {
                            // 앱이 설치되지 않은 경우 당근마켓 웹페이지로 이동
                            val url = "https://web.joongna.com"
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            //context.startActivity(webIntent)
                            // navController.navigate("main")
                            launcher.launch(webIntent)
                        }
                        dismiss()
                        //navController.navigate("main")
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier
                        .border(
                            1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .size(280.dp, 48.dp),
                ) {
                    Text(
                        text = "중고나라 글쓰러가기",
                        fontFamily = FontFamily(fontBold),
                        fontSize = 20.sp,
                        lineHeight = 30.sp,
                        color = Color(0xFF14AE5C),
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        val app = "karrot://"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app))
                        if (intent.resolveActivity(context.packageManager) != null) {
                            // context.startActivity(intent)
                            // navController.navigate("main")
                            launcher.launch(intent)
                        } else {
                            // 앱이 설치되지 않은 경우 당근마켓 웹페이지로 이동
                            val url = "https://www.daangn.com/"
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            //context.startActivity(webIntent)
                            // navController.navigate("main")
                            launcher.launch(webIntent)
                    }
                        dismiss()
                        //navController.navigate("main")
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier
                        .border(
                            1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .size(280.dp, 48.dp)
                ) {
                    Text(
                        text = "당근 글쓰러가기",
                        fontFamily = FontFamily(fontBold),
                        fontSize = 20.sp,
                        lineHeight = 30.sp,
                        color = Color(0xFFFF8329),
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        val app = "bunjang:/"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app))

                        if (intent.resolveActivity(context.packageManager) != null) {
                            // context.startActivity(intent)
                            //  navController.navigate("main")
                            launcher.launch(intent)
                        } else {
                            // 앱이 설치되지 않은 경우 웹페이지로 이동
                            val url = "https://m.bunjang.co.kr"
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            //context.startActivity(webIntent)
                            // navController.navigate("main")
                            launcher.launch(webIntent)
                        }
                        dismiss()
                        // navController.navigate("main")
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier
                        .border(
                            1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .size(280.dp, 48.dp)

                ) {
                    Text(
                        text = "번개장터 글쓰러가기",
                        fontFamily = FontFamily(fontBold),
                        fontSize = 20.sp,
                        lineHeight = 30.sp,
                        color = Color(0xFFFF0000),
                    )
                }
            }
        }
    }
}