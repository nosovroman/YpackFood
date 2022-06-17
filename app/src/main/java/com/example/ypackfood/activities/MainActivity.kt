package com.example.ypackfood.activities


import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants.TOOLBAR_HEIGHT
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.ContentListComponent2
import com.example.ypackfood.components.specific.ActionsRowComponent
import com.example.ypackfood.components.specific.DishesColumnComponent
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.models.mainContent.FilteredDishes
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.ui.theme.Orange
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.MainViewModel
import com.example.ypackfood.viewModels.RoomViewModel


@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    datastoreViewModel: DatastoreViewModel,
    roomViewModel: RoomViewModel
) {
    mainViewModel.listContentStateInit(rememberLazyListState())
    mainViewModel.listCategoryStateInit(rememberLazyListState())
    mainViewModel.scaffoldStateInit(rememberScaffoldState())

    val toolbarHeightPx = with(LocalDensity.current) { TOOLBAR_HEIGHT.roundToPx().toFloat() }
    val nestedScrollConnection = remember {     // официальная документация Android Developers https://developer.android.com/jetpack/compose/gestures
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = mainViewModel.toolbarOffsetState + delta
                mainViewModel.setToolbarOffset(newOffset.coerceIn(-toolbarHeightPx, 0f))
                return Offset.Zero
            }
        }
    }

    val dishesState = mainViewModel.dishesState.observeAsState().value
    val actionsState = mainViewModel.actionsState.observeAsState().value
    val shopList = roomViewModel.shopList.observeAsState(listOf()).value
    val refreshState = mainViewModel.refreshState.observeAsState().value

    LaunchedEffect(true) {
        Log.d("SignInUp LaunchedEffect(registerState)", "mainActivity")
        Log.d("dishesStateLog", "Launched")
        mainViewModel.initStates()
        mainViewModel.getMainContent()
        mainViewModel.getActionsContent()
    }

    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                mainViewModel.getMainContent()
                mainViewModel.getActionsContent()
            }
            is NetworkResult.HandledError<*> -> {
                Log.d("TokenRefresh HandledError ", refreshState.message.toString())
                navController.navigate(route = Screens.SignInUp.route) {
                    popUpTo(Screens.Main.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(dishesState) {
        when (dishesState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = dishesState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        mainViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("dishesStateLog errorCode", errorCode)
                        Log.d("TokenRefresh dishesState", "Logout")
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.Main.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(actionsState) {
        when (actionsState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = actionsState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        mainViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("actionsStateLog errorCode", errorCode)
                        Log.d("TokenRefresh actionsState", "Logout")
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.Main.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    Scaffold(
        scaffoldState = mainViewModel.scaffoldState,
        drawerContent = {
            DrawerComponent(
                navController = navController,
                //onExitClick = { datastoreViewModel.clearAuthInfo() },
                mainViewModel = mainViewModel
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection),
                content = {
                    ContentListComponent2(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        itemsOfList = {

                            when(actionsState) {
                                is NetworkResult.Success<*> -> {
                                    item { ActionsRowComponent(navController, actionsState.data as MutableList<ActionsItem>) }
                                }
                                is NetworkResult.Error<*> -> {
                                    item { ShowErrorComponent(message = actionsState.message, onButtonClick = { mainViewModel.getActionsContent() }) }
                                }
                                else -> {}
                            }

                            when(dishesState) {
                                is NetworkResult.Loading<*> -> {
                                    item {
                                        Column {
                                            Spacer(modifier = Modifier.height(TOOLBAR_HEIGHT + 15.dp))
                                            LoadingBarComponent()
                                        }
                                    }
                                }
                                is NetworkResult.Success<*> -> {
                                    itemsIndexed(dishesState.data as MutableList<FilteredDishes>) { index, item ->
                                        DishesColumnComponent(navController, item, index)
                                    }
                                }
                                is NetworkResult.Error<*> -> {
                                    item { ShowErrorComponent(message = dishesState.message, onButtonClick = { mainViewModel.getMainContent() }) }
                                }
                                else -> {}
                            }
                        }
                    )
                    CategoriesRowComponent(mainViewModel)

                    when(refreshState) {
                        is NetworkResult.Loading<*> -> {
                            Column {
                                Spacer(modifier = Modifier.height(TOOLBAR_HEIGHT + 15.dp))
                                LoadingBarComponent()
                            }
                        }
                        is NetworkResult.Error<*> -> {
                            ShowErrorComponent(message = refreshState.message, onButtonClick = { mainViewModel.getMainContent() })
                        }
                        else -> {}
                    }

                    ToolbarScrollComponent(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        rightIcon = {
                            val infiniteTransition = rememberInfiniteTransition()
                            val animatedBackground by infiniteTransition.animateColor (
                                initialValue = MaterialTheme.colors.primary,
                                targetValue = Orange,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )

                            val modifierForAnimatedBackground = Modifier
                                .background(
                                    color = if (shopList.filter { it.userId == Auth.authInfo.personId }.isNullOrEmpty()) {
                                        MaterialTheme.colors.primary
                                    } else {
                                        animatedBackground
                                    },
                                    shape = CircleShape
                                )

                            IconButton(
                                modifier = modifierForAnimatedBackground,
                                onClick = { navController.navigate(route = Screens.ShoppingCart.route) },
                                content = {
                                    Icon(
                                        imageVector = Icons.Outlined.ShoppingCart,
                                        contentDescription = "Корзина",
                                        modifier = Modifier.scale(-1.0f, 1.0f)
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}