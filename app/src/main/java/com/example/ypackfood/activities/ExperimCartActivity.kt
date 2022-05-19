package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.OrderButtonComponent
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.OrderViewModel
import com.example.ypackfood.viewModels.RoomViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ShoppingCartScreen(
    navController: NavHostController,
    cartViewModel: ShoppingCartViewModel,
    orderViewModel: OrderViewModel,
    roomViewModel: RoomViewModel
) {
    val shopList = roomViewModel.shopList.observeAsState(listOf()).value
    val requestState = cartViewModel.contentResp.observeAsState().value
    val deletingDishList = roomViewModel.deletingCartListState
    val createOrderState = orderViewModel.createOrderState.observeAsState().value

    LaunchedEffect(true) {
        cartViewModel.initContentResp()
    }

    LaunchedEffect(createOrderState) {
        if (createOrderState is NetworkResult.Success<*>) {
            roomViewModel.deleteAllFromCart()
            orderViewModel.createOrderInit()
            navController.navigate(route = Screens.History.route) {
                popUpTo(Screens.ShoppingCart.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(shopList) {
        if (shopList.size > cartViewModel.dishesRoomState.size) {
            cartViewModel.getContentByListId(
                contentIdList = shopList.map { it.dishId }.toSet().toList(),
                roomViewModel = roomViewModel
            )
        }
        cartViewModel.setDishesRoom(shopList)
    }

    LaunchedEffect(deletingDishList) {
        if (deletingDishList.isNotEmpty()) {
            roomViewModel.deleteFromCartByListId(deletingDishList)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            Box(modifier = Modifier.height(420.dp)) {
                OrderScreen(
                    navController = navController,
                    orderViewModel = orderViewModel,
                    cartViewModel = cartViewModel,
                    totalCost = cartViewModel.totalPriceState
                )
            }
        },
        content = {
            Scaffold (
                topBar = {
                    ToolbarComponent(navController = navController, title = Screens.ShoppingCart.title)
                },
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    if (!requestState?.data.isNullOrEmpty() && shopList.size == cartViewModel.dishesRoomState.size) {
                        //val totalCost = cartViewModel.computeTotalPrice()
                        cartViewModel.computeTotalPrice()
                        OrderButtonComponent(
                            totalCost = cartViewModel.totalPriceState,
                            onClick = {
                                if (cartViewModel.totalPriceState > 0) {
                                    coroutineScope.launch {
                                        bottomState.show()
                                    }
                                }
                                else navController.popBackStack()
                            }
                        )
                    }
                },
                content = {
                    ContentCart(requestState, shopList, cartViewModel, roomViewModel)
                }
            )
        }
    )
}

@Composable
fun ContentCart(
    requestState: NetworkResult<MutableList<Dish>>?,
    shopList: List<CartEntity>,
    cartViewModel: ShoppingCartViewModel,
    roomViewModel: RoomViewModel
) {
    Column (
        modifier = Modifier.padding(horizontal = 15.dp),
        content = {
            if (cartViewModel.dishesRoomState.isEmpty()) {
                EmptyContentComponent(message = "Корзина пуста")
            }

            if (!requestState?.data.isNullOrEmpty() && shopList.size == cartViewModel.dishesRoomState.size) {
                Log.d("fe_dishMap requestState?.data", requestState!!.data!!.map{ it.id }.toString())

                cartViewModel.composeDishInfo(
                    dishList = requestState.data!!,
                    shopList = cartViewModel.dishesRoomState
                )

                ContentSimpleListComponent2(
                    contentList = cartViewModel.resultDishState,
                    cartViewModel = cartViewModel,
                    roomViewModel = roomViewModel
                )

                Spacer(modifier = Modifier.height(50.dp))
            }

            RequestStateComponent(
                requestState = requestState,
                byError = {
                    ShowErrorComponent(
                        message = requestState?.message,
                        onButtonClick = {
                            cartViewModel.getContentByListId(
                                contentIdList = cartViewModel.dishesRoomState.map { it.dishId }.toSet().toList(),
                                roomViewModel = roomViewModel
                            )
                        }
                    )
                }
            )
        }
    )
}