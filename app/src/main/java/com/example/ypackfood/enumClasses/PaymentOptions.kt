package com.example.ypackfood.enumClasses

import com.example.ypackfood.enumClasses.PaymentOptions.*

enum class PaymentOptions(val paymentTitle: String) {
    READY_MONEY("Наличными"),
    COURIER_CARD("Картой курьеру")
}

fun getPaymentOptions():  List<String> {
    return listOf(READY_MONEY.paymentTitle, COURIER_CARD.paymentTitle)
}