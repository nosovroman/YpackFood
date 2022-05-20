package com.example.ypackfood.models.orders.OrderFull

data class OrderList(
    val totalCount: Int,
    val orders: MutableList<Order>
)
