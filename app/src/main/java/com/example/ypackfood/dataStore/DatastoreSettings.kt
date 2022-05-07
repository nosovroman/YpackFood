package com.example.ypackfood.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ypackfood.room.dao.FavoritesDao
import com.example.ypackfood.room.dao.CartDao
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.room.entities.FavoritesEntity

abstract class DataSettings {
    companion object {
        private var INSTANCE: DataStore<Preferences>? = null
        fun getInstance(context: Context): DataStore<Preferences> {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = context.createDataStore(name = "settings")
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}