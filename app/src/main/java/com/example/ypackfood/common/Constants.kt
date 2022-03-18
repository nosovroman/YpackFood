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
    val baseUrlPictureCategory = "https://sun9-26.userapi.com/impf/c849020/v849020562/12056a/xOiO0cHdtkk.jpg?size=604x604&quality=96&sign=2c11f0e48c64e290d0bde943da845fd6&type=album"
    val baseUrlPictureContent = "https://i09.fotocdn.net/s116/8d62d46b0c71620f/public_pin_l/2635032180.jpg"
}