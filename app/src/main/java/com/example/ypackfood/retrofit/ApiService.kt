package com.example.ypackfood.retrofit

import com.example.ypackfood.common.Constants.HEADER_AUTH
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.Authorization
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.models.commonData.Favorites
import com.example.ypackfood.models.detailAction.DetailAction
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.models.mainContent.Category
import com.example.ypackfood.models.temp.OrderFull.Order
import com.example.ypackfood.models.temp.OrderMin
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("dishes")
    suspend fun getMainContent(@Header(HEADER_AUTH) token: String): Response<MutableList<Category>>

    @GET("actions")
    suspend fun getActions(@Header(HEADER_AUTH) token: String): Response<MutableList<ActionsItem>>

    @GET("dishes/{dishId}")
    suspend fun getDetailContent(
        @Header(HEADER_AUTH) token: String,
        @Path("dishId") dishId: Int
    ): Response<DetailContent>

    @GET("my/favorites")
    suspend fun getFavoritesId(@Header(HEADER_AUTH) token: String): Response<MutableList<Int>>

    @GET("my/favorite/dishes")
    suspend fun getFavorites(@Header(HEADER_AUTH) token: String): Response<MutableList<Dish>>

    @POST("my/favorites/{dishId}")
    suspend fun addFavorite(
        @Header(HEADER_AUTH) token: String,
        @Path("dishId") dishId: Int
    ): Response<Favorites>

    @DELETE("my/favorites/{dishId}")
    suspend fun deleteFavorite(
        @Header(HEADER_AUTH) token: String,
        @Path("dishId") dishId: Int
    ): Response<Favorites>

    @GET("actions/{actionId}")
    suspend fun getDetailAction(
        @Header(HEADER_AUTH) token: String,
        @Path("actionId") actionId: Int
    ): Response<DetailAction>

    @GET("specificDishes")
    suspend fun getContentByListId(
        @Header(HEADER_AUTH) token: String,
        @Query("ids") contentIdList: List<Int>
    ): Response<MutableList<Dish>>

    @POST("my/orders")
    suspend fun createOrder(
        @Header(HEADER_AUTH) token: String,
        @Body order: OrderMin
    ): Response<Order>

    @POST("auth/login")
    suspend fun authorizeUser(
        @Body auth: Authorization
    ): Response<AuthInfo>

    @POST("auth/register")
    suspend fun registerUser(
        @Body auth: Authorization
    ): Response<AuthInfo>
}