package com.example.ypackfood.retrofit

import com.example.ypackfood.models.actionsContent.Actions
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.models.commonData.Dishes
import com.example.ypackfood.models.detailAction.DetailAction
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.models.mainContent.Categories
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("dishes")
    suspend fun getMainContent(): Response<Categories>

    @GET("actions")
    suspend fun getActions(): Response<Actions>

    @GET("dish/{dishId}")
    suspend fun getDetailContent(@Path("dishId") dishId: Int): Response<DetailContent>

    @GET("action/{actionId}")
    suspend fun getDetailAction(@Path("actionId") actionId: Int): Response<DetailAction>

    @GET("specificDishes")
    suspend fun getContentByListId(@Query("ids%5B%5D", encoded = true) contentIdList: List<Int>): Response<Dishes>
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