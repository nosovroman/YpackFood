package com.example.ypackfood.dataClasses.detailAction

import com.example.ypackfood.dataClasses.commonData.Dishe
import com.example.ypackfood.dataClasses.detailContent.PicturePathsX

data class DetailAction(
    val dishes: List<Dishe>,
    val id: Int,
    val name: String,
    val picturePaths: PicturePathsX
)