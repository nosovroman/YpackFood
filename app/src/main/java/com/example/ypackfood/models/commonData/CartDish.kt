package com.example.ypackfood.models.commonData

data class CartDish(
    val shoppingCartId: Int,
    val dishId: Int,
    val name: String,
    val portionId: Int,
    val portionSize: String = "",
    val priceId: Int,
    val price: Int,
    val count: Int,
    val category: String,
    val composition: String,
    val urlPicture: String,

    val changedPrice: Boolean = false
)