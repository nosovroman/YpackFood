package com.example.ypackfood.dataClasses.commonData

data class Dishe(
    val basePortion: BasePortion,
    val category: String,
    val composition: String,
    val id: Int,
    val name: String,
    val picturePaths: PicturePaths
)