package com.example.ypackfood.viewModels

import com.example.ypackfood.common.Constants.MIN_TIME_ORDER
import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.models.orders.OrderMin.*
import com.example.ypackfood.models.orders.common.DishForOrderPost
import com.example.ypackfood.sealedClasses.DeliveryOptions
import org.junit.Assert.*

import org.junit.Test

class OrderViewModelTest {
    private val orderViewModel = OrderViewModel()

    //---
    @Test
    fun composeOrder() {
        val composition = "Салат Айсберг, куриная грудка, помидорки, перепелиные яйца, соус Цезарь"
        val urlPicture = "https://pictures/cesar.com"
        val category = "Салаты"
        val name = "Салат Цезарь"
        val dishId = 12
        val shoppingCartId = 1
        val count = 2
        val price = 320
        val portionId = 15
        val priceId = 10
        val portion = BasePortionMin(id = portionId, price = PriceMin(priceId))
        val addressMerged = "г. Каменск-Шахтинский, пер. Коммунистический, д. 106"
        val totalCost = price*count

        val dishMinList = listOf(
            CartDish(
                shoppingCartId = shoppingCartId,
                dishId = dishId,
                name = name,
                portionId = portionId,
                priceId = priceId,
                price = price,
                count = count,
                category = category,
                composition = composition,
                urlPicture = urlPicture
            )
        )

        val dishesMin = listOf(
            DishForOrderPost(
                count = count,
                dish = DishMin(id = dishId, portion = portion)
            )
        )

        val resultComposeDelivery = orderViewModel.composeOrder(
            dishMinList = dishMinList,
            addressMerged = addressMerged,
            totalCost = totalCost,
            deliveryState = DeliveryOptions.DELIVERY()
        )
        val wishedOrderDelivery = OrderMin(
            deliveryTime = "00:00",
            totalPrice = totalCost,
            address = AddressMin(address = addressMerged),
            dishes = dishesMin,
            wayToGet = "DELIVERY"
        )

        val resultComposePickup = orderViewModel.composeOrder(
            dishMinList = dishMinList,
            addressMerged = addressMerged,
            totalCost = totalCost,
            deliveryState = DeliveryOptions.PICKUP()
        )
        val wishedOrderPickup = OrderMin(
            deliveryTime = "00:00",
            totalPrice = totalCost,
            address = null,
            dishes = dishesMin,
            wayToGet = "PICKUP"
        )

        assertTrue("incorrect composing with Delivery", resultComposeDelivery == wishedOrderDelivery)
        assertTrue("incorrect composing with Pickup",resultComposePickup == wishedOrderPickup)
    }

    @Test
    fun setHoursField() {
        orderViewModel.setHoursField("23")
        assertTrue(orderViewModel.hoursFieldState == "23")
        orderViewModel.setHoursField("1")
        assertTrue(orderViewModel.hoursFieldState == "1")
        orderViewModel.setHoursField(".")
        assertTrue("must be only digits",orderViewModel.hoursFieldState == "1")
        orderViewModel.setHoursField("24")
        assertTrue("incorrect two-digit hour",orderViewModel.hoursFieldState == "23")
        orderViewModel.setHoursField("05")
        assertTrue(orderViewModel.hoursFieldState == "05")
        orderViewModel.setHoursField("120")
        assertTrue("incorrect three-digit hour",orderViewModel.hoursFieldState == "05")
    }

    @Test
    fun setMinutesField() {
        orderViewModel.setMinutesField("59")
        assertTrue(orderViewModel.minutesFieldState == "59")
        orderViewModel.setHoursField(".")
        assertTrue("must be only digits",orderViewModel.minutesFieldState == "59")
        orderViewModel.setMinutesField("0")
        assertTrue(orderViewModel.minutesFieldState == "0")
        orderViewModel.setMinutesField("60")
        assertTrue("incorrect two-digit minute",orderViewModel.minutesFieldState == "59")
        orderViewModel.setMinutesField("05")
        assertTrue(orderViewModel.minutesFieldState == "05")
        orderViewModel.setMinutesField("120")
        assertTrue("incorrect three-digit minute",orderViewModel.minutesFieldState == "05")
    }

    // ---
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
        var minutes = orderViewModel.convertToMinutes(1, 30)
        assertTrue("incorrect computing hour", minutes == 90)
        minutes = orderViewModel.convertToMinutes(0, 33)
        assertTrue("incorrect computing minutes with hour = 0",minutes == 33)
        minutes = orderViewModel.convertToMinutes(2, 0)
        assertTrue("incorrect computing minutes with minute = 0",minutes == 120)
    }

    @Test
    fun convertToHours() {
        var hours = orderViewModel.convertToHourMinute(90)
        assertTrue("incorrect computing with hour = 90", hours == Pair(1, 30))
        hours = orderViewModel.convertToHourMinute(33)
        assertTrue("incorrect computing with hour = 33", hours == Pair(0, 33))
        hours = orderViewModel.convertToHourMinute(120)
        assertTrue("incorrect computing with hour = 120", hours == Pair(2, 0))
    }

    @Test
    fun computeMinTimeForOrder() {
        val minTime = orderViewModel.computeMinTimeForOrder(10, 15)
        assertTrue(minTime == orderViewModel.convertToMinutes(10, 15) + MIN_TIME_ORDER)
    }
}