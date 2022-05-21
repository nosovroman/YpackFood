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
import com.example.ypackfood.common.Components
import com.example.ypackfood.components.*
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.viewModels.DetailViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun DetailContentScreen(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    roomViewModel: RoomViewModel,
    contentId: Int
) {
    Log.d("params", "result=$contentId")

    LaunchedEffect(true) {
        detailViewModel.initCountWish()
    }

    val detailDishState = detailViewModel.detailDishState.observeAsState().value
    val favoritesState = detailViewModel.favoritesState.observeAsState().value
    //val favoritesToggledState = detailViewModel.favoritesToggledState.observeAsState().value
    //val favorites = roomViewModel.favorites.observeAsState(listOf()).value
    val cartDishes = roomViewModel.shopList.observeAsState(listOf()).value

    LaunchedEffect(true) {
        detailViewModel.getDetailContent(contentId)
        detailViewModel.getFavoritesId()
        detailViewModel.setIndexOption(0)
    }

    LaunchedEffect(favoritesState) {
        Log.d("checkFavorites", favoritesState.toString())
        //roomViewModel.initFavoriteIcon(contentId)
    }

        // отслеживает добавления в корзину
    LaunchedEffect(cartDishes) {
        Log.d("shopList", cartDishes.toString())
    }

    val scrollState = rememberScrollState()
    Scaffold (
        topBar = {
            ToolbarComponent(
                navController = navController,
                rightIcon = {
                    when (favoritesState) {
                        is NetworkResult.Success<*> -> {
                            Log.d("detailDishState", "success ${favoritesState.data}")
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
                            Log.d("detailDishState", "null or error: ${favoritesState?.data}")
                            IconButton(
                                onClick = {
                                    detailViewModel.getFavoritesId()
                                },
                                content = {
                                    Icon(
                                        imageVector = Components.defaultIcon,
                                        contentDescription = "Избранное",
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (!detailDishState?.data?.name.isNullOrEmpty()) {
                ShoppingRowComponent(
                    navController,
                    detailViewModel,
                    roomViewModel
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            if (!detailDishState?.data?.name.isNullOrEmpty()) {
                Log.d("networkAnswer", "Display data")
                Column (
                    Modifier.verticalScroll(scrollState),
                    content = {
                        PictureTwoComponent(url = detailDishState!!.data!!.picturePaths.large)
                        DetailDescription(detailViewModel, Modifier.padding(start = 20.dp, end = 20.dp))
                    }
                )
            }

            RequestStateComponent(
                requestState = detailDishState,
                byError = {
                    ShowErrorComponent(onButtonClick = { detailViewModel.getDetailContent(contentId) })
                }
            )
        }
    )
}

@Composable
fun ShoppingRowComponent(navController: NavHostController, detailViewModel: DetailViewModel, roomViewModel: RoomViewModel) {
    Row(
        //horizontalAlignment = Alignment.CenterHorizontally
        verticalAlignment = Alignment.CenterVertically,
        content = {
            val detailDishState = detailViewModel.detailDishState.value!!.data!!
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
                        detailViewModel.buildDishInfo(
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
                content = {// .basePortion.priceNow.price
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
    val isCombo = detailDishState.category == "COMBO"

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = detailDishState.name+detailDishState.id+detailDishState.category, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))
        // if not combo
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
                    divider = {  }
                ) {
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
            } else {
                Text(text = detailDishState.portions[0].size!!, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Text(text = "Описание", fontSize = 16.sp)
        Text(text = detailDishState.description, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))
        // if not combo
        if (!isCombo) {
            Text(text = "Состав", fontSize = 16.sp)
            Text(text = detailDishState.composition, fontSize = 14.sp, color = Color.Gray)
        } else {// if combo
            ContentSimpleListComponent(detailDishState.dishes)
        }
    }
}