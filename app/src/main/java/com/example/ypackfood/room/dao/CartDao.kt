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

    @Update
    suspend fun updateCart(cartEntity: CartEntity)

    @Delete
    suspend fun deleteFromCart(cartEntity: CartEntity)

    @Query("DELETE FROM CartEntity WHERE dish_id IN (:ids)")
    suspend fun deleteFromCartByListId(ids: List<Int>)
}

/*
insert into CartEntity values (13, 13, 14, 300, 2, null, 13),
(13, 13, 14, 300, 2, null, 14),
(13, 13, 14, 300, 2, null, 15)
*/
