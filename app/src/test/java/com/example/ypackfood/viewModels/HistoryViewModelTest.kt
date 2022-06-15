package com.example.ypackfood.viewModels

import com.example.ypackfood.common.Auth
import com.example.ypackfood.models.commonData.BasePortion
import com.example.ypackfood.models.commonData.PicturePaths
import com.example.ypackfood.models.commonData.Price
import com.example.ypackfood.models.orders.OrderFull.DishOrder
import com.example.ypackfood.models.orders.common.DishForOrderGet
import com.example.ypackfood.room.entities.CartEntity
import org.junit.Assert.assertTrue
import org.junit.Test

class HistoryViewModelTest {
    private val historyViewModel = HistoryViewModel()

    private val dishId = 14
    private val dishCount = 5
    private val portionId = 10
    private val dishPriceId = 12
    private val dishPrice = 100
    private val userId = Auth.authInfo.personId
    private val size = 10
    private val shoppingCartId = null
    private val name = "Салат Цезарь"
    private val category = "Салаты"
    private val urlPicture = "https://pictures/cesar.com"

    private val listOfDetail = listOf(
        DishForOrderGet(
            count = dishCount,
            dish = DishOrder(
                category = category,
                deleted = false,
                dishes = mutableListOf(),
                id = dishId,
                name = name,
                picturePaths = PicturePaths(large = urlPicture),
                portion = BasePortion(id = portionId, priceNow = Price(id = dishPriceId, price = dishPrice), size = size.toString())
            )
        )
    )

    @Test
    fun composeCartEntities() {
        val result = historyViewModel.composeCartEntities(listOfDetail)

        val wishedList = listOf(
            CartEntity(
                dishId = dishId,
                userId = userId,
                dishCount = dishCount,
                portionId = portionId,
                dishPriceId = dishPriceId,
                dishPrice = dishPrice,
                shoppingCartId = shoppingCartId
            )
        )
        assertTrue("unexpected built: $result\r\n$wishedList", result == wishedList)
    }
}