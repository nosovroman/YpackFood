package com.example.ypackfood.models.orders.OrderMin

import com.example.ypackfood.models.orders.common.DishForOrderPost

data class OrderMin(
    val deliveryTime: String,
    val totalPrice: Int,
    val address: AddressMin?,
    val dishes: List<DishForOrderPost>,
    val wayToGet: String
)