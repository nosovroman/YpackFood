package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants.TOOLBAR_HEIGHT
import com.example.ypackfood.components.*
import com.example.ypackfood.components.inOrder.ContentListComponent2
import com.example.ypackfood.components.specific.ActionsRowComponent
import com.example.ypackfood.components.specific.DishesColumnComponent
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.models.mainContent.Category
import com.example.ypackfood.viewModels.MainViewModel


@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    mainViewModel.listContentStateInit(rememberLazyListState())
    mainViewModel.listCategoryStateInit(rememberLazyListState())
    mainViewModel.scaffoldStateInit(rememberScaffoldState())

    val toolbarHeightPx = with(LocalDensity.current) { TOOLBAR_HEIGHT.roundToPx().toFloat() }
    val nestedScrollConnection = remember {
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

    Scaffold(
        scaffoldState = mainViewModel.scaffoldState,
        drawerContent = { DrawerComponent(navController) },
        content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
            ) {
                ContentListComponent2(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    itemsOfList = {
                        item {
                            RequestStateComponent(
                                requestState = actionsState,
                                bySuccess = { ActionsRowComponent(navController, actionsState!!.data as MutableList<ActionsItem>) },
                                byError = { ShowErrorComponent(message = actionsState?.message, onButtonClick = { mainViewModel.getActionsContent() }) }
                            )
                        }

                        itemsIndexed(dishesState?.data as MutableList<Category>) { index, item ->
                            DishesColumnComponent(navController, item, index)
                        }
                    }
                )
                CategoriesRowComponent(mainViewModel)


                RequestStateComponent(
                    requestState = dishesState,
                    byError = {
                        ShowErrorComponent(message = dishesState?.message, onButtonClick = { mainViewModel.getMainContent() })
                    }
                )

                ToolbarScrollComponent(navController, mainViewModel)
            }
        }
    )
}