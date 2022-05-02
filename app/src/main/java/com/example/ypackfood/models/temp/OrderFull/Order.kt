package com.example.ypackfood.models.temp.OrderFull

import com.example.ypackfood.models.detailContent.DetailContent

data class Order(
    val address: Address,
    val client: Client,
    val dateTime: String,
    val dishes: List<DetailContent>,
    val id: Int,
    val status: String,
    val totalPrice: Int
)