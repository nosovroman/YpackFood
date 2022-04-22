package com.example.ypackfood.models.commonData

data class BasePortion(
    val id: Int,
    val oldPrice: Price?,
    val priceNow: Price,
    val size: String?
)