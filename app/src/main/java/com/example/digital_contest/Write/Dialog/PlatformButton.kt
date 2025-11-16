package com.example.digital_contest.Write.Dialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_contest.R

@Composable
fun PlatformButton(
    platform: Platform,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fontBold = Font(R.font.pretendard_bold)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.White),
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(6.dp)
            )
            .size(280.dp, 48.dp),
    ) {
        Text(
            text = platform.displayText,
            fontFamily = FontFamily(fontBold),
            fontSize = 20.sp,
            lineHeight = 30.sp,
            color = platform.buttonColor,
        )
    }
}

