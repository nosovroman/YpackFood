package com.example.ypackfood.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ypackfood.room.entities.CartEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM CartEntity")
    fun getShoppingCart(): LiveData<List<CartEntity>>

    @Insert
    suspend fun addToCart(cartEntity: CartEntity)

//    @Update
//    suspend fun updateCart(cartEntity: CartEntity)

    @Delete
    suspend fun deleteFromCart(cartEntity: CartEntity)
}