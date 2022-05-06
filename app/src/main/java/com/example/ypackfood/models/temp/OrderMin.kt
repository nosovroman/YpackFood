package com.example.ypackfood.models.temp

data class OrderMin(
    val targetProduction: String,
    val totalPrice: Int,
    val address: AddressMin?,
    val dishes: List<DishMin>,
    val wayToGet: String
)