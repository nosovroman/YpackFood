package com.example.ypackfood.models.orders.common

import com.example.ypackfood.models.orders.OrderFull.DishOrder
import com.example.ypackfood.models.orders.OrderMin.DishMin

data class DishForOrderPost(
    val count: Int,
    val dish: DishMin
)

data class DishForOrderGet(
    val count: Int,
    val dish: DishOrder
)