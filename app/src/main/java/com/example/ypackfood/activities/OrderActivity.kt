package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.DELIVERY_COST
import com.example.ypackfood.common.Constants.TIME_ORDER_MESSAGE
import com.example.ypackfood.components.*
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.enumClasses.getCityNames
import com.example.ypackfood.enumClasses.getPaymentOptions
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.orders.OrderFull.Order
import com.example.ypackfood.models.user.ProfileInfo
import com.example.ypackfood.sealedClasses.*
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.OrderViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun OrderScreen(
    navController: NavHostController,
    orderViewModel: OrderViewModel,
    datastoreViewModel: DatastoreViewModel,
    cartViewModel: ShoppingCartViewModel,
    totalCost: Int
) {
    val deliveryState = orderViewModel.deliveryState.observeAsState().value!!
    val createOrderState = orderViewModel.createOrderState.observeAsState().value
    val profileState = orderViewModel.profileState.observeAsState().value

    LaunchedEffect(true) {
        orderViewModel.initStates()
    }

    val refreshState = orderViewModel.refreshState.observeAsState().value
    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                orderViewModel.createOrderState.postValue(null)
                if (profileState is NetworkResult.HandledError<*>) {
                    when (val errorCode = profileState.message.toString()) {
                        ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                            orderViewModel.getProfile()
                        }
                    }
                }
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

    LaunchedEffect(profileState) {
        when (profileState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = profileState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        orderViewModel.refreshToken()
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
        when (createOrderState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = createOrderState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        orderViewModel.refreshToken()
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

    OrderContent(createOrderState, profileState, orderViewModel, refreshState, cartViewModel, deliveryState, totalCost)
}

@Composable
fun OrderContent(
    createOrderState: NetworkResult<Order>?,
    profileState:  NetworkResult<ProfileInfo>?,
    orderViewModel: OrderViewModel,
    refreshState:  NetworkResult<AuthInfo>?,
    cartViewModel: ShoppingCartViewModel,
    deliveryState: TabRowSwitchable,
    totalCost: Int
) {
    when(refreshState) {
        is NetworkResult.Error<*> -> {
            ShowErrorComponent(
                message = refreshState.message,
                onButtonClick = {
                    orderViewModel.refreshToken()
                }
            )
        }
        else -> {}
    }

    when(createOrderState) {
        is NetworkResult.Loading<*> -> {
            Column {
                Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                LoadingBarComponent()
            }
        }
        is NetworkResult.Success<*> -> {

        }
        is NetworkResult.Error<*> -> {
            val totalCostResult = if (orderViewModel.checkIsDELIVERY()) totalCost + DELIVERY_COST else totalCost
            val addressMerged = "${orderViewModel.cityState}, ${orderViewModel.addressState}"
            ShowErrorComponent(
                message = createOrderState.message,
                onButtonClick = { orderViewModel.makeOrder(
                    dishMinList = cartViewModel.resultDishState,
                    addressMerged = addressMerged,
                    totalCost = totalCostResult)
                }
            )
        }
        null -> {
            val addressOptionState = orderViewModel.addressOptionState.observeAsState().value!!
            LazyColumn (
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                content = {
                    item {
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
                                    Text(text = "Цена доставки $DELIVERY_COST")
                                    if (orderViewModel.deliveryDialogState) {
                                        val cities = getCityNames()
                                        AlertDialogComponent(
                                            title = "Адрес доставки",
                                            body = {
                                                Column {
                                                    TabRowComponent(
                                                        currentOption = addressOptionState,
                                                        listOptions = AddressOptions.getOptions(),
                                                        onClick = {
                                                                newChosenOption -> orderViewModel.addressOptionState.postValue(newChosenOption)
                                                                if (newChosenOption is AddressOptions.OLD_ADDRESS) orderViewModel.getProfile()
                                                        },
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                                    when(addressOptionState) {
                                                        is AddressOptions.NEW_ADDRESS -> {
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
                                                        }
                                                        is AddressOptions.OLD_ADDRESS -> {
                                                            when(profileState) {
                                                                is NetworkResult.Loading<*> -> {
                                                                    //Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                                                                    LoadingBarComponent()
                                                                }
                                                                is NetworkResult.Success<*> -> {
                                                                    val addressList = profileState.data!!.addresses.map { it.address }
                                                                    Column {
                                                                        DropdownMenuComponent(
                                                                            chosenItemTitle = orderViewModel.chosenAddressState, //profileState.data.addresses.firstOrNull()?.address ?: "",//[0].address ?: " ",
                                                                            expanded = orderViewModel.expandedAddressState,
                                                                            onMenuClick = { orderViewModel.setExpandedAddress(true) },
                                                                            items = addressList,
                                                                            onItemClick = {
                                                                                    address -> orderViewModel.setChosenAddress(address)
                                                                                    orderViewModel.setExpandedAddress(false)
                                                                            },
                                                                            onDismissRequest = { orderViewModel.setExpandedAddress(false) }
                                                                        )
                                                                    }
                                                                }
                                                                is NetworkResult.Error<*> -> {
                                                                    Log.d("NetworkResult", "error")
                                                                    ShowErrorComponent(
                                                                        message = profileState.message,
                                                                        onButtonClick = { orderViewModel.getProfile() }
                                                                    )
                                                                }
                                                                else -> {}
                                                            }

                                                        }
                                                        else -> {}
                                                    }
                                                }
                                            },
                                            onClickConfirm = { orderViewModel.setConfirmAddress(addressOptionState) },
                                            onClickDismiss = { orderViewModel.setDismissAddress(addressOptionState) }
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
                    item {
                        Spacer(modifier = Modifier.size(10.dp))

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
                            Spacer(modifier = Modifier.size(10.dp))
                            Text("Способ оплаты")
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
                            Spacer(modifier = Modifier.size(10.dp))
                            Text("Комментарий")
                            TextFieldComponent(
                                modifier = Modifier.fillMaxWidth(),
                                currentValue = orderViewModel.commentFieldState,
                                onValueChange = { newComment -> orderViewModel.setCommentField(newComment) },
                                placeholder = "Ваш комментарий для курьера"
                            )
                        }
                    }
                    item {
                        val totalCostResult = orderViewModel.computeResultTotalCost(totalCost)
                        //val addressMerged = "${orderViewModel.cityState}, ${orderViewModel.addressState}"
                        if (totalCostResult > 0) {
                            ButtonComponent(
                                text = "Оформить на $totalCostResult ₽",
                                onClick = { orderViewModel.makeOrder(
                                    dishMinList = cartViewModel.resultDishState,
                                    addressMerged = orderViewModel.getAddress(),//addressMerged,
                                    totalCost = totalCostResult
                                ) },
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
        else -> {}
    }
}