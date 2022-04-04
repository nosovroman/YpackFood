package com.example.ypackfood.dataClasses.detailContent

import com.example.ypackfood.dataClasses.commonData.Dishe

data class DetailContent(
    val addons: Any,
    val allergens: String,
    val baseIndexPortion: Int,
    val category: String,
    val composition: String,
    val description: String,
    val dishes: List<Dishe>,
    val id: Int,
    val name: String,
    val picturePaths: PicturePathsX,
    val portions: List<Portion>
)