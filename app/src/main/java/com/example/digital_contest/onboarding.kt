package com.example.digital_contest

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

//import

@ExperimentalFoundationApi
@Composable
fun onboarding(viewModel: MainActivityViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 5 })

    BoxWithConstraints(modifier=Modifier.fillMaxSize().background(Color.White)) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {// 인디케이터
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.065f, bottom = 98.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(screenWidth * 0.23f)
                    .height(screenHeight * 0.025f)
                    .background(Color(0xFFBFBFBF).copy(0.44f), RoundedCornerShape(50.dp))
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color(0xFF0D0D0D) else Color.Gray
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                                ,

                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when (page) {
                    0 -> onboarding1(screenWidth, screenHeight)
                    1 -> onboarding2(screenWidth, screenHeight)
                    2 -> onboarding3(screenWidth, screenHeight)
                    3 -> onboarding4(screenWidth, screenHeight)
                    4 -> onboarding5(screenWidth, screenHeight)
                }
            }
        }



        // 시작 버튼
        if (pagerState.currentPage != 0) {
            Button(
                onClick = {
                    navController.navigate("Login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                    scope.launch { viewModel.saveOnboardIngState(true) }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
                    .height(40.dp),
                colors= ButtonDefaults.buttonColors(containerColor =Color(0xFF14AE5C))
            ) {
                Text("시작하기")
            }
        }
    }
}
    }
@Composable
fun onboarding1(screenWidth: Dp,screenHeight:Dp){
    val fontBold = FontFamily(Font(R.font.pretendard_bold))

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = screenHeight * 0.065f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween)
    {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier=Modifier.weight(1f)){
            Spacer(modifier = Modifier.height(screenHeight * 0.072F))
            Text(
                text="중고거래 글 작성에도\n비법이 있다는거 아시나요?",
                color=Color(15, 126, 0),
                fontSize = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.096f).toSp() },
                fontFamily = fontBold,
                textAlign= TextAlign.Center
                //modifier=Modifier.padding(bottom = 80.dp)
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.047f))
            Text(text = "Eco-Marketer가 다양한 말투로\n알아서 만들어 드립니다.\n시세와 플랫폼 분석까지 한 번에 조사하실 필요없이\n간편히 사용해보세요."
                ,                fontSize = with(LocalDensity.current) { (screenWidth * 0.0427f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() }, fontFamily = fontBold,
                textAlign=TextAlign.Center)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.52f)
                //.padding(vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.write_view_1),
                contentDescription = "Center Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }


}

@Composable
fun onboarding2(screenWidth: Dp,screenHeight: Dp){
    val fontBold = FontFamily(Font(R.font.pretendard_bold))

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = screenHeight * 0.065f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween)
    {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier=Modifier.weight(1f)){
            Spacer(modifier = Modifier.height(screenHeight * 0.072F))
            Text(
                text="글쓰기 힘들고\n글을 올려도 잘 팔리지 않나요?",
                color=Color(15, 126, 0),
                fontSize = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.096f).toSp() },
                fontFamily = fontBold,
                textAlign= TextAlign.Center
                //modifier=Modifier.padding(bottom = 80.dp)
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.047f))
            Text(text = "Eco-Marketer는 둥글둥글체,단호박체,성냥팔이체 등\n다양한 말투를 사용하여\nAI를 통해 구매자의 눈을 사로잡고\n판매율을 올릴 수 있는 글을 작성해줍니다."
                ,                fontSize = with(LocalDensity.current) { (screenWidth * 0.0415f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() }, fontFamily = fontBold,
                textAlign=TextAlign.Center)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.52f)
            //.padding(vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.write_view_2),
                contentDescription = "Center Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun onboarding3(screenWidth: Dp,screenHeight: Dp){
    val fontBold = FontFamily(Font(R.font.pretendard_bold))

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = screenHeight * 0.065f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween)
    {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier=Modifier.weight(1f)){
            Spacer(modifier = Modifier.height(screenHeight * 0.072F))
            Text(
                text="왜 이 플랫폼만\n물건이 안 팔릴까요?",
                color=Color(15, 126, 0),
                fontSize = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.096f).toSp() },
                fontFamily = fontBold,
                textAlign= TextAlign.Center
                //modifier=Modifier.padding(bottom = 80.dp)
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.047f))
            Text(text = "플랫폼 마다 선호하는 느낌이 달라요!\nEco-Marketer가 다 분석해 놨으니\n더 효과적인 중고거래를 시작해보세요."
                ,                fontSize = with(LocalDensity.current) { (screenWidth * 0.0427f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() }, fontFamily = fontBold,
                textAlign=TextAlign.Center)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.52f)
            //.padding(vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.write_view_3),
                contentDescription = "Center Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun onboarding4(screenWidth: Dp,screenHeight: Dp){
    val fontBold = FontFamily(Font(R.font.pretendard_bold))

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = screenHeight * 0.065f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween)
    {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier=Modifier.weight(1f)){
            Spacer(modifier = Modifier.height(screenHeight * 0.072F))
            Text(
                text="애증이 담긴 물건\n가격 측정 어렵죠?",
                color=Color(15, 126, 0),
                fontSize = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.096f).toSp() },
                fontFamily = fontBold,
                textAlign= TextAlign.Center
                //modifier=Modifier.padding(bottom = 80.dp)
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.038f))
            Text(text = "Eco-Marketerd가\n객관적인 시세를 알아볼 수 있도록 했습니다.\n여러 플랫폼의 가격을 검색하여\n같거나 비슷한 물건의 시세를\n빠르게 한눈에 볼 수 있어요!"
                ,fontSize = with(LocalDensity.current) { (screenWidth * 0.0427f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() },fontFamily = fontBold,
                textAlign=TextAlign.Center)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.52f)
            //.padding(vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.write_view_4),
                contentDescription = "Center Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun onboarding5(screenWidth: Dp,screenHeight: Dp){
    val fontBold = FontFamily(Font(R.font.pretendard_bold))

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = screenHeight * 0.065f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween)
    {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier=Modifier.weight(1f)){
            Spacer(modifier = Modifier.height(screenHeight * 0.072F))
            Text(
                text="중고 거래가\n 지구에 큰 도움이 된다는 걸 아시나요?",
                color=Color(15, 126, 0),
                fontSize = with(LocalDensity.current) { (screenWidth * 0.058f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.096f).toSp() },
                fontFamily = fontBold,
                textAlign= TextAlign.Center
                //modifier=Modifier.padding(bottom = 80.dp)
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.047f))
            Text(text = "매 횟수와 탄소 절감 기여도에 따라\n레벨이 증가하고 테마가 변해요!\n중고 거래를 통해 다양한 테마도 보고\n 탄소 절감도 도와봐요!"
                ,  fontSize = with(LocalDensity.current) { (screenWidth * 0.0427f).toSp() },
                lineHeight = with(LocalDensity.current) { (screenWidth * 0.064f).toSp() }
                ,fontFamily = fontBold,
                textAlign=TextAlign.Center)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.52f)
            //.padding(vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.write_view_5),
                contentDescription = "Center Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
