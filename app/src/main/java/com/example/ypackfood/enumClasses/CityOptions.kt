package com.example.ypackfood.enumClasses

import com.example.ypackfood.enumClasses.CityOptions.*

//sealed class CityOptions(title: String) {
//    class DELIVERY(override val title: String = "Доставка") : DeliveryOptions(title)
//    class PICKUP(override val title: String = "Самовывоз") : DeliveryOptions(title)
//
//    companion object {
//        fun getOptions(): List<DeliveryOptions> {
//            return listOf(DELIVERY(), PICKUP())
//        }
//    }
//}

enum class CityOptions(val cityName: String) {
    KAMENSK("Каменск-Шахтинский"),
    STANICA("Старая станица")
}

fun getCityNames():  List<String> {
    return listOf(KAMENSK.cityName, STANICA.cityName)
}