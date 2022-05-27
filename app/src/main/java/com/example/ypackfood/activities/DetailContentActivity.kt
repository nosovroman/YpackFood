package com.example.ypackfood.activities


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Components
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.STANDARD_PADDING
import com.example.ypackfood.common.RequestTemplate.buildDishInfo
import com.example.ypackfood.components.*
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.DetailViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun DetailContentScreen(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    datastoreViewModel: DatastoreViewModel,
    roomViewModel: RoomViewModel,
    contentId: Int
) {
    val detailDishState = detailViewModel.detailDishState.observeAsState().value
    val favoritesState = detailViewModel.favoritesState.observeAsState().value

    LaunchedEffect(true) {
        detailViewModel.initStates()
        detailViewModel.getDetailContent(contentId)
        detailViewModel.getFavoritesId()
        detailViewModel.setIndexOption(0)
    }

    val refreshState = detailViewModel.refreshState.observeAsState().value
    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                detailViewModel.getDetailContent(contentId)
                detailViewModel.getFavoritesId()
            }
            is NetworkResult.HandledError<*> -> {
                Log.d("TokenRefresh HandledError ", refreshState.message.toString())
                navController.navigate(route = Screens.SignInUp.route) {
                    popUpTo(Screens.DetailContent.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(favoritesState) {
        when (favoritesState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = favoritesState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        detailViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("TokenRefresh favoritesState Logout", errorCode)
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.DetailContent.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(detailDishState) {
        when (detailDishState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = detailDishState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        detailViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("TokenRefresh detailDishState Logout", errorCode)
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.DetailContent.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    val scrollState = rememberScrollState()
    Scaffold (
        topBar = {
            ToolbarComponent(
                navController = navController,
                rightIcon = {
                    when (favoritesState) {
                        is NetworkResult.Success<*> -> {
                            IconButton(
                                onClick = {
                                    detailViewModel.onClickFavoritesIcon(contentId, favoritesState.data!!)
                                },
                                enabled = detailViewModel.enabledIButtonState,
                                content = {
                                    Icon(
                                        imageVector =  detailViewModel.getRealCurrentIcon(contentId, favoritesState.data!!),
                                        contentDescription = "Избранное",
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = Components.defaultIcon,
                                contentDescription = "Избранное",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (!detailDishState?.data?.name.isNullOrEmpty()) {//if (!detailDishState?.data?.name.isNullOrEmpty()) detailDishState is NetworkResult.Success<*>
                ShoppingRowComponent(
                    navController,
                    detailViewModel,
                    roomViewModel
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            when(refreshState) {
                is NetworkResult.Error<*> -> {
                    ShowErrorComponent(
                        message = refreshState.message,
                        onButtonClick = {
                            detailViewModel.getDetailContent(contentId)
                            detailViewModel.getFavoritesId()
                        }
                    )
                }
                else -> {}
            }

            when(detailDishState) {
                is NetworkResult.Loading<*> -> {
                    Column {
                        Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                        LoadingBarComponent()
                    }
                }
                is NetworkResult.Success<*> -> {
                    Log.d("networkAnswer", "Display data")
                    Column (
                        modifier = Modifier.verticalScroll(scrollState),
                        content = {
                            PictureTwoComponent(url = detailDishState.data!!.picturePaths.large)
                            DetailDescription(detailViewModel, Modifier.padding(horizontal = STANDARD_PADDING))
                        }
                    )
                }
                is NetworkResult.Error<*> -> {
                    ShowErrorComponent(message = detailDishState.message, onButtonClick = { detailViewModel.getDetailContent(contentId) })
                }
                else -> {}
            }
        }
    )
}

@Composable
fun ShoppingRowComponent(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    roomViewModel: RoomViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        content = {
            val detailDishState = detailViewModel.detailDishState.value?.data!!
            CounterComponent(
                count = detailViewModel.countWishDishes,
                unit = "шт.",
                onIncClick = { detailViewModel.incrementCounter() },
                onDecClick = { detailViewModel.decrementCounter() }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    val chosenPortion = detailDishState.portions[detailViewModel.indexOptionState]
                    roomViewModel.addToCart(
                        buildDishInfo(
                            dishId = detailDishState.id,
                            portionId = chosenPortion.id,
                            priceId = chosenPortion.priceNow.id,
                            price = chosenPortion.priceNow.price,
                            count = detailViewModel.countWishDishes
                        )
                    )
                    Log.d("Cart", "Корзина")
                    navController.popBackStack()
                },
                content = {
                    Text("В корзину за ${detailDishState.portions[detailViewModel.indexOptionState].priceNow.price * detailViewModel.countWishDishes} ₽")
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
    )
}


@Composable
fun DetailDescription(detailViewModel: DetailViewModel, modifier: Modifier = Modifier) {
    val detailDishState = detailViewModel.detailDishState.value!!.data!!
    val isCombo = detailDishState.category == "Комбо"

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = detailDishState.name, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))

        if (!isCombo) {
            if (detailDishState.portions.size > 1) {
                val state = detailViewModel.indexOptionState
                TabRow(
                    selectedTabIndex = state,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .height(40.dp)
                        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp)),
                    backgroundColor = Color.Transparent,
                    indicator = {  },
                    divider = {  },
                    tabs = {
                        val portionList = detailDishState.portions
                        portionList.forEachIndexed { index, item ->
                            Tab(
                                text = { Text(item.size!!) },
                                selected = state == index,
                                onClick = { detailViewModel.setIndexOption(index) },
                                selectedContentColor = Color.White,
                                unselectedContentColor = MaterialTheme.colors.onBackground,
                                modifier = Modifier
                                    .background(
                                        color = if (state == index) MaterialTheme.colors.primary else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            )
                        }
                    }
                )
            } else {
                Text(text = detailDishState.portions[0].size!!, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Text(text = "Описание", fontSize = 16.sp)
        Text(text = detailDishState.description, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))

        if (!isCombo) {
            Text(text = "Состав", fontSize = 16.sp)
            Text(text = detailDishState.composition, fontSize = 14.sp, color = Color.Gray)
        } else {
            SimpleListComponent(detailDishState.dishes)
        }
    }
}