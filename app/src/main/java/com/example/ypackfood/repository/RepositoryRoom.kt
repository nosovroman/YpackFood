package com.example.ypackfood.repository

import androidx.lifecycle.LiveData
import com.example.ypackfood.room.dao.FavoritesDao
import com.example.ypackfood.room.entities.FavoritesEntity

class RepositoryRoom(private val favoritesDao: FavoritesDao) {
    val favorites: LiveData<List<Int>> = favoritesDao.getFavorites()

    suspend fun checkFavoriteById(favoriteId: Int): Boolean {
        return favoritesDao.checkFavoriteById(favoriteId)// ?: false
    }

    suspend fun addFavorite(favoriteId: Int) {
        return favoritesDao.addFavorite(FavoritesEntity(favoriteId))
    }

    suspend fun deleteFavorite(favoriteId: Int) {
        return favoritesDao.deleteFavorite(FavoritesEntity(favoriteId))
    }
}