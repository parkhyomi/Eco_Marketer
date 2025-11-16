package com.example.digital_contest.Write

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.digital_contest.R
import com.example.digital_contest.ui.theme.pretendard

@Composable
fun TopBar(
    title: String,
    onNavClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onNavClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.x_icon),
                contentDescription = "취소 아이콘",
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.Black
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
    }
    Divider(color = Color(0xFFD9D9D9), thickness = 1.dp)
}


// 텍스트 라벨
@Composable
fun LabelText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        modifier = modifier,
    )
}

// 사진 선택 박스
@Composable
fun PhotoPickerBox(
    imageUri: Uri?,
    onPickRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(1.dp, Color(217, 217, 217), RoundedCornerShape(6.dp))
            .size(64.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF6F6F6))
            .clickable { onPickRequest() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.camera_icon),
                    contentDescription = "사진 선택",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


// 커스텀 텍스트 필드
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
) {
    Box(
        modifier = modifier
            .border(1.dp, Color(217, 217, 217), RoundedCornerShape(6.dp))
            .height(45.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            textStyle = TextStyle(
                fontFamily = pretendard,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF999999),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

// 플랫폼, 카테고리, 컨셉 선택 박스
@Composable
fun SelectBox(
    label: String,
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Column {
        LabelText(label)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(45.dp)
                .border(1.dp, Color(217, 217, 217), RoundedCornerShape(6.dp))
                .clickable { onClick() }
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = if (value.isNotEmpty()) value else placeholder,
                color = if (value.isNotEmpty()) Color.Black else Color(0xFF999999),
                fontSize = 16.sp,
                lineHeight = 24.sp,
            )
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .size(18.dp),
                tint = Color(0xFF999999)
            )
        }
    }
}

// 생성된 글 박스
@Composable
fun GeneratedTextBox(
    text: String,
    onCopyClick: () -> Unit
) {
    Row {
        Text(
            text = "생성된 글",
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = Color(0xFF14AE5C)
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.copy),
            contentDescription = null,
            modifier = Modifier.size(24.dp).clickable { onCopyClick() }
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF14AE5C), RoundedCornerShape(6.dp))
            .background(Color(0xFFE9F4EE), RoundedCornerShape(6.dp))
            .padding(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}

// 액션 버튼들
@Composable
fun ActionButtons(
    isGenerated: Boolean,
    onGenerateClick: () -> Unit,
    onRegenerateClick: () -> Unit,
    onGoWriteClick: () -> Unit
) {
    if (!isGenerated) {
        Button(
            onClick = onGenerateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(Color(20, 174, 92)),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "💫생성하기",
                fontSize = 17.sp,
                lineHeight = 22.sp
            )
        }
    } else {
        Row {
            Button(
                onClick = onRegenerateClick,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                border = BorderStroke(1.dp, Color(20, 174, 92)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "다시 생성하기",
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    color = Color(20, 174, 92)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onGoWriteClick,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color(20, 174, 92)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "글쓰러 가기",
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    color = Color.White
                )
            }
        }
    }
}

// 이미지 소스 선택 BottomSheet (갤러리/카메라)
@Composable
fun ImageSourceSheet(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 갤러리 버튼
        Button(
            onClick = onGalleryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(1.dp, Color(217, 217, 217), RoundedCornerShape(6.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "📷 갤러리에서 선택",
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 카메라 버튼
        Button(
            onClick = onCameraClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(1.dp, Color(217, 217, 217), RoundedCornerShape(6.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "📸 카메라로 촬영",
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            )
        }
    }
}
