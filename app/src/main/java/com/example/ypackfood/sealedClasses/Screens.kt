package com.example.ypackfood.sealedClasses

sealed class Screens(val route: String) {
    object Main: Screens(route = "MainScreen")
    object SignInUp: Screens(route = "SignInUpScreen")
    object Offers: Screens(route = "OffersScreen")
    object ShoppingCart: Screens(route = "ShoppingCartScreen")
}
