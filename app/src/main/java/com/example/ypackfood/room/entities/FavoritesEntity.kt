package com.example.ypackfood.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoritesEntity(
    @PrimaryKey val favoriteId: Int
)