package com.example.ypackfood.dataClasses.mainContent2

//import com.example.ypackfood.dataClasses.mainContent.Dishe

data class Dishe(
    val basePortion: BasePortion,
    val category: String,
    val composition: String,
    val dishes: List<Dishe>? = null,
    val id: Int,
    val name: String,
    val picturePaths: PicturePaths
)