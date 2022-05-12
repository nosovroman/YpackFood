package com.example.ypackfood.models.commonData

data class CartDish(
    val shoppingCartId: Int,
    val dishId: Int,
    val name: String,
    val portionId: Int,
    val priceId: Int,
    val price: Int,
    val count: Int,
    val category: String,
    val composition: String,
    val urlPicture: String,
    val addons: String? = null,

    val changedPrice: Boolean = false
)