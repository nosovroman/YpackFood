package com.example.ypackfood.viewModels

import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.models.orders.OrderMin.*
import com.example.ypackfood.sealedClasses.DeliveryOptions
import org.junit.Assert.*

import org.junit.Test

class OrderViewModelTest {
    private val orderViewModel = OrderViewModel()

    // only Delivery
    @Test
    fun composeOrder() {
        val dishId = 12
        val count = 2
        val portionId = 15
        val priceId = 10
        val portion = BasePortionMin(id = portionId, price = PriceNowMin(priceId))
        val dishMinList = listOf(
            CartDish(
                shoppingCartId = 1,
                dishId = dishId,
                name = "Салат Цезарь",
                portionId = portionId,
                priceId = priceId,
                price = 320,
                count = count,
                category = "Салаты",
                composition = "Салат Айсберг, куриная грудка, помидорки, перепелиные яйца, соус Цезарь",
                urlPicture = "https://pictures/cesar.com"
            )
        )
        val addressMerged = "г. Каменск-Шахтинский, пер. Коммунистический, д. 106"
        val totalCost = 640
        val dishesMin = listOf(
            DishMin(
                id = dishId,
                count = count,
                portion = portion
            )
        )

        val orderMin = orderViewModel.composeOrder(
            dishMinList = dishMinList,
            addressMerged = addressMerged,
            totalCost = totalCost,
        )

        val wishedOrder = OrderMin(
            deliveryTime = "00:00",
            totalPrice = totalCost,
            address = AddressMin(address = addressMerged),
            dishes = dishesMin,
            wayToGet = "DELIVERY"
        )

        assertTrue(orderMin == wishedOrder)
    }

    @Test
    fun setHoursField() {
        orderViewModel.setHoursField("0")
        val result1 = orderViewModel.hoursFieldState
        orderViewModel.setHoursField("24")
        val result2 = orderViewModel.hoursFieldState
        orderViewModel.setHoursField("10")
        val result3 = orderViewModel.hoursFieldState
        orderViewModel.setHoursField("05")
        val result4 = orderViewModel.hoursFieldState

        assertTrue(
            result1 == "0" &&
            result2 == "23" &&
            result3 == "10" &&
            result4 == "05"
        )
    }

    @Test
    fun setMinutesField() {
        orderViewModel.setMinutesField("0")
        val result1 = orderViewModel.minutesFieldState
        orderViewModel.setMinutesField("60")
        val result2 = orderViewModel.minutesFieldState
        orderViewModel.setMinutesField("10")
        val result3 = orderViewModel.minutesFieldState
        orderViewModel.setMinutesField("05")
        val result4 = orderViewModel.minutesFieldState

        assertTrue(
            result1 == "0" &&
            result2 == "59" &&
            result3 == "10" &&
            result4 == "05"
        )
    }

    @Test
    fun tryConfirmTime() {
        orderViewModel.setHoursField("15")
        orderViewModel.setMinutesField("5")

        val wishHourString = "15"
        val wishMinuteString = "05"
        val wishErrorEnteringTime = ""
        val wishTimeDialog = false

        orderViewModel.tryConfirmTime()

        val resultHour = wishHourString == orderViewModel.hourState
        val resultMinute = wishMinuteString == orderViewModel.minuteState
        val resultError = wishErrorEnteringTime == orderViewModel.errorEnteringTime
        val resultTimeDialog = wishTimeDialog == orderViewModel.timeDialogState

        assertTrue(resultHour && resultMinute && resultError && resultTimeDialog)
    }

    @Test
    fun convertToMinutes() {
        val minutes = orderViewModel.convertToMinutes(1, 30)
        val wishedMinutes = 90
        val minutes2 = orderViewModel.convertToMinutes(0, 30)
        val wishedMinutes2 = 30

        val result1 = minutes == wishedMinutes
        val result2 = minutes2 == wishedMinutes2

        assertTrue(result1 && result2)
    }

    @Test
    fun convertToHours() {
        val hours = orderViewModel.convertToHours(90)
        val wishedHours = Pair(1, 30)
        val hours2 = orderViewModel.convertToHours(30)
        val wishedHours2 = Pair(0, 30)

        val result1 = hours == wishedHours
        val result2 = hours2 == wishedHours2

        assertTrue(result1 && result2)
    }

    @Test
    fun computeMinTimeForOrder() {
        val minTime = orderViewModel.computeMinTimeForOrder(10, 15)
        val wishedTime = 660

        assertTrue(minTime == wishedTime)
    }
}