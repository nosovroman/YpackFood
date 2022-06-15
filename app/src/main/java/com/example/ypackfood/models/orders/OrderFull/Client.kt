package com.example.ypackfood.models.orders.OrderFull

import com.example.ypackfood.models.commonData.Address

data class Client(
    val addresses: List<Address>,
    val email: String,
    val id: Int,
    val name: String?,
    val password: String
)