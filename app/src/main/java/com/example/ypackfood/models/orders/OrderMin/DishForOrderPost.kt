package com.example.ypackfood.models.orders.OrderMin

import com.example.ypackfood.models.detailContent.DetailContent

data class DishForOrderPost(
    val count: Int,
    val dish: DishMin
)

data class DishForOrderGet(
    val count: Int,
    val dish: DetailContent
)