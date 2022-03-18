package com.example.ypackfood.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.ui.theme.YpackFoodTheme
import com.example.ypackfood.viewModels.MainViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel

class NavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val shoppingCartViewModel = ViewModelProvider(this).get(ShoppingCartViewModel::class.java)

        setContent {
            YpackFoodTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screens.ShoppingCart.route
                ) {
                    composable(route = Screens.Main.route) { MainScreen(navController, mainViewModel) }
                    composable(route = Screens.SignInUp.route) { SignInUpScreen() }
                    composable(route = Screens.Offers.route) { OffersScreen() }
                    composable(route = Screens.ShoppingCart.route) { ShoppingCartScreen(shoppingCartViewModel) }
                }
            }
        }
    }
}

//@Composable
//fun Navigator() {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = Screens.Main.route
//    ) {
//        composable(route = Screens.Main.route) { MainScreen(navController, mainViewModel) }
//        composable(route = Screens.SignInUp.route) { SignInUpScreen() }
//        composable(route = Screens.Offers.route) { OffersScreen() }
//    }
//}