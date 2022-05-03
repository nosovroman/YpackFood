package com.example.ypackfood.enumClasses

import com.example.ypackfood.R
import com.example.ypackfood.enumClasses.MainDrawer.*
import com.example.ypackfood.sealedClasses.Screens

enum class MainDrawer(val itemName: String, val icon: Int, val route: String) {
    MENU("Меню", R.drawable.ic_menu, Screens.Main.route),
    ORDERS("Мои заказы", R.drawable.ic_orders, Screens.History.route),
    FAVORITES("Избранное", R.drawable.ic_favorite, Screens.Favorites.route),
    PROFILE("Профиль", R.drawable.ic_profile, Screens.Profile.route),
    INFO("Инфо", R.drawable.ic_info, Screens.Main.route),
    EXIT("Выйти", R.drawable.ic_exit, Screens.Main.route),
}

fun getDrawerItems():  List<MainDrawer> {
    return listOf(MENU, ORDERS, FAVORITES, PROFILE, INFO, EXIT)
}