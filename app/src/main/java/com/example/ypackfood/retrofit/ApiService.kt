package com.example.ypackfood.retrofit

import com.example.ypackfood.dataClasses.detailContent.DetailContent
import com.example.ypackfood.dataClasses.mainContent.Categories
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path

interface ApiService {
    @GET("dishes")
    suspend fun getMainContent(): Response<Categories>

    @GET("dish/{dishId}")
    suspend fun getDetailContent(@Path("dishId") dishId: Int): Response<DetailContent>
}
//@GET("hello")
//suspend fun getHello(): Response<String>
//
//@GET("credit_card?details=")
//suspend fun getCard(@Query("details") details_card: String): Response<String>
//
//@GET("path/{number}")
//suspend fun getPath(@Path("number") number: Int): Response<String>
//
//@GET("body_value")
//suspend fun getBody(@Query("body_value") body_value: String): Response<String>
//
//@GET("teapot")
//suspend fun getTea(): Response<String>
//
//@GET("error1")
//suspend fun getError(): Response<String>