package com.example.ypackfood.repository

import com.example.ypackfood.models.auth.Authorization
import com.example.ypackfood.models.temp.OrderMin
import com.example.ypackfood.retrofit.ApiService

class Repository(private val apiService: ApiService) {
    suspend fun authorizeUser(auth: Authorization) = apiService.authorizeUser(auth)
    suspend fun registerUser(auth: Authorization) = apiService.registerUser(auth)
    suspend fun getMainContent(token: String) = apiService.getMainContent(token)
    suspend fun getActions(token: String) = apiService.getActions(token)
    suspend fun getDetailContent(token: String, dishId: Int) = apiService.getDetailContent(token, dishId)
    suspend fun getFavorites(token: String) = apiService.getFavorites(token)
    suspend fun addFavorite(token: String, contentId: Int) = apiService.addFavorite(token, contentId)
    suspend fun deleteFavorite(token: String, contentId: Int) = apiService.deleteFavorite(token, contentId)
    suspend fun getDetailAction(token: String, actionId: Int) = apiService.getDetailAction(token, actionId)
    suspend fun getContentByListId(token: String, contentIdList: List<Int>) = apiService.getContentByListId(token, contentIdList)
    suspend fun createOrder(token: String, order: OrderMin) = apiService.createOrder(token, order)
}

//suspend fun getHello() = apiService.getHello()
//suspend fun getCard(details_card: String) = apiService.getCard(details_card)
//suspend fun getPath(number: Int) = apiService.getPath(number)
//suspend fun getBody(body_value: String) = apiService.getBody(body_value)
//suspend fun getTea() = apiService.getTea()
//suspend fun getError() = apiService.getError()