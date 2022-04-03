package com.example.ypackfood.activities


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ypackfood.components.BackFABComponent
import com.example.ypackfood.components.PictureTwoComponent

@Composable
fun DetailContentScreen(contentId: Int) {
    Log.d("params", "result=$contentId")

    val scrollState = rememberScrollState()
    Scaffold (
        floatingActionButton = { ShoppingCartButton() },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            Column (
                Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .verticalScroll(scrollState)
            ) {
                PictureTwoComponent()
                DetailDescription()
            }
            Row() {
                BackFABComponent()
                FavoriteIconButton()
            }
        }
    )
}

@Composable
fun ShoppingCartButton() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { Log.d("twer", "Счетчик") },
            content = { Text("Счетчик") }
        )
        Text("Стоимость")
        Button(
            onClick = { Log.d("twer", "Корзина") },
            content = { Text("В корзину") }
        )
    }
}

@Composable
fun FavoriteIconButton() {
    IconButton(
        onClick = { /*TODO*/ },
        content = {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,//if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(55.dp)
                    .padding(0.dp),
                tint = MaterialTheme.colors.secondary
            )
        }
    )
}

@Composable
fun DetailDescription() {
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = "Название", fontSize = 16.sp)
    // if size not null
    Text(text = "30 см/600 г", fontSize = 14.sp, color = Color.Gray)
    Text(text = "Описание", fontSize = 16.sp)
    Text(text = "Посмотри, как вкусно, попробуй, как красиво", fontSize = 14.sp, color = Color.Gray)
    // if not combo
    Text(text = "Состав", fontSize = 16.sp)
    Text(text = "Помидорки, огурчики, перец, рис, ананас, моцарелла, шохолатка, а дальше все по-новой, Помидорки, огурчики, перец, рис, ананас, моцарелла, шохолатка, а дальше все по-новой", fontSize = 14.sp, color = Color.Gray)
    // if combo



    //ContentCardComponent(cardName = "Акция1")
    //ContentCardComponent(cardName = "Акция2")
}