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
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.FavoritesViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun FavoritesScreen(navController: NavHostController, favoritesViewModel: FavoritesViewModel, roomViewModel: RoomViewModel) {

    val favoritesState = favoritesViewModel.favoritesState.observeAsState().value

    LaunchedEffect(true) {
        favoritesViewModel.getFavorites()
    }

    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Favorites.title) },
        content = {
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                when (favoritesState) {
                    is NetworkResult.Success<*> -> {
                        Log.d("networkAnswer", "Display data")
                        ContentSimpleListComponent(
                            contentList = favoritesState.data!! as List<Dish>,
                            showPrice = true,
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
                    is NetworkResult.Loading<*> -> {
                        Column {
                            Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                            LoadingBarComponent()
                        }
                    }
                    else -> {}
                }
            }
        }
    )
}