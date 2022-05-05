package com.example.ypackfood.models.dima

data class Order(
    val address: Address,
    val client: Client,
    val dishes: List<Dishe>,
    val totalPrice: Int
)