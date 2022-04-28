package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.OrderButtonComponent
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.RoomViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun ShoppingCartScreen(navController: NavHostController, cartViewModel: ShoppingCartViewModel, roomViewModel: RoomViewModel) {
    //val scrollState = rememberScrollState()
    //val prices = listOf(100, 200)
    //val x = cartViewModel.count.mapIndexed { index, i -> i * prices[index] }
    //val totalCost = x.sum()


    val shopList = roomViewModel.shopList.observeAsState(listOf()).value
    val requestState = cartViewModel.contentResp.observeAsState().value

    LaunchedEffect(shopList) {
        if (shopList.isNotEmpty()) { // && requestState.isEmpty()
            cartViewModel.getContentByListId(shopList.map { it.dishId }.toSet().toList())
        }
    }


    Scaffold (
        topBar = {
            ToolbarComponent(navController = navController, title = Screens.ShoppingCart.title)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { OrderButtonComponent(-1) },
        content = {
            Column (
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                    //.verticalScroll(scrollState),
                content = {
                    if (!shopList.isNullOrEmpty()) {
                        if (!requestState?.data.isNullOrEmpty()) {

                            val resultDishList = cartViewModel.composeDishInfo(
                                dishList = requestState!!.data!!,
                                shopList = shopList
                            )

                            ContentSimpleListComponent2(
                                contentList = resultDishList,
                                cartViewModel = cartViewModel,
                                roomViewModel = roomViewModel
                            )
                        }
                    } else {
                        Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Корзина пуста",
                                textAlign = TextAlign.Center
                            )
                        }
                    }


                    RequestStateComponent(
                        requestState = requestState,
                        byError = {
                            ShowErrorComponent(onButtonClick = { cartViewModel.getContentByListId(shopList.map { it.dishId }.toSet().toList()) })
                        }
                    )
                }
            )
        }
    )
}