package com.example.ypackfood.models.temp.OrderFull

data class Client(
    val addresses: List<Address>,
    val email: String,
    val id: Int,
    val name: String,
    val password: String
)