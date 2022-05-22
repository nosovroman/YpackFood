package com.example.ypackfood.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.ui.theme.YpackFoodTheme
import com.example.ypackfood.viewModels.*

class NavActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val datastoreViewModel = ViewModelProvider(this).get(DatastoreViewModel::class.java)
        datastoreViewModel.getAuthInfo()

        val signViewModel = ViewModelProvider(this).get(SignInUpViewModel::class.java)
        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val offerViewModel = ViewModelProvider(this).get(OfferViewModel::class.java)
        val shoppingCartViewModel = ViewModelProvider(this).get(ShoppingCartViewModel::class.java)
        val orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        val historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        val favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)




        setContent {
            YpackFoodTheme(darkTheme = false) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screens.Main.route
                ) {
                    composable(route = Screens.SignInUp.route) { SignInUpScreen(navController, signViewModel, datastoreViewModel) }
                    composable(route = Screens.Main.route) { MainScreen(navController, mainViewModel, datastoreViewModel, roomViewModel) }
                    composable(
                        route = Screens.DetailContent.route,
                        arguments = listOf(
                            navArgument(Constants.NAV_KEY__CONTENT_ID) { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getInt(Constants.NAV_KEY__CONTENT_ID)?.let { contentId ->
                            DetailContentScreen(navController, detailViewModel, datastoreViewModel, roomViewModel, contentId)
                        }
                    }
                    composable(
                        route = Screens.Offers.route,
                        arguments = listOf(
                            navArgument(Constants.NAV_KEY__OFFER_ID) { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getInt(Constants.NAV_KEY__OFFER_ID)?.let { offerId ->
                            OffersScreen(navController, offerViewModel, offerId)
                        }
                    }
                    //composable(route = Screens.ShoppingCart.route) { ShoppingCartScreen(navController, shoppingCartViewModel, roomViewModel) }
                    composable(route = Screens.ShoppingCart.route) { ShoppingCartScreen(navController, shoppingCartViewModel, orderViewModel, roomViewModel) }
                    composable(
                        route = Screens.Order.route,
                        arguments = listOf(
                            navArgument(Constants.NAV_KEY__ORDER_COST) { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getInt(Constants.NAV_KEY__ORDER_COST)?.let { orderId ->
                            OrderScreen(navController, orderViewModel, shoppingCartViewModel, orderId)
                        }
                    }
                    composable(route = Screens.History.route) { HistoryScreen(navController, historyViewModel, datastoreViewModel, roomViewModel) }
                    composable(route = Screens.Profile.route) { ProfileScreen(navController, profileViewModel) }
                    composable(route = Screens.Favorites.route) { FavoritesScreen(navController, favoritesViewModel, datastoreViewModel, roomViewModel) }
                    composable(route = Screens.Info.route) { InfoScreen(navController) }
                }
            }
        }
    }
}
