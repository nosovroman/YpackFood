package com.example.ypackfood.dataClasses.commonData

data class BasePortion(
    val id: Int,
    val oldPrice: OldPrice?,
    val priceNow: PriceNow,
    val size: String?
)