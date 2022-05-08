package com.example.ypackfood.models.orders.OrderMin

data class DishMin(
    val id: Int,
    val count: Int,
    val portion: BasePortionMin,
    val addons: List<AddonMin>? = null,
)