package com.example.ypackfood.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ypackfood.room.entities.FavoritesEntity

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM FavoritesEntity")
    fun getFavorites(): LiveData<List<Int>>

    @Query("SELECT * FROM FavoritesEntity WHERE favoriteId = :favoriteId LIMIT 1")
    suspend fun checkFavoriteById(favoriteId: Int): Boolean//?

    @Insert
    suspend fun addFavorite(favoritesEntity: FavoritesEntity)

    @Delete
    suspend fun deleteFavorite(favoritesEntity: FavoritesEntity)
}