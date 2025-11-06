package com.example.digital_contest.API

import android.content.Context
import android.util.Log
import com.example.digital_contest.API.Manager.TokenManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.io.File
 //게시글 생성 api

suspend fun IntroduceCreate(context:Context,files:File, //이미지 파일,소개글(말투),가격,물품이름,물품카테고리
                            introduceCategory:String,
                            price:Int,
                            product:String,productCategory:String) {

    val retrofit: Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)

    val api: WriteService = retrofit.create(WriteService::class.java) //쓰기서비스 생성
    val tokenManager = TokenManager(context)
    val introManager = IntroduceDataStore(context)

    val accessToken = tokenManager.getAccessToken() //액세스 토큰 불러오기

    if(accessToken != null){
        try{ //요청파일,이미지만 넣기
            val requestFile = files.asRequestBody("image/*".toMediaTypeOrNull())//mapIndexed { index, file ->
                val filePart = MultipartBody.Part.createFormData("files", files.name, requestFile)//멀티파트  폼데이터를 생성

            // 다른 파라미터들을 RequestBody로 변환
            val introduceCategoryBody = introduceCategory.toRequestBody("text/plain".toMediaTypeOrNull()) //멀티파트를 사용하므로 다같이 생성.
            val priceBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val productBody = product.toRequestBody("text/plain".toMediaTypeOrNull())
            val productCategoryBody = productCategory.toRequestBody("text/plain".toMediaTypeOrNull())  //오류시 toString빼셈..toString()

            val response = api.uploadProduct( //해당 데이터로 api와 통신
                token = "$accessToken",
                files =filePart,
                introduceCategory=introduceCategoryBody,
                price=priceBody,
                product=productBody,
                productCategory=productCategoryBody
            )
            if(response.isSuccessful){ //성공하면
                val writePostData=response.body() //해당 데이터 가져오고
                if(writePostData != null){
                    val introduceText=writePostData.data.introduceText
                    val priceDatas=writePostData.data.price
                    //val productprice =writePostData.data.price
                    introManager.savaIntroduceText(introduceText) //저장.
                    introManager.savaIntroduceprice(priceDatas)

                    Log.d("성공","데이터 = ${response.body().toString()}")
                    Log.d("데이터 저장하는거 성공!","성공!")
                    Log.d("데이터 저장하는거 성공!","${price}")
                    Log.d("데이터 체크","${introduceText},${priceDatas}")
                }else{
                    Log.e("에러1","${response.errorBody()?.toString()}")
                }
            }else{
                val errorCode = response.code()
                val errorMessage = response.message()
                val errorBody = response.errorBody()?.string()
                Log.e("에러", "에러 바디: $errorBody")
                Log.e("에러", "코드: $errorCode, 메시지: $errorMessage")
                Log.e("에러","${response.errorBody()?.toString()}")
            }
        }catch (e: Exception){

            Log.e("호출중 에러","$e")
        }

    }else {
        Log.e("토큰이 없어용","$accessToken")
    }

}

suspend fun IntroDuceplus(context:Context,files:File  //이미지 파일,소개글(말투),가격,물품이름,물품카테고리
                          ,introduceCategory:String,productCategory:String //productCategory:String  //productCategory:Int 둘중 오류안나는거 선택,기본은 int
                          ,price:Int
                          ,product:String,introduceText:String
                          ,companys:List<String>){  //물품을 추가하는 api
    val retrofit: Retrofit = RetrofitHelper.getRetrofitInstance(WriteService::class.java)
    val api: WriteService = retrofit.create(WriteService::class.java)
    val tokenManager = TokenManager(context)

    val accessToken = tokenManager.getAccessToken()

    if(accessToken != null){
        try{
            val requestFile=files.asRequestBody("image/*".toMediaTypeOrNull())  //이미지형태로 올려서
            val filePart = MultipartBody.Part.createFormData("files",files.name,requestFile)  //파일로 생성.

            val introduceCategoryBody = introduceCategory.toRequestBody("text/plain".toMediaTypeOrNull())  //아래는 멀티파트 폼형태이기에 변형해 보내줌.
            val productCategory = productCategory.toRequestBody("text/plain".toMediaTypeOrNull())  //위에서 String으로 바꿀시,tostring()빼셈.toString()
            val priceBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val productBody = product.toRequestBody("text/plain".toMediaTypeOrNull())
            val introduceTextBody = introduceText.toRequestBody("text/plain".toMediaTypeOrNull())


            val companysParts = companys.mapIndexed { index, company ->  //인덱스와 회사별로 멀티파트를 나누어 저장해서
                MultipartBody.Part.createFormData("companys[$index]", company)
            }
            val response = api.productplus(  //보냄.
                token="$accessToken",
                files=filePart,
                introduceCategory=introduceCategoryBody,
                productCategory=productCategory,
                price=priceBody,
                product=productBody,
                introduceText = introduceTextBody,
                companys=companysParts
            )
            if(response.isSuccessful){  //성공일시
                val writePost = response.body()?.toString()  //내용부분을 writepost에 담음.(어차피 돌아오는 정보가 없기에 성공이 뜨면 ok
                if(writePost != null){
                    Log.d("없어","데이터 = $writePost" )
                }else {
                    Log.e("성공1","${response.errorBody()?.string()}")
                    Log.d("성공2","데이터 = ${response.code()}" )
                }
            }   else{
                val errorCode = response.code()
                val errorMessage = response.message()
                val errorBody = response.errorBody()?.string()
                Log.e("에러값 체크","${productCategory}")
                Log.e("에러", "에러 바디: $errorBody")
                Log.e("에러", "코드: $errorCode, 메시지: $errorMessage")
            }

        }catch (e:Exception){
            Log.e("호출중에러","$e")
        }
    }else{
        Log.e("토큰이 없습니다","$accessToken")
    }

}