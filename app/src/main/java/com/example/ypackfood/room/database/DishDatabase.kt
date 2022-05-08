package com.example.ypackfood.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ypackfood.common.Constants
import com.example.ypackfood.room.dao.CartDao
import com.example.ypackfood.room.entities.CartEntity

@Database(entities = [CartEntity::class], version = 5)
abstract class DishDatabase : RoomDatabase() {
    //abstract fun favoritesDao(): FavoritesDao
    abstract fun shoppingCartDao(): CartDao

    companion object {
        private var INSTANCE_FAV: DishDatabase? = null
        fun getFavoritesInstance(context: Context): DishDatabase {
            synchronized(this) {
                var instance = INSTANCE_FAV
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DishDatabase::class.java,
                        Constants.ROOM_FAVORITES
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE_FAV = instance
                }
                return instance
            }
        }
    }
}