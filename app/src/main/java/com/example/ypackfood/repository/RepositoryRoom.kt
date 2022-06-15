package com.example.ypackfood.repository

import androidx.lifecycle.LiveData
import com.example.ypackfood.common.Auth
import com.example.ypackfood.room.dao.CartDao
import com.example.ypackfood.room.entities.CartEntity

class RepositoryRoom(private val cartDao: CartDao) {
    var shopList: LiveData<List<CartEntity>> = cartDao.getShoppingCart()

    // ------------------ ShoppingCart
    suspend fun addToCart(cartEntity: CartEntity) {
        return cartDao.addToCart(cartEntity)
    }

    suspend fun addToCartMany(cartEntityList: List<CartEntity>) {
        return cartDao.addToCartMany(cartEntityList)
    }

    suspend fun updateCart(cartEntity: CartEntity) {
        return cartDao.updateCart(cartEntity)
    }

    suspend fun deleteFromCart(cartEntity: CartEntity) {
        return cartDao.deleteFromCart(cartEntity)
    }

    suspend fun deleteFromCartByListId(userId: Int, ids: List<Int>) {
        return cartDao.deleteFromCartByListId(ids)
    }

    suspend fun deleteAllFromCart(userId: Int) {
        return cartDao.deleteAllFromCart(userId)
    }
}