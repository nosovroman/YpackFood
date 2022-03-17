package com.example.ypackfood.common

import androidx.compose.ui.unit.dp

object Constants {
    val pizza = listOf("Пицца1", "Пицца2", "Пицца3")
    val roll = listOf("Ролл1", "Ролл2", "Ролл3")
    val salads = listOf("Салат1", "Салат2", "Салат3")
    val soups =  listOf("Суп1", "Суп2", "Суп3")
    val drink = listOf("Напиток1", "Напиток2", "Напиток3", "Напиток4")
    val mergedList = pizza+roll+salads+soups+drink

    val TOOLBAR_HEIGHT = 48.dp
    const val BASE_URL = "https://bironix17-test-connect.herokuapp.com/"
}