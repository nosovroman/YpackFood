package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.models.orders.OrderFull.Order
import com.example.ypackfood.models.orders.OrderMin.DishForOrderGet
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.HistoryViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun HistoryScreen(
    navController: NavHostController,
    historyViewModel: HistoryViewModel,
    roomViewModel: RoomViewModel
) {
    val historyDishesState = historyViewModel.historyDishesState.observeAsState().value
    val updateButtonState = historyViewModel.updateButtonState.observeAsState().value

    LaunchedEffect(true) {
        historyViewModel.getHistoryContent(historyViewModel.currentPageState)
        historyViewModel.setUpdateButton(false)
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

    LaunchedEffect(historyDishesState) {
        Log.d("historyDishesState", historyDishesState.toString())
        val orders = historyDishesState?.data?.orders
        if (!orders.isNullOrEmpty()) {
            if (orders.any { order -> order.status != "Завершен" && order.status != "Отменён" }) {
                historyViewModel.setTimerForStatusUpdate()
            }
        }
    }

    Scaffold(
        topBar = { ToolbarComponent(title = "Мои заказы", navController = navController) },
        content = {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp),
                content = {
                    if (updateButtonState == true) {
                        ButtonComponent(
                            modifier = Modifier.padding(top = 10.dp),
                            text = "Обновить статусы ожидаемых заказов",
                            onClick = {
                                historyViewModel.setUpdateButton(false)
                                historyViewModel.getHistoryContent(historyViewModel.currentPageState)
                            }
                        )
                    }

                    if (!historyViewModel.detailOrderDialogIsEmpty()) {
                        DetailOrder(
                            contentList = historyViewModel.chosenOrderDialogState,
                            historyViewModel = historyViewModel
                        )
                    }
                    when(historyDishesState) {
                        is NetworkResult.Success<*> -> {
                            if (historyDishesState.data?.orders?.isEmpty() == true) {
                                EmptyContentComponent(message = "Заказов пока что не было")
                            }

                            LazyColumn (
                                content = {
                                    itemsIndexed(historyDishesState.data?.orders as MutableList<Order>) { index, item ->
                                        Log.d("NetworkResult ok", item.toString())
                                        Spacer(modifier = Modifier.height(10.dp))
                                        HistoryCardComponent(
                                            orderNumber = index.toString(),
                                            status = item.status ?: "Готовится",
                                            totalPrice = item.totalPrice,
                                            time = "${item.created.substringBefore("T")}, ${item.deliveryTime}",
                                            imageList = item.dishes.map { it.dish.picturePaths.large },
                                            listOfId = item.dishes.map { it.dish.id },
                                            onCardClick = {
                                                Log.d(
                                                    "HistoryCardComponent",
                                                    item.dishes.toString()
                                                )
                                                historyViewModel.setDetailOrderDialog(
                                                    true,
                                                    item.dishes as MutableList<DishForOrderGet>
                                                )
                                            },
                                            onButtonClick = {
                                                roomViewModel.addToCartMany(
                                                    historyViewModel.buildCartEntity(item.dishes)
                                                )
                                                historyViewModel.setAddedToCart(true)
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Divider()
                                    }
                                }
                            )
                        }
                        is NetworkResult.Loading<*> -> {
                            Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                            LoadingBarComponent()
                        }
                        is NetworkResult.Error<*> -> {
                            Log.d("NetworkResult", "error")
                            ShowErrorComponent(
                                message = historyDishesState.message,
                                onButtonClick = { historyViewModel.getHistoryContent(historyViewModel.currentPageState) }
                            )
                        }
                        else -> {}
                    }
                }
            )
        }
    )
}

@Composable
fun DetailOrder(
    contentList: List<DishForOrderGet>,
    historyViewModel: HistoryViewModel
) {
    val scrollState = rememberScrollState()

    AlertDialogComponent(
        title = "Просмотр блюд заказа",
        body = {
            Box(
                modifier = Modifier.heightIn(max = 400.dp),
                content = {
                    LazyColumn (
                        content = {
                            items(contentList) { item ->
                                with (item.dish) {
                                    Log.d("ShowDetailOrder", item.toString())
                                    ContentCardComponent(
                                        cardName = name,
                                        hint = "${item.count} шт. = ${item.count * portion.priceNow.price} ₽",
                                        urlPicture = picturePaths.large
                                    )
                                }
                            }
                        }
                    )
                }
            )

        },
        onClickConfirm = { historyViewModel.clearDetailOrderDialog() },
        onClickDismiss = { historyViewModel.clearDetailOrderDialog() }
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
    onCardClick: () -> Unit,
    onButtonClick: () -> Unit
) {
    Log.d("NetworkResult", "HistoryCardComponent")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        content = {
            Text(text = "Заказ № $orderNumber | $status")
            PictureRowComponent(imageList)
            Text(text = time)
            Text(text = "Сумма: $totalPrice ₽")
            Button(
                onClick = {
                    Log.d("History onclick ", listOfId.toString())
                    onButtonClick()
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
            PictureOneComponent(size = 55.dp, url = url)
        }
    }
}