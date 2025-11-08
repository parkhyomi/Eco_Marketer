package com.example.digital_contest.Write


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.digital_contest.API.Intro.IntroduceCreate
import com.example.digital_contest.API.Intro.IntroduceDataStore
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.google.accompanist.permissions.isGranted
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.digital_contest.API.Intro.IntroDuceplus
import com.example.digital_contest.Viewmodel.MyPageViewModel
import com.example.digital_contest.Dialog.LodingDialog
import com.example.digital_contest.Dialog.PlatformDialog
import com.example.digital_contest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

fun Context.uriToFile(uri: Uri): File? {
    val fileName = getFileName(uri)
    val file = File(externalCacheDir, fileName)
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val outputStream = FileOutputStream(file)
    inputStream.copyTo(outputStream)
    inputStream.close()
    outputStream.close()
    return file
}

fun Context.getFileName(uri: Uri): String {
    var fileName = ""
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }
    return fileName
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
    val fileName = "JPEG_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, fileName)

    Log.d("압축 전 크기", "압축 전 비트맵 크기: ${bitmap.byteCount} bytes")

    val compressedFile = compressImage(bitmap, file, 20 * 1024 * 1024) // 20MB

    Log.d("최종 파일 크기", "최종 파일 크기: ${compressedFile.length()} bytes")

    return compressedFile
}

fun compressImage(bitmap: Bitmap, file: File, maxSizeBytes: Int): File {
    var quality = 100
    var fileSize = maxSizeBytes + 1 // 초기 파일 크기를 최대 크기보다 크게 설정

    Log.d("원본 이미지 크기", "원본 이미지 크기: ${bitmap.byteCount} bytes")
    Log.d("목표 최대 크기", "목표 최대 크기: $maxSizeBytes bytes")

    var compressionCount = 0
    while (fileSize > maxSizeBytes && quality > 0) {
        file.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        fileSize = file.length().toInt()
        Log.d("압축", "압축 시도 $compressionCount - 품질: $quality%, 크기: $fileSize bytes")
        quality -= 5 // 품질을 5씩 감소
        compressionCount++
    }

    Log.d("결과", "최종 압축 결과 - 품질: $quality%, 크기: $fileSize bytes")
    Log.d("압축 횟수", "압축 횟수: $compressionCount")

    if (fileSize <= maxSizeBytes) {
        Log.d("성공", "압축 성공: 파일 크기가 목표 이하입니다.")
    } else {
        Log.d("실패", "압축 실패: 최소 품질에도 목표 크기를 달성하지 못했습니다.")
    }

    return file
}

fun formatNumberWithCommas(number: String): String {
    return number.replace(",", "").reversed().chunked(3).joinToString(",").reversed()
}

