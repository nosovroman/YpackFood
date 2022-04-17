package com.example.ypackfood.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ypackfood.common.Constants
import com.example.ypackfood.room.dao.FavoritesDao
import com.example.ypackfood.room.entities.FavoritesEntity

@Database(entities = [FavoritesEntity::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        private var INSTANCE: FavoritesDatabase? = null
        fun getInstance(context: Context): FavoritesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoritesDatabase::class.java,
                        Constants.ROOM_FAVORITES
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}