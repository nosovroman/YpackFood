package com.example.ypackfood.sealedClasses

import com.example.ypackfood.common.Constants

sealed class Screens(val route: String, val title: String = Constants.APP_NAME_RUS) {
    object Main: Screens(route = "MainScreen")
    object DetailContent: Screens(route = "DetailContentScreen/{${Constants.NAV_KEY__CONTENT_ID}}") {
        fun createRoute(contentId: Int) = this.route.replace(oldValue = "{${Constants.NAV_KEY__CONTENT_ID}}", newValue = contentId.toString())
    }
    object SignInUp: Screens(route = "SignInUpScreen")
    object Offers: Screens(route = "OffersScreen/{${Constants.NAV_KEY__OFFER_ID}}", title = "Акции") {
        fun createRoute(offerId: Int) = this.route.replace(oldValue = "{${Constants.NAV_KEY__OFFER_ID}}", newValue = offerId.toString())
    }
    object ShoppingCart: Screens(route = "ShoppingCartScreen", title = "Корзина")
    object History: Screens(route = "HistoryScreen", title = "История покупок")
    object Profile: Screens(route = "ProfileScreen", title = "Профиль")
    object Favorites: Screens(route = "FavoritesScreen", title = "Избранное")
}
