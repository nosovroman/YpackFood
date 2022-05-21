package com.example.ypackfood.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ypackfood.room.entities.CartEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM CartEntity WHERE user_id = :userId")
    fun getShoppingCart(userId: Int): LiveData<List<CartEntity>>

    @Insert
    suspend fun addToCart(cartEntity: CartEntity)

    @Insert
    suspend fun addToCartMany(cartEntityList: List<CartEntity>)

    @Update
    suspend fun updateCart(cartEntity: CartEntity)

    @Delete
    suspend fun deleteFromCart(cartEntity: CartEntity)

    @Query("DELETE FROM CartEntity WHERE user_id = :userId AND dish_id IN (:ids)")
    suspend fun deleteFromCartByListId(userId: Int, ids: List<Int>)

    @Query("DELETE FROM CartEntity")
    suspend fun deleteAllFromCart()
}

/*
insert into CartEntity values (13, 13, 14, 300, 2, null, 13),
(13, 13, 14, 300, 2, null, 14),
(13, 13, 14, 300, 2, null, 15)
*/
