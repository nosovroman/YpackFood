package com.example.ypackfood.viewModels

import com.example.ypackfood.common.Auth
import com.example.ypackfood.models.commonData.*
import com.example.ypackfood.room.entities.CartEntity
import org.junit.Assert.*

import org.junit.Test

class ShoppingCartViewModelTest {
    private val cartViewModel = ShoppingCartViewModel()

    val dishId = 14
    val dishCount = 5
    val portionId = 10
    val dishPriceId = 12
    val dishPrice = 100
    val userId = Auth.authInfo.personId
    val shoppingCartId = 1
    val name = "Салат Цезарь"
    val category = "Салаты"
    val urlPicture = "https://pictures/cesar.com"
    val composition = "Салат Айсберг, куриная грудка, помидорки, перепелиные яйца, соус Цезарь"

    val listOfDish = listOf(
        Dish(
            id = dishId,
            picturePaths = PicturePaths(large = urlPicture),
            name = name,
            composition = composition,
            category = category,
            deleted = false,
            basePortion = BasePortion(
                id = portionId,
                priceNow = Price(dishPriceId, dishPrice)
            )
        )
    )
    val cartEntity = CartEntity(
        dishId = dishId,
        userId = userId,
        dishCount = dishCount,
        portionId = portionId,
        dishPriceId = dishPriceId,
        dishPrice = dishPrice,
        shoppingCartId = shoppingCartId
    )

    // ---
    @Test
    fun composeDishInfo() {
        val cartDish = CartDish(
            shoppingCartId = shoppingCartId,
            dishId = dishId,
            name = name,
            portionId = portionId,
            priceId = dishPriceId,
            price = dishPrice,
            count = dishCount,
            category = category,
            composition = composition,
            urlPicture = urlPicture,
            changedPrice = true
        )

        val cartEntity = CartEntity(
            dishId = dishId,
            userId = userId,
            dishCount = dishCount,
            portionId = portionId,
            dishPriceId = dishPriceId,
            dishPrice = dishPrice,
            shoppingCartId = shoppingCartId
        )

        val wishedList = listOf(cartDish, cartDish.copy(shoppingCartId = shoppingCartId+1))
        val listOfCart = listOf(cartEntity, cartEntity.copy(shoppingCartId = shoppingCartId+1))

        cartViewModel.composeDishInfo(listOfDish, listOfCart)
        val result = cartViewModel.resultDishState

        assertTrue(result == wishedList)
    }

    @Test
    fun computeTotalPrice() {
        val listOfCart = listOf(cartEntity, cartEntity.copy(shoppingCartId = shoppingCartId+1))
        cartViewModel.setDishesRoom(listOfCart)
        cartViewModel.computeTotalPrice()
        val totalPrice = cartViewModel.totalPriceState
        val wishedPrice = 1000
        assertTrue(totalPrice == wishedPrice)
    }
}