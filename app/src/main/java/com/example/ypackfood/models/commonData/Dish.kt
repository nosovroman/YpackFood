package com.example.ypackfood.models.commonData

data class Dish(
    val id: Int,
    val picturePaths: PicturePaths,
    val name: String,
    val composition: String,
    val category: String,
    val deleted: Boolean,
    val basePortion: BasePortion,
)