package com.example.ypackfood.activities


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.viewModels.DetailViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun DetailContentScreen(navController: NavHostController, detailViewModel: DetailViewModel, roomViewModel: RoomViewModel, contentId: Int) {
    Log.d("params", "result=$contentId")

    LaunchedEffect(contentId) {
        detailViewModel.getDetailContent(contentId)
        roomViewModel.initFavoriteIcon(contentId)
        //roomViewModel.checkFavoriteById(contentId)
    }

    val requestState = detailViewModel.contentResp.observeAsState().value
    val favorites = roomViewModel.favorites.observeAsState(listOf()).value

    val scrollState = rememberScrollState()
    Scaffold (
        topBar = {
            ToolbarComponent(
                navController = navController,
                rightIcon = {
                    favorites.let {
                        Log.d("roomRequest", it.toString())
                        IconButton(
                            onClick = {
                                roomViewModel.setFavoritesIcon(contentId)
                            },
                            content = {
                                Icon(
                                    imageVector = roomViewModel.currentIcon,
                                    contentDescription = "Избранное",
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        )
                    }
                },
            )
        },
        floatingActionButton = { ShoppingRowComponent() },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            if (!detailViewModel.contentResp.value?.data?.name.isNullOrEmpty()) {
                Log.d("networkAnswer", "Display data")
                Column (
                    Modifier
                        .verticalScroll(scrollState)
                ) {
                    //PictureTwoComponent(url = detailViewModel.contentResp.value!!.data!!.picturePaths.large)
                    PictureTwoComponent(url = Constants.baseUrlPictureContent)
                    DetailDescription(detailViewModel, Modifier.padding(start = 20.dp, end = 20.dp))
                }
            }

            RequestStateComponent(
                requestState = requestState,
                byLoading = {
                    Column {
                        Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                        LoadingBarComponent()
                    }
                },
                byError = {
                    ShowErrorComponent(onButtonClick = { detailViewModel.getDetailContent(contentId) })
                }
            )
        }
    )
}

@Composable
fun DetailDescription(detailViewModel: DetailViewModel, modifier: Modifier = Modifier) {
    val response = detailViewModel.contentResp.value!!.data!!
    val isCombo = response.category == "COMBO"

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = response.name+response.id+response.category, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))
        // if not combo
        if (!isCombo) {
            Text(text = response.portions[0].size, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))
        }
        Text(text = "Описание", fontSize = 16.sp)
        Text(text = response.description, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))
        // if not combo
        if (!isCombo) {
            Text(text = "Состав", fontSize = 16.sp)
            Text(text = response.composition, fontSize = 14.sp, color = Color.Gray)
        } else {// if combo
            ContentSimpleListComponent(response.dishes)
        }
    }
}

@Composable
fun ShoppingRowComponent() {
    Row(
        //horizontalAlignment = Alignment.CenterHorizontally
        verticalAlignment = Alignment.CenterVertically
    ) {
        CounterComponent()
        Spacer(modifier = Modifier.width(10.dp))
            //Text("1500 ₽", modifier = Modifier.padding(start = 10.dp, end = 10.dp))
//        IconButton(
//            onClick = { },
//            content = {
//                Icon(imageVector = Icons.Outlined.AddShoppingCart,
//                    contentDescription = "В корзину",
//                    tint = MaterialTheme.colors.primary
//                )
//            }
//        )
        Button(
            onClick = { Log.d("Cart", "Корзина") },
            content = { Text("В корзину за 350 ₽") },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun CounterComponent()//(ind: Int, shoppingCartViewModel: ShoppingCartViewModel)
{
    val counterButtonSize: Dp = 30.dp
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        //onClick = { shoppingCartViewModel.decrementCount(ind) },
        //enabled = shoppingCartViewModel.count[ind] != 1,
        //Text(text = "${shoppingCartViewModel.count[ind]}", modifier = Modifier.align(alignment = Alignment.CenterVertically))
        IconButton(
            modifier = Modifier
                .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                .size(counterButtonSize),
            onClick = {  },
            content = {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "-", tint = MaterialTheme.colors.primary)
            }
        )
        Text(text = "1 шт.", fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp, end = 5.dp))
        IconButton(
            modifier = Modifier
                .background(color = MaterialTheme.colors.primary, shape = CircleShape)
                .size(counterButtonSize),
            onClick = { },
            content = {
                Icon(imageVector = Icons.Default.Add, contentDescription = "+", tint = MaterialTheme.colors.onPrimary)
            }
        )
    }
}