package com.example.digital_contest.Write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import com.example.digital_contest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteView() {
    //임의 처리
    val showPlatformSheet = remember { mutableStateOf(false) }
    val showCategorySheet = remember { mutableStateOf(false) }
    val showConceptSheet = remember { mutableStateOf(false) }
    val selectedPlatform = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf("") }
    val selectedConcept = remember { mutableStateOf("") }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                title = "게시글 생성하기",
                onNavClick = {
                    // navHostController.navigate("main") {popUpTo("write") {inclusive = true}}
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LabelText("사진 가져오기")
                    Spacer(modifier = Modifier.height(8.dp))
                    PhotoPickerBox(
                        imageUri = null, // 실제 이미지 Uri State
                        onClick = {
                            // showImagePickerDialog.value = true
                        }
                    )

                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LabelText("제목")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = "",                // title state
                        onValueChange = { /* title change */ },
                        placeholder = "제목",
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LabelText("가격")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = "",                // price state
                        onValueChange = { /* price change */ },
                        placeholder = "가격",
                        keyboardType = KeyboardType.Number,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectBox(
                        label = "플랫폼 선택",
                        value = selectedPlatform.value,
                        placeholder = "플랫폼을 선택해주세요",
                        onClick = { showPlatformSheet.value = true },
                        iconRes = R.drawable.chevron_down_1
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectBox(
                        label = "카테고리",
                        value = selectedCategory.value,
                        placeholder = "카테고리를 선택해주세요",
                        onClick = { showCategorySheet.value = true },
                        iconRes = R.drawable.chevron_down_1
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectBox(
                        label = "판매 컨셉",
                        value = selectedConcept.value,
                        placeholder = "판매 컨셉을 선택해주세요",
                        onClick = { showConceptSheet.value = true },
                        iconRes = R.drawable.chevron_down_1
                    )
                }
                // 생성된 글
                item {
                    // 통신 중 로딩중일 때 -> 다이어로그
                    // 통신 완료 -> 생성된 글 보여주기
                    Spacer(modifier = Modifier.height(16.dp))
                    GeneratedTextBox(
                        text = "생성된 글입니다", // 실제 generatedText state
                        onCopyClick = { }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 버튼
                item {
                    // 생성 전/후 상태에 따라 버튼 변경
                    // API 연결 후 inGenerated 판별
                    ActionButtons(
                        isGenerated = false, // 실제 state
                        onGenerateClick = { /* 생성하기 */ },
                        onRegenerateClick = { /* 다시 생성하기 */ },
                        onGoWriteClick = { /* 글쓰러 가기 */ }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // 바텀시트들 - Column 밖에 배치
    if (showPlatformSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showPlatformSheet.value = false },
            containerColor = Color.White,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            PlatformSheet(
                onItemClick = {
                    selectedPlatform.value = it
                    showPlatformSheet.value = false
                }
            )
        }
    }

    if (showCategorySheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet.value = false },
            containerColor = Color.White,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            CategorySheet(
                onItemClick = {
                    selectedCategory.value = it
                    showCategorySheet.value = false
                }
            )
        }
    }

    if (showConceptSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showConceptSheet.value = false },
            containerColor = Color.White,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            SaleConceptSheet(
                onItemClick = {
                    selectedConcept.value = it
                    showConceptSheet.value = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WriteViewPreview() {
    WriteView()
}