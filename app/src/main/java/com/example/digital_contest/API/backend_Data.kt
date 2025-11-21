package com.example.digital_contest.API

import kotlinx.serialization.Serializable

//
//// 거래 횟수 데이터 응답을 위한 데이터 클래스
//data class tradeDataResponse(
//    val data: tradeData
//)
//
//data class tradeData(
//    val count: Int
//)
//
//data class colorDataResponse(
//    val data: colorData
//
//)
//
//data class colorData(
//    val color: String
//)
//
////레벨 및 경험치 데이터 클레서
//data class LevelExperience(
//    val data: levelexperiencedata,
//    //val errordata: ErrorResponse
//)
////레벨 및 경험치 데이터 자료
//data class levelexperiencedata(
//    val levelExperience: Int,
//    val myLevelExperience: Int,
//    val myLevel:Int
//)

//
//data class CategoryStatisResponse(
//    val data: List<CategoryData>
//) //: StatisResponse
//
//@Serializable
//data class CategoryData(
//    val target: String,
//    val introduceTextCategory: String,
//    val introduceTextCategoryCount: String
//)
//
//// 회사 데이터
//data class CompanyStatisResponse(
//    val data: List<CompanyData>
//) //: StatisResponse
//
//@Serializable
//data class CompanyData(
//    val target: String,
//    val introduceTextCategory: String,
//    val introduceTextCategoryCount: String
//)
//
////여기아래는 전부 플랫폼 데이터 받기.
//@Serializable
//data class CompanyDetail(
//    val data: List<CompanyDetails>
//)
//
//@Serializable
//data class CompanyDetails(
//    val target: String,
//    val data: List<CompanyDetailData>
//)
//
//@Serializable
//data class CompanyDetailData(
//    val introduceTextCategory: String,
//    val introduceTextCategoryCount: String
//)
////카테고리 디테일
//@Serializable
//data class CategoryDetail(
//    val data: List<CategoryDetails>
//)
//
//@Serializable
//data class CategoryDetails(
//    val target: String,
//    val data: List<CategoryDetailData>
//)
//
//@Serializable
//data class CategoryDetailData(
//    val introduceTextCategory: String,
//    val introduceTextCategoryCount: String
//)