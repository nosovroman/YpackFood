package com.example.ypackfood.retrofit

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("hello")
    suspend fun getHello(): Response<String>

    @GET("credit_card?details=")
    suspend fun getCard(@Query("details") details_card: String): Response<String>

    @GET("path/{number}")
    suspend fun getPath(@Path("number") number: Int): Response<String>

    @GET("body_value")
    suspend fun getBody(@Query("body_value") body_value: String): Response<String>

    @GET("teapot")
    suspend fun getTea(): Response<String>

    @GET("error1")
    suspend fun getError(): Response<String>


}