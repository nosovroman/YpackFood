package com.example.ypackfood.models.temp

data class DishMin(
    val addons: List<AddonMin>? = null,
    val basePortion: BasePortionMin,
    val id: Int
)