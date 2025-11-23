package com.example.digital_contest.Write

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.digital_contest.R
import com.example.digital_contest.Write.Dialog.LodingDialog
import com.example.digital_contest.Write.Dialog.PlatformDialog
import com.example.digital_contest.Write.Ui.ActionButtons
import com.example.digital_contest.Write.Ui.CategorySheet
import com.example.digital_contest.Write.Ui.CustomTextField
import com.example.digital_contest.Write.Ui.GeneratedTextBox
import com.example.digital_contest.Write.Ui.ImageSourceSheet
import com.example.digital_contest.Write.Ui.LabelText
import com.example.digital_contest.Write.Ui.PhotoPickerBox
import com.example.digital_contest.Write.Ui.PlatformSheet
import com.example.digital_contest.Write.Ui.SaleConceptSheet
import com.example.digital_contest.Write.Ui.SelectBox
import com.example.digital_contest.Write.Ui.TopBar
import com.example.digital_contest.Write.util.ImageUtil
import com.example.digital_contest.Write.util.copyToClipboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteView(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: WriteViewModel = viewModel(factory = WriteViewModelFactory(context))

    // ViewModel의 상태 구독
    val writeUiState by viewModel.writeUiState.collectAsState()
    val productPlusState by viewModel.productPlusState.collectAsState()
    val title by viewModel.title.collectAsState(initial = "")
    val price by viewModel.price.collectAsState(initial = "")
    val selectedPlatform by viewModel.selectedPlatform.collectAsState(initial = "")
    val selectedCategory by viewModel.selectedCategory.collectAsState(initial = "")
    val selectedConcept by viewModel.selectedConcept.collectAsState(initial = "")
    val generatedText by viewModel.generatedText.collectAsState(initial = "")

    val showPlatformSheet = remember { mutableStateOf(false) }
    val showCategorySheet = remember { mutableStateOf(false) }
    val showConceptSheet = remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceSheet by remember { mutableStateOf(false) }
    var showPlatformDialog by remember { mutableStateOf(false) }

    // UI 상태 처리
    LaunchedEffect(writeUiState) {
        when (val state = writeUiState) {
            is WriteUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }

            is WriteUiState.Success -> {
                Toast.makeText(context, "게시글이 생성되었습니다", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }

            else -> {}
        }
    }

    // generatedText 변경 감지 (디버그용)
    LaunchedEffect(generatedText) {
        android.util.Log.d("WriteView", "Generated text changed: $generatedText")
    }
    LaunchedEffect(productPlusState) {
        when (val state = productPlusState) {
            is ProductPlusState.Success -> {
                Toast.makeText(context, "게시글이 성공적으로 등록되었습니다", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                showPlatformDialog = false
            }

            is ProductPlusState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }

            else -> {}
        }
    }

    // Android 시스템 Photo Picker 사용 (Android 13+에서 시스템 UI 제공)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            try {
                // URI를 Bitmap으로 변환 후 파일 저장
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                }
                val file = ImageUtil.saveBitmapToFile(context, bitmap)
                val fileUri = Uri.fromFile(file)
                selectedImageUri = fileUri
                viewModel.updateImageUri(fileUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 권한 요청 Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            // 권한이 허용되면 이미지 소스 선택 BottomSheet 표시
            showImageSourceSheet = true
        }
    }

    // 카메라 Launcher
    val cameraLauncher = ImageUtil.rememberCameraLauncher { bitmap ->
        val file = ImageUtil.saveBitmapToFile(context, bitmap)
        val fileUri = Uri.fromFile(file)
        selectedImageUri = fileUri
        viewModel.updateImageUri(fileUri)
    }

    // 권한 체크 및 이미지 소스 선택 함수
    val checkAndRequestPermissions: () -> Unit = {
        if (ImageUtil.hasImagePermissions(context)) {
            // 이미 권한이 있으면 바로 선택 BottomSheet 표시
            showImageSourceSheet = true
        } else {
            // 권한 요청
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
            permissionLauncher.launch(permissions)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
    ) {
        val maxHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                title = "게시글 생성하기",
                onNavClick = {
                    navController.navigate("main") { popUpTo("write") { inclusive = true } }
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
                        imageUri = selectedImageUri,
                        onPickRequest = checkAndRequestPermissions  // 권한 체크 후 다이얼로그 표시
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LabelText("제목")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = title,
                        onValueChange = { viewModel.updateTitle(it) },
                        placeholder = "제목",
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LabelText("가격")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = price,
                        onValueChange = { viewModel.updatePrice(it) },
                        placeholder = "가격",
                        keyboardType = KeyboardType.Number,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectBox(
                        label = "플랫폼 선택",
                        value = selectedPlatform,
                        placeholder = "플랫폼을 선택해주세요",
                        onClick = { showPlatformSheet.value = true },
                        iconRes = R.drawable.chevron_down_1
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectBox(
                        label = "카테고리",
                        value = selectedCategory,
                        placeholder = "카테고리를 선택해주세요",
                        onClick = { showCategorySheet.value = true },
                        iconRes = R.drawable.chevron_down_1
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectBox(
                        label = "판매 컨셉",
                        value = selectedConcept,
                        placeholder = "판매 컨셉을 선택해주세요",
                        onClick = { showConceptSheet.value = true },
                        iconRes = R.drawable.chevron_down_1
                    )
                }
                // 생성된 글
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (!generatedText.isNullOrEmpty()) {
                        GeneratedTextBox(
                            text = generatedText,
                            onCopyClick = {
                                copyToClipboard(context, generatedText)
                                Toast.makeText(context, "클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 버튼
                item {
                    ActionButtons(
                        isGenerated = !generatedText.isNullOrEmpty(),
                        onGenerateClick = { viewModel.uploadProduct() },
                        onRegenerateClick = { viewModel.uploadProduct() },
                        onGoWriteClick = {
                            showPlatformDialog = true
                            viewModel.productPlus()
                        }
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
                    viewModel.updatePlatform(it)
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
                    viewModel.updateCategory(it)
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
                    viewModel.updateConcept(it)
                    showConceptSheet.value = false
                }
            )
        }
    }

    // 이미지 소스 선택 BottomSheet (갤러리/카메라)
    if (showImageSourceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showImageSourceSheet = false },
            containerColor = Color.White,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            ImageSourceSheet(
                onGalleryClick = {
                    showImageSourceSheet = false
                    imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onCameraClick = {
                    showImageSourceSheet = false
                    cameraLauncher.launch(null)
                }
            )
        }
    }

    // 로딩 다이얼로그
    if (writeUiState is WriteUiState.Loading) {
        LodingDialog()
    }
    // 플랫폼 선택 다이얼로그
    if (showPlatformDialog) {
        PlatformDialog(
            dismiss = { showPlatformDialog = false },
            navController = navController
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WriteViewPreview() {
    val navController = rememberNavController()
    WriteView(navController)
}