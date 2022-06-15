package com.example.ypackfood.enumClasses

import com.example.ypackfood.R
import com.example.ypackfood.enumClasses.MainDrawer.*
import com.example.ypackfood.sealedClasses.Screens

enum class MainDrawer(val itemName: String, val icon: Int, val route: String) {
    ORDERS("Мои заказы", R.drawable.ic_history, Screens.History.route),
    FAVORITES("Избранное", R.drawable.ic_favorite, Screens.Favorites.route),
    PROFILE("Профиль", R.drawable.ic_profile, Screens.Profile.route),
    INFO("О нас", R.drawable.ic_info, Screens.Info.route),
    EXIT("Выйти", R.drawable.ic_exit, Screens.Main.route),
}

fun getDrawerItems():  List<MainDrawer> {
    return listOf(ORDERS, FAVORITES, PROFILE, INFO, EXIT)
}