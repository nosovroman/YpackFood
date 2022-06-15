package com.example.ypackfood.models.orders.OrderFull

import com.example.ypackfood.models.commonData.BasePortion
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.models.commonData.PicturePaths

data class DishOrder(
    val category: String,
    val deleted: Boolean,
    val dishes: List<Dish>,
    val id: Int,
    val name: String,
    val picturePaths: PicturePaths,
    val portion: BasePortion
)