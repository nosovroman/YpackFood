package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel,
    datastoreViewModel: DatastoreViewModel
) {

    val favoritesState = favoritesViewModel.favoritesState.observeAsState().value

    LaunchedEffect(true) {
        favoritesViewModel.initStates()
        favoritesViewModel.getFavorites()
    }

    val refreshState = favoritesViewModel.refreshState.observeAsState().value
    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                favoritesViewModel.getFavorites()
            }
            is NetworkResult.HandledError<*> -> {
                Log.d("TokenRefresh HandledError ", refreshState.message.toString())
                navController.navigate(route = Screens.SignInUp.route) {
                    popUpTo(Screens.Favorites.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(favoritesState) {
        when (favoritesState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = favoritesState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        favoritesViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("TokenRefresh detailDishState Logout", errorCode)
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.Favorites.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Favorites.title) },
        content = {
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                when(refreshState) {
                    is NetworkResult.Error<*> -> {
                        ShowErrorComponent(message = refreshState.message, onButtonClick = { favoritesViewModel.getFavorites() })
                    }
                    else -> {}
                }
                when (favoritesState) {
                    is NetworkResult.Loading<*> -> {
                        Column {
                            Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                            LoadingBarComponent()
                        }
                    }
                    is NetworkResult.Success<*> -> {
                        Log.d("networkAnswer", "Display data")
                        SimpleListComponent(
                            contentList = favoritesState.data as List<Dish>,
                            //showPrice = true,
                            onItemClick = { id -> navController.navigate(route = Screens.DetailContent.createRoute(contentId = id)) }
                        )
                    }
                    is NetworkResult.Empty<*> -> {
                        EmptyContentComponent(message = "У Вас пока что нет любимых блюд")
                    }
                    is NetworkResult.Error<*> -> {
                        ShowErrorComponent(
                            message = favoritesState.message,
                            onButtonClick = { favoritesViewModel.getFavorites() }
                        )
                    }
                    else -> {}
                }
            }
        }
    )
}