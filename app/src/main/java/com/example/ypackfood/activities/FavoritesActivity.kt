package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.viewModels.FavoritesViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun FavoritesScreen(navController: NavHostController, favoritesViewModel: FavoritesViewModel, roomViewModel: RoomViewModel) {

    val favorites = roomViewModel.favorites.observeAsState(listOf()).value
    val requestState = favoritesViewModel.contentResp.observeAsState().value

    LaunchedEffect(favorites) {
        if (favorites.isNotEmpty()) {
            favoritesViewModel.getContentByListId(favorites)
        }
    }

    Scaffold (
        topBar = {
            ToolbarComponent(navController = navController)
        },
        content = {
            Column {
//                Text("Избранное")
//                if (!favorites.isNullOrEmpty()) {
//                    Log.d("networkAnswer", "Display data")
//                    Text(favorites.toString())
//                }
//                else {
//                    Text("Загрузка... или избранных нет :)")
//                }
                if (!requestState?.data.isNullOrEmpty()) {
                    Log.d("networkAnswer", "Display data")
                    ContentSimpleListComponent(contentList = requestState!!.data!! as List<Dish>)
                }

                RequestStateComponent(
                    requestState = requestState,
                    byError = {
                        ShowErrorComponent(onButtonClick = { favoritesViewModel.getContentByListId(favorites) })
                    }
                )
            }
        }
    )
}