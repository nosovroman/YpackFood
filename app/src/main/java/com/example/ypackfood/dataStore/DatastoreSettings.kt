package com.example.ypackfood.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore

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