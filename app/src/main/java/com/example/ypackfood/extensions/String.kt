package com.example.ypackfood.extensions

fun String.isDigitsOnly() = all(Char::isDigit) && isNotEmpty()