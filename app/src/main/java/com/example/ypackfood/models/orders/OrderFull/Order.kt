package com.example.ypackfood.models.orders.OrderFull

import com.example.ypackfood.models.detailContent.DetailContent

data class Order(
    val address: Address?,
    val client: Client,
    val created: String,
    val deliveryTime: String,
    val dishes: List<DetailContent>,
    val id: Int,
    val status: String?,
    val totalPrice: Int,
    val wayToGet: String
)