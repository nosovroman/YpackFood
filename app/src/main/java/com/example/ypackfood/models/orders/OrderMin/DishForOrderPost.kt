package com.example.ypackfood.models.orders.OrderMin

import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.models.orders.OrderFull.temp.DishOrder


data class DishForOrderPost(
    val count: Int,
    val dish: DishMin
)

data class DishForOrderGet(
    val count: Int,
    val dish: DishOrder//DetailContent
)