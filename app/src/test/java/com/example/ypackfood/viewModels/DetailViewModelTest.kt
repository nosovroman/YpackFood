package com.example.ypackfood.viewModels

import com.example.ypackfood.common.Components
import com.example.ypackfood.room.entities.CartEntity
import org.junit.Assert.*

import org.junit.Test

class DetailViewModelTest {
    private val detailViewModel = DetailViewModel()

    @Test
    fun getRealCurrentIcon() {
        val contentId1 = 5
        val contentId2 = 10
        val listOfFavorites = mutableListOf(1, 2, 3, 5, 8, 11)
        detailViewModel.setCurrentIcon(Components.defaultIcon)

        val filledIcon = detailViewModel.getRealCurrentIcon(
            contentId = contentId1,
            listOfFavorites = listOfFavorites
        )

        val outlinedIcon = detailViewModel.getRealCurrentIcon(
            contentId = contentId2,
            listOfFavorites = listOfFavorites
        )

        assertTrue(
            filledIcon == Components.filledFavoriteIcon &&
            outlinedIcon == Components.outlinedFavoriteIcon
        )
    }

    @Test
    fun buildDishInfo() {
        val dishId = 1
        val portionId = 10
        val priceId = 13
        val price = 1500
        val count = 5


        val composedCart = detailViewModel.buildDishInfo(
            dishId = dishId,
            portionId = portionId,
            priceId = priceId,
            price = price,
            count = count
        )

        val wishedCart = CartEntity(
            dishId = dishId,
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count
        )

        assertTrue(composedCart == wishedCart)
    }

    @Test
    fun incrementCounter() {
        val incremented = 2
        detailViewModel.initCountWish()

        detailViewModel.incrementCounter()
        assertTrue(detailViewModel.countWishDishes == incremented)
    }

    @Test
    fun decrementCounter() {
        val decremented = 0
        detailViewModel.initCountWish()

        detailViewModel.decrementCounter()
        assertTrue(detailViewModel.countWishDishes == decremented)
    }
}