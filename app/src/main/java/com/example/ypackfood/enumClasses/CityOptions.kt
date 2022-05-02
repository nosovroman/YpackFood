package com.example.ypackfood.enumClasses

import com.example.ypackfood.enumClasses.CityOptions.*

enum class CityOptions(val cityTitle: String) {
    KAMENSK("Каменск-Шахтинский"),
    STANICA("Старая Станица")
}

fun getCityNames():  List<String> {
    return listOf(KAMENSK.cityTitle, STANICA.cityTitle)
}