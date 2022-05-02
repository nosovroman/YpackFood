package com.example.ypackfood.activities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.DELIVERY_COST
import com.example.ypackfood.common.Constants.TIME_ORDER_MESSAGE
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.OrderButtonComponent
import com.example.ypackfood.sealedClasses.DeliveryOptions
import com.example.ypackfood.enumClasses.getCityNames
import com.example.ypackfood.enumClasses.getPaymentOptions
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.sealedClasses.TabRowSwitchable
import com.example.ypackfood.sealedClasses.TimeOptions
import com.example.ypackfood.viewModels.OrderViewModel

@Composable
fun OrderScreen(navController: NavHostController, orderViewModel: OrderViewModel, totalCost: Int) {
    val deliveryState = orderViewModel.deliveryState.observeAsState().value!!

    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Order.title) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            OrderButtonComponent(
                totalCost = if (orderViewModel.checkIsDELIVERY()) totalCost + DELIVERY_COST else totalCost,
                onClick = {
                    orderViewModel.makeOrder()
                }
            )
        },
        content = {
            OrderContent(orderViewModel, deliveryState)
        }
    )
}



@Composable
fun OrderAddressComponent(orderViewModel: OrderViewModel, deliveryState: TabRowSwitchable) {
    Column {
        TabRowComponent(
            currentOption = deliveryState,
            listOptions = DeliveryOptions.getOptions(),
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
                        title = "Адрес доставки",
                        body = {
                            Column {
                                Text(text = "Город")
                                DropdownMenuComponent(
                                    chosenItemTitle = orderViewModel.chosenCityState,
                                    expanded = orderViewModel.expandedMenuCityState,
                                    onMenuClick = { orderViewModel.setExpandedMenuCity(true) },
                                    items = cities,
                                    onItemClick = {
                                        cityName -> orderViewModel.setChosenCity(cityName)
                                        orderViewModel.setExpandedMenuCity(false)
                                    },
                                    onDismissRequest = { orderViewModel.setExpandedMenuCity(false) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Улица, дом, квартира")
                                TextFieldComponent(
                                    modifier = Modifier.fillMaxWidth(),
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
                    //modifier = Modifier.clickable { orderViewModel.setDeliveryDialog(true) }
                )
            }
            else -> {}
        }
    }
}

@Composable
fun OrderContent(orderViewModel: OrderViewModel, deliveryState: TabRowSwitchable) {
    LazyColumn (
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp),
        content = {
            item {
                OrderAddressComponent(orderViewModel = orderViewModel, deliveryState)
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))

                val timeState = orderViewModel.timeState.observeAsState().value!!

                Column {
                    TabRowComponent(
                        currentOption = timeState,
                        listOptions = TimeOptions.getOptions(),
                        onClick = { newChosenOption -> orderViewModel.timeState.postValue(newChosenOption) }
                    )
                    when(timeState) {
                        is TimeOptions.Faster -> {
                            TextRectangleComponent(
                                text = "Примерное время ожидания 45-90 минут",
                            )
                        }
                        is TimeOptions.ForTime -> {
                            TextRectangleComponent(
                                text = "${orderViewModel.hourState}:${orderViewModel.minuteState}",
                                modifier = Modifier.clickable { orderViewModel.setTimeDialog(true) }
                            )
                            if (orderViewModel.timeDialogState) {
                                AlertDialogComponent(
                                    title = "Выбор времени доставки",
                                    body = {
                                        Column {
                                            Text(text = TIME_ORDER_MESSAGE)
                                            Row {
                                                TextFieldComponent(
                                                    modifier = Modifier.width(60.dp),
                                                    currentValue = orderViewModel.hoursFieldState,
                                                    onValueChange = { newHours -> orderViewModel.setHoursField(newHours) },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    //placeholder = "11"
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Text("ч.")
                                                Spacer(modifier = Modifier.width(10.dp))
                                                TextFieldComponent(
                                                    modifier = Modifier.width(60.dp),
                                                    currentValue = orderViewModel.minutesFieldState,
                                                    onValueChange = { newMinutes -> if (newMinutes.length in 0..2 && newMinutes.isDigitsOnly()) orderViewModel.setMinutesField(newMinutes) },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    //placeholder = "00"
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Text("мин.")
                                            }
                                            if (orderViewModel.errorEnteringTime.isNotBlank())
                                                Text(orderViewModel.errorEnteringTime)
                                        }

                                    },
                                    onClickConfirm = {
                                        orderViewModel.tryConfirmTime()
                                    },
                                    onClickDismiss = {
                                        orderViewModel.setDismissTime()
                                    }
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
            if (deliveryState is DeliveryOptions.DELIVERY) {
                item {
                    val paymentOptions = getPaymentOptions()
                    Spacer(modifier = Modifier.size(20.dp))
                    Text("Способ оплаты", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    DropdownMenuComponent(
                        chosenItemTitle = orderViewModel.paymentState,
                        expanded = orderViewModel.expandedMenuPayState,
                        onMenuClick = { orderViewModel.setExpandedMenuPay(true) },
                        items = paymentOptions,
                        onItemClick = {
                            paymentTitle -> orderViewModel.setPayment(paymentTitle)
                            orderViewModel.setExpandedMenuPay(false)
                        },
                        onDismissRequest = { orderViewModel.setExpandedMenuPay(false) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.size(20.dp))
                    Text("Комментарий", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    TextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        currentValue = orderViewModel.commentFieldState,
                        onValueChange = { newComment -> orderViewModel.setCommentField(newComment) },
                        placeholder = "Ваш комментарий для курьера"
                    )
                }
            }
        }
    )

    if (orderViewModel.emptyDataDialogState) {
        AlertDialogComponent(
            title = "Заполните обязательные поля ввода",
            body = { Text(orderViewModel.emptyFieldMsgState) },
            onClickConfirm = { orderViewModel.hideEmptyDataDialog() },
            onClickDismiss = { orderViewModel.hideEmptyDataDialog() }
        )
    }
}