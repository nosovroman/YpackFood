package com.example.ypackfood.enumClasses

import com.example.ypackfood.enumClasses.PaymentOptions.COURIER_CARD

enum class PaymentOptions(val paymentTitle: String) {
    READY_MONEY("Наличными"),
    COURIER_CARD("Картой курьеру")
}

fun getPaymentOptions():  List<String> {
    return listOf(PaymentOptions.READY_MONEY.paymentTitle, COURIER_CARD.paymentTitle)
}