package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.OrderButtonComponent
import com.example.ypackfood.components.inOrder.ShCartCardComponent
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.OrderViewModel
import com.example.ypackfood.viewModels.RoomViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ShoppingCartScreen(
    navController: NavHostController,
    cartViewModel: ShoppingCartViewModel,
    datastoreViewModel: DatastoreViewModel,
    orderViewModel: OrderViewModel,
    roomViewModel: RoomViewModel
) {
    val shopList = roomViewModel.shopList.observeAsState(listOf()).value
    val cartState = cartViewModel.cartState.observeAsState().value
    val deletingDishList = roomViewModel.deletingCartListState
    val createOrderState = orderViewModel.createOrderState.observeAsState().value
    val shopListFiltered = shopList.filter { it.userId == Auth.authInfo.personId }

    LaunchedEffect(true) {
        cartViewModel.initStates()
    }

    val refreshState = cartViewModel.refreshState.observeAsState().value
    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                cartViewModel.getContentByListId(
                    contentIdList = shopListFiltered.map { it.dishId }.toSet().toList(),
                    roomViewModel = roomViewModel
                )
            }
            is NetworkResult.HandledError<*> -> {
                Log.d("TokenRefresh HandledError ", refreshState.message.toString())
                navController.navigate(route = Screens.SignInUp.route) {
                    popUpTo(Screens.ShoppingCart.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(cartState) {
        when (cartState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = cartState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        cartViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("TokenRefresh favoritesState Logout", errorCode)
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.ShoppingCart.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(createOrderState) {
        if (createOrderState is NetworkResult.Success<*>) {
            roomViewModel.deleteAllFromCart()
            orderViewModel.initStates()
            navController.navigate(route = Screens.History.route) {
                popUpTo(Screens.ShoppingCart.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(shopList) {
        if (shopListFiltered.size > cartViewModel.dishesRoomState.size) {
            cartViewModel.getContentByListId(
                contentIdList = shopListFiltered.map { it.dishId }.toSet().toList(),
                roomViewModel = roomViewModel
            )
        }
        cartViewModel.setDishesRoom(shopListFiltered)
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
                    datastoreViewModel = datastoreViewModel,
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
                    if (!cartState?.data.isNullOrEmpty() && shopListFiltered.size == cartViewModel.dishesRoomState.size) {
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
                    Column (
                        modifier = Modifier.padding(horizontal = 15.dp),
                        content = {
                            if (shopListFiltered.isNullOrEmpty()) {
                                EmptyContentComponent(message = "Корзина пуста")
                            }

                            when(refreshState) {
                                is NetworkResult.Error<*> -> {
                                    ShowErrorComponent(
                                        message = refreshState.message,
                                        onButtonClick = { cartViewModel.getContentByListId(
                                            contentIdList = shopListFiltered.map { it.dishId }.toSet().toList(),
                                            roomViewModel = roomViewModel
                                        )}
                                    )
                                }
                                else -> {}
                            }

                            when (cartState) {
                                is NetworkResult.Loading<*> -> {
                                    Column {
                                        Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                                        LoadingBarComponent()
                                    }
                                }
                                is NetworkResult.Success<*> -> { //if (!requestState?.data.isNullOrEmpty() && shopList.size == cartViewModel.dishesRoomState.size) { }
                                    if (shopListFiltered.size == cartViewModel.dishesRoomState.size) {
                                        cartViewModel.composeDishInfo(
                                            dishList = cartState.data!!,
                                            shopList = cartViewModel.dishesRoomState
                                        )

                                        LazyColumn {
                                            items (cartViewModel.resultDishState) { item ->
                                                ShCartCardComponent(
                                                    cartDish = item,
                                                    cartViewModel = cartViewModel,
                                                    roomViewModel = roomViewModel
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(50.dp))
                                    }
                                }
//                                is NetworkResult.Empty<*> -> {
//                                    EmptyContentComponent(message = "Корзина пуста")
//                                }
                                is NetworkResult.Error<*> -> {
                                    ShowErrorComponent(
                                        message = cartState.message,
                                        onButtonClick = {
                                            cartViewModel.getContentByListId(
                                                contentIdList = cartViewModel.dishesRoomState.map { it.dishId }.toSet().toList(),
                                                roomViewModel = roomViewModel
                                            )
                                        }
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