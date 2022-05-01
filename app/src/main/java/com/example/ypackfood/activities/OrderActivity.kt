package com.example.ypackfood.activities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.DELIVERY_COST
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.OrderButtonComponent
import com.example.ypackfood.enumClasses.DeliveryOptions
import com.example.ypackfood.enumClasses.getCityNames
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.OrderViewModel

@Composable
fun OrderScreen(navController: NavHostController, orderViewModel: OrderViewModel, totalCost: Int) {
    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Order.title) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            OrderButtonComponent(
                totalCost = if (true) totalCost + DELIVERY_COST else totalCost,
                onClick = {}
            )
        },
        content = {
            OrderContent(orderViewModel)
        }
    )
}



@Composable
fun OrderAddressComponent(orderViewModel: OrderViewModel) {
    val deliveryState = orderViewModel.deliveryState.observeAsState().value!!

    Column {
        TabRowComponent(
            currentOption = deliveryState,
            onClick = { newChosenOption -> orderViewModel.deliveryState.postValue(newChosenOption) }
        )
        when(deliveryState) {
            is DeliveryOptions.DELIVERY -> {
                TextRectangleComponent(
                    text = orderViewModel.getAddress(),
                    modifier = Modifier.clickable { orderViewModel.setDeliveryDialog(true) }
                )
                if (orderViewModel.deliveryDialogState) {
                    val cities = getCityNames()
                    AlertDialogComponent(
                        onDismiss = { orderViewModel.setDeliveryDialog(false) },
                        title = "Адрес доставки",
                        body = {
                            Column {
                                Text(text = "Город")

                                DropdownMenuComponent(
                                    chosenItemTitle = orderViewModel.chosenCityState,
                                    expanded = orderViewModel.expandedMenuCity,
                                    onMenuClick = { orderViewModel.setExpandedCity(true) },
                                    items = cities,
                                    onItemClick = {
                                        cityName -> orderViewModel.setCity(cityName)
                                        orderViewModel.setExpandedCity(false)
                                    },
                                    onDismissRequest = { orderViewModel.setExpandedCity(false) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Улица, дом, квартира")
                                TextFieldComponent(
                                    currentValue = orderViewModel.addressFieldState,
                                    onValueChange = { newAddress -> orderViewModel.setAddressField(newAddress) }
                                )
                            }

                        },
                        onClickConfirm = { orderViewModel.setConfirmAddress() },
                        onClickDismiss = { orderViewModel.setDismissAddress() }
                    )
                }
            }
            is DeliveryOptions.PICKUP -> {
                TextRectangleComponent(
                    text = Constants.YPACK_ADDRESS,
                    modifier = Modifier.clickable { orderViewModel.setDeliveryDialog(true) }
                )
            }
            else -> {}
        }
    }
}

@Composable
fun OrderContent(orderViewModel: OrderViewModel) {
    LazyColumn (
        modifier = Modifier.padding(horizontal = 20.dp),
        content = {
            item {
                OrderAddressComponent(orderViewModel = orderViewModel)
            }

            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Как можно скорее/ко времени", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Коммунистический 110", fontSize = 18.sp)
            }

            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Способ оплаты", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Наличными", fontSize = 18.sp)
            }

            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Комментарий", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                TextFieldComponent(
                    currentValue = orderViewModel.commentFieldState,
                    onValueChange = { newComment -> orderViewModel.setCommentField(newComment) },
                    placeholder = "Ваш комментарий для курьера"
                )
            }
        }
    )
}