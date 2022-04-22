package com.example.ypackfood.models.detailAction

import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.models.commonData.PicturePaths

data class DetailAction(
    val dishes: List<Dish>,
    val id: Int,
    val name: String,
    val picturePaths: PicturePaths
)