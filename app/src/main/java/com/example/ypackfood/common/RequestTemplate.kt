package com.example.ypackfood.common

import com.example.ypackfood.models.commonData.ErrorResponse
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.room.entities.CartEntity
import com.google.gson.Gson

object RequestTemplate {
    var mainRepository: Repository

    fun getErrorFromJson(jsonString: String): ErrorResponse {
        return Gson().fromJson(jsonString, ErrorResponse::class.java)
    }

    fun composeCartInfo(dishId: Int, portionId: Int, priceId: Int, price: Int, count: Int): CartEntity {
        return CartEntity(
            dishId = dishId,
            userId = Auth.authInfo.personId,
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count
        )
    }

    init {
        val apiService = RetrofitBuilder.apiService
        mainRepository = Repository(apiService)
    }
}