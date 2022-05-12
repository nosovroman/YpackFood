package com.example.ypackfood.viewModels

import com.example.ypackfood.common.Components
import com.example.ypackfood.room.entities.CartEntity
import org.junit.Assert.*

import org.junit.Test

class MainViewModelTest {
    private val viewModel: DetailViewModel = DetailViewModel()

    @Test
    fun getRealCurrentIcon_Test() {
        val contentId1 = 5
        val contentId2 = 10
        val listOfFavorites = mutableListOf(1, 2, 3, 5, 8, 11)
        viewModel.setCurrentIcon(Components.defaultIcon)

        val filledIcon = viewModel.getRealCurrentIcon(
            contentId = contentId1,
            listOfFavorites = listOfFavorites
        )

        val outlinedIcon = viewModel.getRealCurrentIcon(
            contentId = contentId2,
            listOfFavorites = listOfFavorites
        )

        assertTrue(
            filledIcon == Components.filledFavoriteIcon &&
            outlinedIcon == Components.outlinedFavoriteIcon
        )
    }

    @Test
    fun buildDishInfo_Test() {
        val dishId = 1
        val portionId = 10
        val priceId = 13
        val price = 1500
        val count = 5


        val composedCart = viewModel.buildDishInfo(
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


}