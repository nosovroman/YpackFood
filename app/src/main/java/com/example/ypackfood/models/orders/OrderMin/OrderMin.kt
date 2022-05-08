package com.example.ypackfood.models.orders.OrderMin

data class OrderMin(
    val deliveryTime: String,
    val totalPrice: Int,
    val address: AddressMin?,
    val dishes: List<DishMin>,
    val wayToGet: String
)