fun removeCommas(number: String): String {
    return number.replace(",", "")
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("Copy Text", text))
    Toast.makeText(context, "텍스트가 복사되었습니다.", Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun WriteView(navHostController: NavHostController,viewModel: MyPageViewModel) {

    // 화면 크기 가져오기
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var selectedCategory = remember { mutableStateOf("") }
    var selectedConcept = remember { mutableStateOf("") }
    var selectedPlatform = remember { mutableStateOf("") }
    var imageFile = remember { mutableStateOf<File?>(null) }
    var title = remember { mutableStateOf("") }
    val titleInteractionSource = remember { MutableInteractionSource() }
    val priceInteractionSource = remember { MutableInteractionSource() }
    val isTitleFocused = titleInteractionSource.collectIsFocusedAsState()
    val isPriceFocused = priceInteractionSource.collectIsFocusedAsState()
    var price = remember { mutableStateOf("") }
    val selectedColor = remember { mutableStateOf(Color(0xFF999999)) }


    var imageUri = remember { mutableStateOf<Uri?>(null) }
    var showImagePickerDialog = remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)


    val galleryPermissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val file = saveBitmapToFile(context, it)
            imageFile.value = file
            imageUri.value = Uri.fromFile(file)
        }
    }



    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val file = saveBitmapToFile(context, bitmap)
            imageFile.value = file
            imageUri.value = Uri.fromFile(file)
        }
    }

    val fontBold = Font(R.font.pretendard_bold)
    val fontMedium = Font(R.font.pretendard_medium)
    val fontSemiBold = Font(R.font.pretendard_semibold)

    @OptIn(ExperimentalMaterialApi::class)
    val categorySheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    @OptIn(ExperimentalMaterialApi::class)
    val conceptSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    @OptIn(ExperimentalMaterialApi::class)
    val platformSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )


    var ad = remember { mutableStateOf<String?>(null) }

    suspend fun someFunction() {
        val priceCount = removeCommas(price.value).toInt()

        ad.value = when (selectedCategory.value) {
            "디지털 기기" -> "디지털기기"
            "생활 가전" -> "생활가전"
            "여성 패션/잡화" -> "여성패션/잡화"
            "남성 패션/잡화" -> "남성패션/잡화"
            "티켓/교환권" -> "티켓/교환권"
            "생활 주방" -> "생활주방"
            "스포츠/레저" -> "스포츠/레저"
            "취미/게임/음악" -> "취미/게임/음악"
            "뷰티/미용" -> "뷰티/미용"
            "식물" -> "식물"
            "가공식품" -> "가공식품"
            "건강기능식품" -> "건강기능식품"
            "반려동물" -> "반려동물"
            "도서" -> "도서"
            "기타 중고" -> "기타중고"
            else -> null
        }


        if (ad.value != null) {
            imageFile.value?.let { file ->
                IntroduceCreate(
                    context,
                    file,
                    selectedConcept.value,
                    priceCount,
                    title.value,
                    ad.value!!
                )
            }
                ?: run {
                    Log.d("ImageUpload", "No image file selected")
                }
        }
    }


    val isLoading = remember { mutableStateOf(false) }
    val isplafome = remember { mutableStateOf(false) }
    val introduceManager = IntroduceDataStore(context)
    //val introduceTextState = introduceManager.getIntroduceText()
    val generatedText = remember { mutableStateOf<String?>(null) }
    val WriteServiceManager = MyPageViewModel(context)
    // 필요한 데이터 준비
    val token = "your_token_here" // 실제 토큰을 가져오는 방법 사용

    "your_product_category".toRequestBody("text/plain".toMediaTypeOrNull())

    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = categorySheetState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Category(
                    onItemClick = { category ->
                        selectedCategory.value = category
                        scope.launch {
                            categorySheetState.hide()
                        }
                    },
                    onCloseClick = {
                        scope.launch { categorySheetState.hide() }
                    }
                )
            }
        },
        sheetBackgroundColor = Color.White,
        sheetElevation = 8.dp
    ) {
        ModalBottomSheetLayout(
            sheetState = conceptSheetState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    SaleConcept(
                        onConceptClick = { concept ->
                            selectedConcept.value = concept
                            scope.launch { conceptSheetState.hide() }
                        },
                        onCloseClick = {
                            scope.launch { conceptSheetState.hide() }
                        }
                    )
                }
            },
            sheetBackgroundColor = Color.White,
            sheetElevation = 8.dp
        ) {
            ModalBottomSheetLayout(
                sheetState = platformSheetState,
                sheetContent = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.35f)
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Platform(
                            onPlatformClick = { platform ->
                                selectedPlatform.value = platform
                                // 선택된 플랫폼에 따라 색상 변경
                                selectedColor.value = when (platform) {
                                    "중고나라" -> Color(0xFF14AE5C)
                                    "당근" -> Color(0xFFFF8329)
                                    "번개장터" -> Color(0xFFFF0000)
                                    else -> Color(0xFF999999)
                                }
                                scope.launch { platformSheetState.hide() }
                            },
                            onCloseClick = {
                                scope.launch { platformSheetState.hide() }
                            }
                        )
                    }
                },
                sheetBackgroundColor = Color.White,
                sheetElevation = 8.dp
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(Color.White)
                ) {
                    val maxWidth = maxWidth
                    val maxHeight = maxHeight

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 상단 바
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(maxHeight * 0.08f)
                                .padding(horizontal = maxWidth * 0.04f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { navHostController.navigate("main"){
                                    popUpTo("write"){inclusive=true}
                                } },
                                modifier = Modifier.size(maxHeight * 0.03f)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.x_icon),
                                    contentDescription = "Back",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.padding(maxWidth * 0.12f))
                            Text(
                                text = "게시글 생성하기",
                                fontSize = 20.sp,
                                fontFamily = FontFamily(fontMedium),
                                lineHeight = 30.sp
                            )
                        }
                        Divider(color = Color(217, 217, 217), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(maxHeight * 0.01f))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = maxHeight * 0.02f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            item {
                                Text(
                                    text = "사진 가져오기",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(fontMedium),
                                    lineHeight = 22.sp,
                                    modifier = Modifier.widthIn(maxWidth),
                                    textAlign = TextAlign.Left
                                )
                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))

                                Box(
                                    modifier = Modifier
                                        .border(
                                            1.dp,
                                            color = Color(217, 217, 217),
                                            shape = RoundedCornerShape(size = 6.dp)
                                        )
                                        //.padding(maxHeight * 0.01f)
                                        .size(maxHeight * 0.07f)
                                        .clickable { showImagePickerDialog.value = true }
                                ) {
                                    if (imageUri.value != null) {
                                        AsyncImage(
                                            model = imageUri.value,
                                            contentDescription = "Selected image",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(size = 6.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.camera_icon),
                                                contentDescription = null,
                                                modifier = Modifier.size(maxHeight * 0.03f)
                                            )
                                        }
                                    }
                                }

                                if (showImagePickerDialog.value) {
                                    AlertDialog(
                                        onDismissRequest = {
                                            showImagePickerDialog.value = false
                                        },
                                        title = { Text("이미지 선택") },
                                        text = {
                                            Column {
                                                TextButton(onClick = {
                                                    if (cameraPermissionState.status.isGranted) {
                                                        takePictureLauncher.launch(null)
                                                    } else {
                                                        cameraPermissionState.launchPermissionRequest()
                                                    }
                                                    showImagePickerDialog.value = false
                                                }) {
                                                    Text("카메라로 촬영")
                                                }
                                                TextButton(onClick = {
                                                    if (galleryPermissionState.status.isGranted) {
                                                        pickImageLauncher.launch("image/*")
                                                    } else {
                                                        galleryPermissionState.launchPermissionRequest()
                                                    }
                                                    showImagePickerDialog.value = false
                                                }) {
                                                    Text("갤러리에서 선택")
                                                }
                                            }
                                        },
                                        confirmButton = {},
                                        dismissButton = {
                                            TextButton(onClick = {
                                                showImagePickerDialog.value = false
                                            }) {
                                                Text("취소")
                                            }
                                        }
                                    )
                                }
                            }

                            item {
                                //제목
                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))
                                Text(
                                    text = "제목",
                                    fontFamily = FontFamily(fontMedium),
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))
                                Box(
                                    modifier = Modifier
                                        .widthIn(maxWidth)
                                        .border(
                                            1.dp,
                                            color = Color(217, 217, 217),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .height(maxHeight * 0.07f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicTextField(
                                        value = title.value,
                                        onValueChange = { title.value = it },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        textStyle = TextStyle(
                                            fontFamily = FontFamily(fontMedium),
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp,
                                            color = Color(0xFF000000)
                                        ),
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus()
                                            }
                                        ),
                                        interactionSource = titleInteractionSource,
                                        decorationBox = { innerTextField ->
                                            Box(contentAlignment = Alignment.CenterStart) {
                                                if (title.value.isEmpty() && !isTitleFocused.value) {
                                                    Text(
                                                        text = "제목",
                                                        fontFamily = FontFamily(fontMedium),
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

                            item {
                                // 가격
                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))
                                Text(
                                    text = "가격",
                                    fontFamily = FontFamily(fontMedium),
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))
                                Box(
                                    modifier = Modifier
                                        .widthIn(maxWidth)
                                        .border(
                                            1.dp,
                                            color = Color(217, 217, 217),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .height(maxHeight * 0.07f),
                                    contentAlignment = Alignment.Center  //추가.
                                ) {
                                    BasicTextField(  //여기에 폰트 매기고,000넣고,거시긱,숫자패드,데이터 넘길때 쉼표컷.
                                        value = price.value,
                                        onValueChange = { newValue ->
                                            val cleanValue = removeCommas(newValue)
                                            if (cleanValue.length < 16 && cleanValue.all { it.isDigit() }) {
                                                price.value =
                                                    formatNumberWithCommas(cleanValue)
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(
                                                horizontal = 16.dp,
                                                vertical = 10.dp
                                            ),//(maxHeight * 0.02f),
                                        textStyle = TextStyle(
                                            fontFamily = FontFamily(fontMedium),
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp,
                                            color = Color(0xFF000000)
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus()
                                            }
                                        ),
                                        interactionSource = priceInteractionSource,
                                        decorationBox = { innerTextField ->
                                            Box(contentAlignment = Alignment.CenterStart) {
                                                if (price.value.isEmpty() && !isPriceFocused.value) {
                                                    Text(
                                                        text = "가격",
                                                        fontFamily = FontFamily(fontMedium),
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


                            item {
                                //플랫폼 선택
                                Spacer(modifier = Modifier.height(maxHeight * 0.025f))
                                Text(
                                    text = "플랫폼 선택",
                                    fontFamily = FontFamily(fontMedium),
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp
                                )
                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(maxHeight * 0.07f)
                                        .border(
                                            1.dp,
                                            color = Color(217, 217, 217),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable {
                                            scope.launch {
                                                platformSheetState.show()
                                            }
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .padding(start = maxHeight * 0.02f),
                                        text = if (selectedPlatform.value.isNotEmpty()) selectedPlatform.value else "플랫폼을 선택해주세요",
                                        fontFamily = FontFamily(fontMedium),
                                        color = selectedColor.value,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.chevron_down_1),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = maxHeight * 0.02f)
                                            .size(maxHeight * 0.025f),
                                        tint = Color(0xFF999999)
                                    )
                                }
                            }



                            item {
                                // 카테고리
                                Spacer(modifier = Modifier.height(maxHeight * 0.025f))
                                Text(
                                    text = "카테고리",
                                    fontFamily = FontFamily(fontMedium),
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                )
                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(maxHeight * 0.07f)
                                        .border(
                                            1.dp,
                                            color = Color(217, 217, 217),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable { // 여기에 clickable 추가
                                            scope.launch {
                                                categorySheetState.show()
                                            }
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier.padding(maxHeight * 0.02f),
                                        text = if (selectedCategory.value.isNotEmpty()) selectedCategory.value else "카테고리를 선택해주세요",
                                        fontFamily = FontFamily(fontMedium),
                                        color = if (selectedCategory.value.isNotEmpty()) Color(0xFF000000) else Color(0xFF999999),
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.chevron_down_1),
                                        contentDescription = "카테고리 선택",
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = maxHeight * 0.02f)
                                            .size(maxHeight * 0.025f),
                                        tint = Color(0xFF999999)
                                    )
                                }
                            }


                            item {
                                //판매 컨셉
                                Spacer(modifier = Modifier.height(maxHeight * 0.025f))
                                Text(
                                    text = "판매 컨셉",
                                    fontFamily = FontFamily(fontMedium),
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp
                                )
                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(maxHeight * 0.07f)
                                        .border(
                                            1.dp,
                                            color = Color(217, 217, 217),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable { // 여기에 clickable 추가
                                            scope.launch {
                                                conceptSheetState.show()
                                            }
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier.padding(maxHeight * 0.02f),
                                        text = if (selectedConcept.value.isNotEmpty()) selectedConcept.value else "판매 컨셉을 선택해주세요",
                                        fontFamily = FontFamily(fontMedium),
                                        color = if (selectedConcept.value.isNotEmpty()) Color(0xFF000000) else Color(0xFF999999),
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.chevron_down_1),
                                        contentDescription = "판매 컨셉 선택",
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = maxHeight * 0.02f)
                                            .size(maxHeight * 0.025f),
                                        tint = Color(0xFF999999)
                                    )
                                }
                            }

                            // 생성된 글
                            item {
                                // 생성이 완료 되어 lodingDialog 창이 닫혀 있을때 추가로 뜰 박스

                                Spacer(modifier = Modifier.height(maxHeight * 0.01f))

                                generatedText.value?.let { text ->

                                    Row {
                                        Text(
                                            text = "생성된 글",
                                            fontFamily = FontFamily(fontMedium),
                                            fontSize = 14.sp,
                                            lineHeight = 22.sp,
                                            color = Color(0xFF14AE5C)
                                        )
                                        Spacer(modifier = Modifier.padding(horizontal = maxWidth * 0.36f))
                                        Image(
                                            painter = painterResource(R.drawable.copy),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable {
                                                    copyToClipboard(context, text)
                                                }
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(maxHeight * 0.01f))

                                    Box(
                                        modifier = Modifier
                                            .widthIn(maxWidth)
                                            .border(
                                                1.dp,
                                                color = Color(0xFF14AE5C),
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                            .wrapContentHeight()
                                            .background(
                                                color = Color(0xFFE9F4EE),
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                    ) {
                                        Text(
                                            text = text,
                                            fontFamily = FontFamily(fontMedium),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(maxHeight * 0.02f))
                            }

                            // 버튼
                            item {
                                Spacer(modifier = Modifier.height(maxHeight * 0.02f))
                                if (generatedText.value == null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(maxHeight * 0.07f)
                                            .border(
                                                1.dp,
                                                color = Color(217, 217, 217),
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                    ) {
                                        Button(
                                            onClick = {
                                                isLoading.value = true
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    someFunction()

                                                    val text = introduceManager.getIntroduceText()
                                                    if (text != null) {
                                                        generatedText.value = text
                                                        isLoading.value = false
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(maxHeight * 0.07f),
                                            colors = ButtonDefaults.buttonColors(
                                                Color(
                                                    20,
                                                    174,
                                                    92
                                                )
                                            ),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "💫생성하기",
                                                fontFamily = FontFamily(fontSemiBold),
                                                fontSize = 17.sp,
                                                lineHeight = 2.sp
                                            )
                                        }
                                        if (isLoading.value) {
                                            LodingDialog()
                                        }
                                    }
                                } else {
                                    Row() {
                                        Button(
                                            onClick = {
                                                isLoading.value = true
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    someFunction()
                                                    val text =
                                                        introduceManager.getIntroduceText()
                                                    if (text != null) {
                                                        generatedText.value = text
                                                        isLoading.value = false
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .width(maxWidth * 0.44f)
                                                .height(maxHeight * 0.07f),
                                            colors = ButtonDefaults.buttonColors(Color.White),
                                            border = BorderStroke(
                                                1.dp,
                                                color = Color(20, 174, 92)
                                            ),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "다시 생성하기",
                                                fontFamily = FontFamily(fontSemiBold),
                                                fontSize = 17.sp,
                                                lineHeight = 2.sp,
                                                color = Color(20, 174, 92)
                                            )

                                            if (isLoading.value) {
                                                LodingDialog()
                                            }
                                        }

                                        Spacer(modifier = Modifier.padding(maxWidth * 0.02f))
                                        Button(
                                            onClick = { isplafome.value = true
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    val prices=introduceManager.getIntroducePrice()
                                                    IntroDuceplus(context, files = imageFile.value!!,
                                                        introduceCategory = selectedConcept.value,price=prices, productCategory = ad.value!!
                                                        ,product=title.value, introduceText = generatedText.value!!, companys = listOf(selectedPlatform.value)
                                                    )
                                                    viewModel.loadOnSaleProducts()
                                                    viewModel.sortOnSaleProductsByIdAscending()
                                                }},
                                            modifier = Modifier
                                                .width(maxWidth * 0.44f)
                                                .height(maxHeight * 0.07f),
                                            colors = ButtonDefaults.buttonColors(
                                                Color(20, 174, 92)
                                            ),
                                            shape = RoundedCornerShape(6.dp)

                                        ) {
                                            Text(
                                                text = "글쓰러 가기",
                                                fontFamily = FontFamily(fontSemiBold),
                                                fontSize = 17.sp,
                                                lineHeight = 2.sp,
                                                color = Color.White
                                            )

                                            if (isplafome.value) {
                                                PlatformDialog(
                                                    navController = NavController(context),
                                                    dismiss = { isplafome.value = false })
                                            }

                                            Log.d("물품 등록 APi", "ok")
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(maxHeight * 0.02f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
