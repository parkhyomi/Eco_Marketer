//package com.example.digital_contest.Dialog
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import com.example.digital_contest.R
//import com.example.digital_contest.ui.theme.Digital_ContestTheme
//
//@Composable
//fun LodingDialog(){
//
//    val fontBold = Font(R.font.pretendard_bold)
//    val fontMedium = Font(R.font.pretendard_medium)
//
//    Dialog(onDismissRequest ={}) {
//        Surface (
//            modifier = Modifier
//                .height(182.dp)
//                .width(312.dp),
//            shape = RoundedCornerShape(16.dp)){
//                Column(
//                    modifier = Modifier
//                        .padding(vertical = 24.dp)
//                        .fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        modifier = Modifier
//                            .fillMaxWidth(1f)
//                            .size(36.dp),
//                        painter = painterResource(R.drawable.dialog),
//                        contentDescription = null
//                    )
//                    Spacer(modifier = Modifier.padding(4.dp))
//
//                    Text(
//                        text = "AI가",
//                        fontFamily = FontFamily(fontBold),
//                        fontSize = 20.sp,
//                        lineHeight = 30.sp,
//                        color = Color.Black
//                    )
//
//                    Row() {
//                        Text(
//                            text = "게시글을 생성",
//                            fontFamily = FontFamily(fontBold),
//                            fontSize = 20.sp,
//                            lineHeight = 30.sp,
//                            color = Color(0xFF14AE5C),
//                        )
//                        Text(
//                            text = "하고 있어요!",
//                            fontFamily = FontFamily(fontBold),
//                            fontSize = 20.sp,
//                            lineHeight = 30.sp,
//                            color = Color.Black
//                        )
//                    }
//                    Spacer(modifier = Modifier.padding(4.dp))
//
//                    Text(
//                        text = "잠시만 기다려 주세요!",
//                        fontFamily = FontFamily(fontMedium),
//                        fontSize = 14.sp,
//                        lineHeight = 22.sp,
//                        color = Color.Black
//                    )
//                }
//            }
//        }
//    }
//
//@Preview
//@Composable
//fun LodingDialogPreview() {
//    Digital_ContestTheme {
//        LodingDialog()
//    }
//}