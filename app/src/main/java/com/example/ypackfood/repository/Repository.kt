package com.example.ypackfood.repository

import com.example.ypackfood.models.temp.OrderMin
import com.example.ypackfood.retrofit.ApiService

class Repository(private val apiService: ApiService) {
    suspend fun getMainContent() = apiService.getMainContent()
    suspend fun getActions() = apiService.getActions()
    suspend fun getDetailContent(dishId: Int) = apiService.getDetailContent(dishId)
    suspend fun getDetailAction(actionId: Int) = apiService.getDetailAction(actionId)
    suspend fun getContentByListId(contentIdList: List<Int>) = apiService.getContentByListId(contentIdList)
    suspend fun createOrder(order: OrderMin) = apiService.createOrder(order)
}

//suspend fun getHello() = apiService.getHello()
//suspend fun getCard(details_card: String) = apiService.getCard(details_card)
//suspend fun getPath(number: Int) = apiService.getPath(number)
//suspend fun getBody(body_value: String) = apiService.getBody(body_value)
//suspend fun getTea() = apiService.getTea()
//suspend fun getError() = apiService.getError()