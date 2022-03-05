package com.example.ypackfood.sealedClasses

sealed class Screens(val route: String) {
    object Main: Screens(route = "MainScreen")
}
