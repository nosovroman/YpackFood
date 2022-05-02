package com.example.ypackfood.models.temp

data class OrderMin(
    val address: AddressMin,
    val client: ClientMin,
    val dishes: List<DishMin>,
    val totalPrice: Int
)