package com.example.ypackfood.common

import com.example.ypackfood.models.commonData.ErrorResponse
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.room.entities.CartEntity
import com.google.gson.Gson

object RequestTemplate {
    var mainRepository: Repository
    var TOKEN: String

    fun getErrorFromJson(jsonString: String): ErrorResponse {
        return Gson().fromJson(jsonString, ErrorResponse::class.java)
    }

    fun buildDishInfo(dishId: Int, portionId: Int, priceId: Int, price: Int, count: Int, addons: String? = null): CartEntity {
        return CartEntity(
            dishId = dishId,
            userId = Auth.authInfo.personId,
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count,
            dishAddons = addons
        )
    }

    init {
        val apiService = RetrofitBuilder.apiService
        mainRepository = Repository(apiService)

        TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd2VydEBnbWFpbC5jb20iLCJpZCI6MzUsImlhdCI6MTY1MTg0NjA4MCwiZXhwIjoxNjU0NTI0NDgwfQ.5u33FglKAK6fq3eTBvhl4uyaKy4IwITekt8Eftet2o8"
    }
}