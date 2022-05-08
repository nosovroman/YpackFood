package com.example.ypackfood.repository

import androidx.lifecycle.LiveData
import com.example.ypackfood.room.dao.CartDao
import com.example.ypackfood.room.entities.CartEntity

class RepositoryRoom(private val cartDao: CartDao) {
    val shopList: LiveData<List<CartEntity>> = cartDao.getShoppingCart()


    // ------------------ ShoppingCart
    suspend fun addToCart(cartEntity: CartEntity) {
        return cartDao.addToCart(cartEntity)
    }

    suspend fun updateCart(cartEntity: CartEntity) {
        return cartDao.updateCart(cartEntity)
    }

    suspend fun deleteFromCart(cartEntity: CartEntity) {
        return cartDao.deleteFromCart(cartEntity)
    }

    suspend fun deleteFromCartByListId(ids: List<Int>) {
        return cartDao.deleteFromCartByListId(ids)
    }

    suspend fun deleteAllFromCart() {
        return cartDao.deleteAllFromCart()
    }
}