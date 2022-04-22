package com.example.ypackfood.models.detailContent

import com.example.ypackfood.models.commonData.Price

data class Addon(
    val id: Int,
    val name: String,
    val picturePath: String,
    val price: Price
)