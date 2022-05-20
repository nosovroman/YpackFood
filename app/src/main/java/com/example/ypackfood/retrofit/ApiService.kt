package com.example.ypackfood.retrofit

import com.example.ypackfood.common.Constants.HEADER_AUTH
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.AuthorizationData
import com.example.ypackfood.models.auth.RegistrationData
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.models.commonData.Favorites
import com.example.ypackfood.models.detailAction.DetailAction
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.models.mainContent.Category
import com.example.ypackfood.models.orders.OrderFull.Order
import com.example.ypackfood.models.orders.OrderFull.OrderList
import com.example.ypackfood.models.orders.OrderMin.OrderMin
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
        // Блюдо
    @GET("client/dishes/actual/categories")
    suspend fun getMainContent(@Header(HEADER_AUTH) token: String): Response<MutableList<Category>>

    @GET("client/dishes/{dishId}")
    suspend fun getDetailContent(
        @Header(HEADER_AUTH) token: String,
        @Path("dishId") dishId: Int
    ): Response<DetailContent>

    @GET("client/specificDishes")
    suspend fun getContentByListId(
        @Header(HEADER_AUTH) token: String,
        @Query("ids") contentIdList: List<Int>
    ): Response<MutableList<Dish>>


        // Акции
    @GET("client/actions")
    suspend fun getActions(@Header(HEADER_AUTH) token: String): Response<MutableList<ActionsItem>>

    @GET("client/actions/{actionId}")
    suspend fun getDetailAction(
        @Header(HEADER_AUTH) token: String,
        @Path("actionId") actionId: Int
    ): Response<DetailAction>


        // Пользователь
    @GET("client/my/favorites")
    suspend fun getFavoritesId(@Header(HEADER_AUTH) token: String): Response<MutableList<Int>>

    @GET("client/my/favorites/dishes")
    suspend fun getFavorites(@Header(HEADER_AUTH) token: String): Response<MutableList<Dish>>

    @POST("client/my/favorites/{dishId}")
    suspend fun addFavorite(
        @Header(HEADER_AUTH) token: String,
        @Path("dishId") dishId: Int
    ): Response<Favorites>

    @DELETE("client/my/favorites/{dishId}")
    suspend fun deleteFavorite(
        @Header(HEADER_AUTH) token: String,
        @Path("dishId") dishId: Int
    ): Response<Favorites>


        // Заказ
    @POST("client/my/orders")
    suspend fun createOrder(
        @Header(HEADER_AUTH) token: String,
        @Body order: OrderMin
    ): Response<Order>

    @GET("client/my/orders")
    suspend fun getHistory(
        @Header(HEADER_AUTH) token: String,
        @Query("page") page: Int
    ): Response<OrderList>
    //suspend fun getHistory(@Header(HEADER_AUTH) token: String): Response<MutableList<Order>>


        // Авторизация
    @POST("auth/login")
    suspend fun authorizeUser(
        @Body auth: AuthorizationData
    ): Response<AuthInfo>

    @POST("auth/register")
    suspend fun registerUser(
        @Body auth: RegistrationData
    ): Response<AuthInfo>
}