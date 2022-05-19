package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.FavoritesViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun InfoScreen(
    navController: NavHostController
) {

    //val favoritesState = favoritesViewModel.favoritesState.observeAsState().value

//    LaunchedEffect(true) {
//        favoritesViewModel.getFavorites()
//    }

    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Info.title) },
        content = {
            Column(modifier = Modifier.padding(25.dp)) {

                val titleSize = 20.sp
                val contentSize = 16.sp
                val space = 30.dp

                Text(text = "Описание", fontSize = titleSize)
                Text(text = "Мы самые лучший ресторан на свете", fontSize = contentSize, color = Color.Gray)

                Spacer(modifier = Modifier.height(space))

                Text(text = "Наши контакты", fontSize = titleSize)
                Text(text = "+7 (86365) 445-48-25", fontSize = contentSize, color = Color.Gray)

                Spacer(modifier = Modifier.height(space))

                Text(text = "Адрес", fontSize = titleSize)
                Text(text = "Старая Станица, парк Лога", fontSize = contentSize, color = Color.Gray)

                Spacer(modifier = Modifier.height(space))

                Text(text = "Политика конфиденциальности", fontSize = contentSize, color = Color.Blue)
                Text(text = "Пользовательское соглашение", fontSize = contentSize, color = Color.Blue)

                Spacer(modifier = Modifier.height(space))

                Text(text = "Разработчики:", fontSize = titleSize)
                Text(text = "Андрияш Дмитрий (bironix@gmail.com)\r\nНосов Роман (revenger13@gmail.com)", fontSize = contentSize, color = Color.Gray)
            }
        }
    )
}