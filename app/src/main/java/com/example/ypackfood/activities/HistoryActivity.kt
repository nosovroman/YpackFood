package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.components.PictureOneComponent
import com.example.ypackfood.components.ShowErrorComponent
import com.example.ypackfood.components.ToolbarComponent
import com.example.ypackfood.models.orders.OrderFull.Order
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.HistoryViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun HistoryScreen(navController: NavHostController, historyViewModel: HistoryViewModel, roomViewModel: RoomViewModel) {

    val historyDishesState = historyViewModel.historyDishesState.observeAsState().value

    LaunchedEffect(true) {
        historyViewModel.getMainContent()
    }

    LaunchedEffect(historyViewModel.addedToCartState) {
        if (historyViewModel.addedToCartState) {
            historyViewModel.setAddedToCart(false)
            navController.navigate(route = Screens.ShoppingCart.route) {
                popUpTo(Screens.History.route) { inclusive = true }
            }
        }
        historyViewModel.setAddedToCart(false)
    }

    Scaffold(
        topBar = { ToolbarComponent(navController = navController) },
        content = {
            LazyColumn (
                modifier = Modifier.padding(horizontal = 15.dp),
                content = {
                    when(historyDishesState) {
                        is NetworkResult.Success<*> -> {
                            itemsIndexed(historyDishesState.data as MutableList<Order>) { index, item ->
                                Log.d("NetworkResult ok", item.toString())
                                HistoryCardComponent(
                                    orderNumber = index.toString(),
                                    status = item.status ?: "Готовится",
                                    totalPrice = item.totalPrice,
                                    time = "${item.created.substringBefore("T")}, ${item.deliveryTime}",
                                    imageList = item.dishes.map { it.picturePaths.large },
                                    listOfId = item.dishes.map { it.id },
                                    onClick = {
                                        roomViewModel.addToCartMany(
                                            historyViewModel.buildCartEntity(item.dishes)
                                        )
                                        historyViewModel.setAddedToCart(true)
                                    }
                                )
                            }
                        }
                        is NetworkResult.Error<*> -> {
                            item {Log.d("NetworkResult", "error")
                                ShowErrorComponent(
                                    message = historyDishesState.message,
                                    onButtonClick = { historyViewModel.getMainContent() }
                                )
                            }
                        }
                        else -> {}
                    }
                }
            )
        }
    )
}

@Composable
fun HistoryCardComponent(
    orderNumber: String,
    status: String,
    time: String,
    totalPrice: Int,
    imageList: List<String>,
    listOfId: List<Int>,
    onClick: () -> Unit
) {
    Log.d("NetworkResult", "HistoryCardComponent")
    Column(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth(),
        content = {
            Text(text = "Заказ № $orderNumber | $status")
            PictureRowComponent(imageList)
            Text(text = time)
            Text(text = "Сумма: $totalPrice ₽")
            Button(
                onClick = {
                    Log.d("History onclick ", listOfId.toString())
                    onClick()
                },
                content = { Text(text = "Повторить заказ") }
            )
        }
    )
}

@Composable
fun PictureRowComponent(imageList: List<String>) {
    LazyRow {
        items(imageList) { url ->
            PictureOneComponent(size = 45.dp, url = url)
        }
    }
}