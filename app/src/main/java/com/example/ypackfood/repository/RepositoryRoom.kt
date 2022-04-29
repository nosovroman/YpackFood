package com.example.ypackfood.repository

import androidx.lifecycle.LiveData
import com.example.ypackfood.room.dao.FavoritesDao
import com.example.ypackfood.room.dao.CartDao
import com.example.ypackfood.room.entities.FavoritesEntity
import com.example.ypackfood.room.entities.CartEntity

class RepositoryRoom(private val favoritesDao: FavoritesDao, private val cartDao: CartDao) {
    val favorites: LiveData<List<Int>> = favoritesDao.getFavorites()
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


    // ------------------ Favorites
    suspend fun addFavorite(favoriteId: Int) {
        return favoritesDao.addFavorite(FavoritesEntity(favoriteId))
    }

    suspend fun deleteFavorite(favoriteId: Int) {
        return favoritesDao.deleteFavorite(FavoritesEntity(favoriteId))
    }
}

//suspend fun checkFavoriteById(favoriteId: Int): Boolean {
//    return favoritesDao.checkFavoriteById(favoriteId)// ?: false
//}