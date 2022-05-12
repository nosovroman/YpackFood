package com.example.ypackfood.viewModels

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

    val listOfCart = listOf(
        CartEntity(
            dishId = dishId,
            dishCount = dishCount,
            portionId = portionId,
            dishPriceId = dishPriceId,
            dishPrice = dishPrice,
            shoppingCartId = shoppingCartId
        ),
        CartEntity(
            dishId = dishId,
            dishCount = dishCount,
            portionId = portionId,
            dishPriceId = dishPriceId,
            dishPrice = dishPrice,
            shoppingCartId = shoppingCartId+1
        )
    )

    // changedPrice
    @Test
    fun composeDishInfo() {
        cartViewModel.composeDishInfo(listOfDish, listOfCart)
        val result = cartViewModel.resultDishState

        val wishedList = listOf(
            CartDish(
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
                addons = null,
                changedPrice = true
            ),
            CartDish(
                shoppingCartId = shoppingCartId+1,
                dishId = dishId,
                name = name,
                portionId = portionId,
                priceId = dishPriceId,
                price = dishPrice,
                count = dishCount,
                category = category,
                composition = composition,
                urlPicture = urlPicture,
                addons = null,
                changedPrice = true
            )
        )

        assertTrue(result == wishedList)
    }

    @Test
    fun computeTotalPrice() {
        cartViewModel.setDishesRoom(listOfCart)
        cartViewModel.computeTotalPrice()
        val totalPrice = cartViewModel.totalPriceState
        val wishedPrice = 1000
        assertTrue(totalPrice == wishedPrice)
    }
}