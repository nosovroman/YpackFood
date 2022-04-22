package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.ypackfood.components.*
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun FavoritesScreen(navController: NavHostController, roomViewModel: RoomViewModel) {

    val favorites = roomViewModel.favorites.observeAsState(listOf()).value

    Scaffold (
        topBar = {
            ToolbarComponent(navController = navController)
        },
        content = {
//            if (!detailViewModel.contentResp.value?.data?.name.isNullOrEmpty()) {
//                Log.d("networkAnswer", "Display data")
//
//            }

//            RequestStateComponent(
//                requestState = requestState,
//                byError = {
//                    ShowErrorComponent(onButtonClick = { detailViewModel.getDetailContent(contentId) })
//                }
//            )
        }
    )
}