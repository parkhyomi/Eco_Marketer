package com.example.digital_contest.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.digital_contest.R


@Composable
fun LoginView() {
    LoginContent(
        onKakaoLoginClick = { }
    )
}

@Composable
private fun LoginContent(onKakaoLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // 로고
        LoginLogo()

        Spacer(modifier = Modifier.height(54.dp))

        // 도움말 텍스트
        LoginHelpTexts()

        Spacer(modifier = Modifier.height(257.dp))

        // 카카오 로그인 버튼
        KakaoLoginButton(onClick = onKakaoLoginClick)
    }
}

@Composable
private fun LoginLogo() {
    Image(
        painter = painterResource(id = R.drawable.login_logo),
        contentDescription = "앱 로고"
    )
}

@Composable
private fun LoginHelpTexts() {
    Text(
        text = stringResource(id = R.string.login_help_1),
        fontFamily = FontFamily(Font(R.font.pretendard_medium))
    )
    Text(
        text = stringResource(id = R.string.login_help_2),
        fontFamily = FontFamily(Font(R.font.pretendard_medium))
    )
}

@Composable
private fun KakaoLoginButton(onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.login_help_with_kakao),
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            color = Color(0xFF0F7E00),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Spacer(modifier = Modifier.height(49.dp))

        Image(
            painter = painterResource(id = R.drawable.kakao_login),
            contentDescription = "카카오 로그인",
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    LoginContent(onKakaoLoginClick = {})
}

