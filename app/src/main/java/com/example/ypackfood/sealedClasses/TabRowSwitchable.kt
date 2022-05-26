package com.example.ypackfood.sealedClasses

interface TabRowSwitchable {
    val title: String
    val index: Int
    fun getByIndex(index: Int): TabRowSwitchable {
        return DeliveryOptions.DELIVERY()
    }
}