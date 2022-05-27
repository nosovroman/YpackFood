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

        assertTrue("existing element not found",filledIcon == Components.filledFavoriteIcon)
        assertFalse("found not existing element",outlinedIcon != Components.outlinedFavoriteIcon)
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