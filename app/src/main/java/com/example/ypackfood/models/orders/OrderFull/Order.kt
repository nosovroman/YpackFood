package com.example.ypackfood.models.orders.OrderFull

import com.example.ypackfood.models.commonData.Address
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.models.orders.OrderMin.DishForOrderGet

data class Order(
    val id: Int,
    val created: String,
    val status: String?,
    val totalPrice: Int,
    val dishes: List<DishForOrderGet>,
    val deliveryTime: String,
    val wayToGet: String,
    val client: Client,
    val address: Address?,
)