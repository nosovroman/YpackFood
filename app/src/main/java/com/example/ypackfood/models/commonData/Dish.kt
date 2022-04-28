package com.example.ypackfood.models.commonData

data class Dish(
    val basePortion: BasePortion,
    val category: String,
    val composition: String,
    val id: Int,
    val name: String,
    val picturePaths: PicturePaths
)