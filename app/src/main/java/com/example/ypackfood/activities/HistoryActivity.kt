package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.MAX_ORDERS_ON_PAGE
import com.example.ypackfood.common.Constants.STANDARD_PADDING
import com.example.ypackfood.common.Constants.TITLE_SIZE
import com.example.ypackfood.components.*
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.models.orders.common.DishForOrderGet
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.HistoryViewModel
import com.example.ypackfood.viewModels.RoomViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(
    navController: NavHostController,
    historyViewModel: HistoryViewModel,
    datastoreViewModel: DatastoreViewModel,
    roomViewModel: RoomViewModel
) {
    val historyDishesState = historyViewModel.historyDishesState.observeAsState().value
    val updateButtonState = historyViewModel.updateButtonState.observeAsState().value

    LaunchedEffect(true) {
        historyViewModel.initStates()
        historyViewModel.getHistoryContent(historyViewModel.currentPageState-1)
        historyViewModel.setUpdateButton(false)
    }

    val refreshState = historyViewModel.refreshState.observeAsState().value

    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                historyViewModel.getHistoryContent(historyViewModel.currentPageState-1)
            }
            is NetworkResult.HandledError<*> -> {
                Log.d("TokenRefresh HandledError ", refreshState.message.toString())
                navController.navigate(route = Screens.SignInUp.route) {
                    popUpTo(Screens.History.route) { inclusive = true }
                }
            }
            else -> {}
        }
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
        when (historyDishesState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = historyDishesState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        historyViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("TokenRefresh detailDishState Logout", errorCode)
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.History.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            is NetworkResult.Success<*> -> {
                Log.d("historyDishesState", historyDishesState.toString())
                historyViewModel.setUpdateButton(false)
                val orders = historyDishesState.data?.orders
                if (!orders.isNullOrEmpty()) {
                    if (orders.any { order -> order.status != "Завершен" && order.status != "Отменён" }) {
                        historyViewModel.setTimerForStatusUpdate()
                    }
                }
            }
            else -> {}
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(horizontal = STANDARD_PADDING)
                    .height(500.dp),
                content = {
                    DetailOrder(
                        contentList = historyViewModel.chosenOrderDialogState,
                        historyViewModel = historyViewModel
                    )
                }
            )
        },
        content = {
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
                                        historyViewModel.getHistoryContent(historyViewModel.currentPageState-1)
                                    }
                                )
                            }
                            when(refreshState) {
                                is NetworkResult.Error<*> -> {
                                    ShowErrorComponent(message = refreshState.message, onButtonClick = { historyViewModel.getHistoryContent(historyViewModel.currentPageState-1) })
                                }
                                else -> {}
                            }
                            when(historyDishesState) {
                                is NetworkResult.Empty<*> -> {
                                    EmptyContentComponent(message = "Заказов пока что не было")
                                }
                                is NetworkResult.Success<*> -> {
                                    LazyColumn (
                                        content = {
                                            itemsIndexed(historyDishesState.data!!.orders) { index, item ->
                                                Log.d("NetworkResult ok", item.toString())
                                                Spacer(modifier = Modifier.height(10.dp))
                                                HistoryCardComponent(
                                                    orderNumber = item.id.toString(),
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
                                                        historyViewModel.setDetailOrderDialog(true, item.dishes as MutableList<DishForOrderGet>)
                                                        coroutineScope.launch {
                                                            bottomState.show()
                                                        }
                                                    },
                                                    onButtonClick = {
                                                        roomViewModel.addToCartMany(
                                                            historyViewModel.composeCartEntities(item.dishes)
                                                        )
                                                        historyViewModel.setAddedToCart(true)
                                                    }
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Divider()
                                            }
                                            item {
                                                if (historyDishesState.data.totalCount > MAX_ORDERS_ON_PAGE) {
                                                    Spacer(modifier = Modifier.height(5.dp))
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Center,
                                                        content = {
                                                            CounterComponent(
                                                                count = historyViewModel.currentPageState,
                                                                upperLimit = historyViewModel.computeMaxPage(historyDishesState.data.totalCount),
                                                                onIncClick = {
                                                                    historyViewModel.incrementCounter()
                                                                    historyViewModel.getHistoryContent(historyViewModel.currentPageState-1)
                                                                },
                                                                onDecClick = {
                                                                    historyViewModel.decrementCounter()
                                                                    historyViewModel.getHistoryContent(historyViewModel.currentPageState-1)
                                                                },
                                                                incImgVector = Icons.Filled.NavigateNext,
                                                                incSymbol = ">",
                                                                decImgVector = Icons.Filled.NavigateBefore,
                                                                decSymbol = "<"
                                                            )
                                                        }
                                                    )
                                                }
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
                                        onButtonClick = { historyViewModel.getHistoryContent(historyViewModel.currentPageState-1) }
                                    )
                                }
                                else -> {}
                            }
                        }
                    )
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
    Spacer(modifier = Modifier.height(10.dp))
    Text("Просмотр блюд заказа", fontSize = TITLE_SIZE)
    LazyColumn (
        content = {
            items(contentList) { item ->
                with (item.dish) {
                    Log.d("ShowDetailOrder", item.toString())
                    ContentCardComponent(
                        cardName = name,
                        description = "${item.count} шт, ${portion.size} = ${item.count * portion.priceNow.price} ₽",
                        urlPicture = picturePaths.large
                    )
                }
            }
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
            ButtonComponent(
                text = "Повторить заказ",
                onClick = {
                    Log.d("History onclick ", listOfId.toString())
                    onButtonClick()
                }
            )
        }
    )
}

@Composable
fun PictureRowComponent(imageList: List<String>) {
    LazyRow {
        items(imageList) { url ->
            PictureOneComponent(size = 55.dp, url = url)
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}