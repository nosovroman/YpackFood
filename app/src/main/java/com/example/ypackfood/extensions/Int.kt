package com.example.ypackfood.extensions

fun Int.toTimeString(): String {
    return if (this < 10) "0$this" else "$this"
}