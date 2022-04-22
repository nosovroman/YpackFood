package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
            Column() {
                Text("Избранное")
                if (!favorites.isNullOrEmpty()) {
                    Log.d("networkAnswer", "Display data")
                    Text(favorites.toString())
                }
                else {
                    Text("Загрузка... или избранных нет :)")
                }
            }
        }
    )
}