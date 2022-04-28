package com.example.ypackfood.models.commonData

data class CartDish(
    val id: Int,
    val name: String,
    val price: Int,
    val count: Int,
    val category: String,
    val composition: String,
    val urlPicture: String,
    val addons: String?,

    val changedPrice: Boolean = false
)