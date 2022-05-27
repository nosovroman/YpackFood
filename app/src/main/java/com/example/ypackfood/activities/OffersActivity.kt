package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.components.*
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.OfferViewModel
import com.example.ypackfood.viewModels.RoomViewModel

@Composable
fun OffersScreen(
    navController: NavHostController,
    offerViewModel: OfferViewModel,
    datastoreViewModel: DatastoreViewModel,
    roomViewModel: RoomViewModel,
    offerId: Int
) {
    LaunchedEffect(true) {
        offerViewModel.initStates()
        offerViewModel.getOfferContent(offerId)
    }

    val actionState = offerViewModel.actionState.observeAsState().value

    val refreshState = offerViewModel.refreshState.observeAsState().value
    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                offerViewModel.getOfferContent(offerId)
            }
            is NetworkResult.HandledError<*> -> {
                Log.d("TokenRefresh HandledError ", refreshState.message.toString())
                navController.navigate(route = Screens.SignInUp.route) {
                    popUpTo(Screens.Offers.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(actionState) {
        when (actionState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = actionState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        offerViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("TokenRefresh favoritesState Logout", errorCode)
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.Offers.route) { inclusive = true }
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
        topBar = { ToolbarComponent(navController = navController, title = Screens.Offers.title) },
        content = {
            when(refreshState) {
                is NetworkResult.Error<*> -> {
                    ShowErrorComponent(
                        message = refreshState.message,
                        onButtonClick = {
                            offerViewModel.getOfferContent(offerId)
                        }
                    )
                }
                else -> {}
            }

            when(actionState) {
                is NetworkResult.Loading<*> -> {
                    Column {
                        Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                        LoadingBarComponent()
                    }
                }
                is NetworkResult.Success<*> -> {
                    Column(
                        modifier = Modifier.verticalScroll(scrollState),
                        content = {
                            val actionInfo = actionState.data!!
                            PictureTwoComponent(url = actionInfo.picturePaths.large)
                            //OfferDescription(offerViewModel, Modifier.padding(horizontal = 20.dp))
                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(text = "Описание", fontSize = 16.sp)
                                Text(text = actionInfo.name, fontSize = 14.sp, color = Color.Gray)

                                if (actionInfo.dishes.isNotEmpty()) {
                                    ContentListComponentActions(
                                        contentList = actionInfo.dishes,
                                        onItemClick = {
                                            //val chosenPortion = detailDishState.portions[detailViewModel.indexOptionState]
                                                chosenDish ->
                                            roomViewModel.addToCart(
                                                RequestTemplate.buildDishInfo(
                                                    dishId = chosenDish.id,
                                                    portionId = chosenDish.basePortion.id,
                                                    priceId = chosenDish.basePortion.priceNow.id,
                                                    price = chosenDish.basePortion.priceNow.price,
                                                    count = 1
                                                )
                                            )

                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
                is NetworkResult.Error<*> -> {
                    ShowErrorComponent(onButtonClick = { offerViewModel.getOfferContent(offerId) })
                }
                else -> {}
            }

//            if (!actionState?.data?.name.isNullOrEmpty()) {
//                val response = offerViewModel.actionState.value!!.data!!
////                offerViewModel.setTotalOldPrice(response.dishes.sumOf { it.basePortion.oldPrice?.price ?: 0 })
////                offerViewModel.setTotalNewPrice(response.dishes.sumOf { it.basePortion.priceNow.price })
//
//                Column(
//                    modifier = Modifier.verticalScroll(scrollState),
//                    content = {
//                        PictureTwoComponent(url = offerViewModel.actionState.value!!.data!!.picturePaths.large)
//                        //OfferDescription(offerViewModel, Modifier.padding(horizontal = 20.dp))
//                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//                            Spacer(modifier = Modifier.height(20.dp))
//                            Text(text = "Описание", fontSize = 16.sp)
//                            Text(text = response.name, fontSize = 14.sp, color = Color.Gray)
//
//                            if (response.dishes.isNotEmpty()) {
//                                ContentListComponentActions(
//                                    contentList = response.dishes,
//                                    onItemClick = {
//                                        //val chosenPortion = detailDishState.portions[detailViewModel.indexOptionState]
//                                        chosenDish ->
//                                        roomViewModel.addToCart(
//                                            RequestTemplate.buildDishInfo(
//                                                dishId = chosenDish.id,
//                                                portionId = chosenDish.basePortion.id,
//                                                priceId = chosenDish.basePortion.priceNow.id,
//                                                price = chosenDish.basePortion.priceNow.price,
//                                                count = 1
//                                            )
//                                        )
//
//                                        navController.popBackStack()
//                                    }
//                                )
//                                Spacer(modifier = Modifier.size(100.dp))
//                            }
//                        }
//                    }
//                )
//            }
        }
    )
}