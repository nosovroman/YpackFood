package com.example.ypackfood.viewModels

import com.example.ypackfood.common.Components
import com.example.ypackfood.models.commonData.BasePortion
import com.example.ypackfood.models.commonData.PicturePaths
import com.example.ypackfood.models.commonData.Price
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.models.detailContent.Portion
import com.example.ypackfood.room.entities.CartEntity
import org.junit.Assert.*

import org.junit.Test

class HistoryViewModelTest {
    private val historyViewModel = HistoryViewModel()

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
    val description = "Знаменитый на весь мир салат"

    val listOfDetail = listOf(
        DetailContent(
            allergens = "",
            basePortion = BasePortion(id=10, priceNow = Price(11, 450)),
            composition = composition,
            category = category,
            count = dishCount,
            deleted = false,
            description = description,
            dishes = mutableListOf(),
            id = dishId,
            name = name,
            picturePaths = PicturePaths(large = urlPicture),
            portions = listOf(Portion(id = portionId, price = dishPrice, size = "10"))
        )
    )

    @Test
    fun buildCartEntity_Test() {
        val result = historyViewModel.buildCartEntity(listOfDetail)

        val wishedList = listOf(
            CartEntity(
                dishId = dishId,
                dishCount = dishCount,
                portionId = portionId,
                dishPriceId = dishPriceId,
                dishPrice = dishPrice,
                shoppingCartId = shoppingCartId
            )
        )
        assertTrue(result == wishedList)
    }
}