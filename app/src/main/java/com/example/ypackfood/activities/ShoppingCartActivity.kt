package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.OrderButtonComponent
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.RoomViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun ShoppingCartScreen(navController: NavHostController, cartViewModel: ShoppingCartViewModel, roomViewModel: RoomViewModel) {

    LaunchedEffect(true) {
        cartViewModel.initContentResp()
    }

    val shopList = roomViewModel.shopList.observeAsState(listOf()).value
    val requestState = cartViewModel.contentResp.observeAsState().value

    LaunchedEffect(shopList) {
        if (shopList.size > cartViewModel.dishesRoomState.size) {
            cartViewModel.getContentByListId(shopList.map { it.dishId }.toSet().toList())
        }
        cartViewModel.setDishesRoom(shopList)
    }
    LaunchedEffect(cartViewModel.dishesRoomState) {
        Log.d("fe_dishMap shopListChanged", shopList.toString())
    }
    LaunchedEffect(requestState) {
        Log.d("fe_dishMap requestChanged", requestState?.data.toString())
    }

    Scaffold (
        topBar = {
            ToolbarComponent(navController = navController, title = Screens.ShoppingCart.title)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (!requestState?.data.isNullOrEmpty() && shopList.size == cartViewModel.dishesRoomState.size) {
                val totalCost = cartViewModel.computeTotalPrice()
                OrderButtonComponent(
                    totalCost = totalCost,
                    onClick = {
                        if (totalCost > 0) navController.navigate(route = Screens.Order.createRoute(orderCost = totalCost))
                        else navController.popBackStack()
                    }
                )
            }
        },
        content = {
            Column (
                modifier = Modifier.padding(horizontal = 15.dp),
                content = {
                    if (cartViewModel.dishesRoomState.isEmpty()) {
                        EmptyContentComponent(message = "Корзина пуста")
                    }

                    if (!requestState?.data.isNullOrEmpty() && shopList.size == cartViewModel.dishesRoomState.size) {
                        Log.d("fe_dishMap requestState?.data", requestState!!.data!!.map{ it.id }.toString())

                        ContentSimpleListComponent2(
                            contentList = cartViewModel.composeDishInfo(
                                dishList = requestState.data!!,
                                shopList = cartViewModel.dishesRoomState
                            ),
                            cartViewModel = cartViewModel,
                            roomViewModel = roomViewModel
                        )
                    }

                    RequestStateComponent(
                        requestState = requestState,
                        byError = {
                            ShowErrorComponent(onButtonClick = { cartViewModel.getContentByListId(cartViewModel.dishesRoomState.map { it.dishId }.toSet().toList()) })
                        }
                    )
                }
            )
        }
    )
}