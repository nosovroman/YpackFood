package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants.TOOLBAR_HEIGHT
import com.example.ypackfood.common.Constants.baseUrlPictureCategory
import com.example.ypackfood.common.Constants.mergedList
import com.example.ypackfood.components.ContentCardComponent
import com.example.ypackfood.components.PictureOneComponent
import com.example.ypackfood.enumClasses.MainDrawer
import com.example.ypackfood.enumClasses.getDrawerItems
import com.example.ypackfood.viewModels.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


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

    Scaffold(
        scaffoldState = mainViewModel.scaffoldState,
        drawerContent = { Drawer(navController) },
        content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
            ) {

                ContentListComponent(mainViewModel)
                CategoriesRowComponent(mainViewModel)
                ToolBarComponent(mainViewModel)
            }
        }
    )
}

@Composable
fun Drawer(navController: NavHostController) {
    val itemsDrawer = getDrawerItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp),
        content = {
            itemsDrawer.forEach { currentItem ->
                DrawerItem(item = currentItem) {
                    // переход к нужной странице
                    navController.navigate(route = currentItem.route)
                }
            }
        }
    )
}

@Composable
fun DrawerItem(item: MainDrawer, onItemClick: () -> Unit) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clickable { onItemClick() }
            .padding(start = 0.dp)
    ) {
        Icon(
            painterResource(item.icon),
            contentDescription = item.itemName
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = item.itemName, fontSize = 18.sp)
    }
}

@Composable
fun ToolBarComponent(mvvmViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()

    TopAppBar (modifier = Modifier
        .height(TOOLBAR_HEIGHT)
        .offset { IntOffset(x = 0, y = mvvmViewModel.toolbarOffsetState.roundToInt()) }) {
        IconButton(
            onClick = {
                scope.launch {
                    mvvmViewModel.scaffoldState.drawerState.open()
                }
            },
            content = {
                Icon(Icons.Filled.Menu, contentDescription = "Меню")
            }
        )

        Spacer(Modifier.weight(1f, true))
        Text("Упак.Еда", fontSize = 22.sp)
        Spacer(Modifier.weight(1f, true))

        IconButton(
            onClick = { },
            content = {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = "Корзина", Modifier.scale(-1.0f, 1.0f) )
            }
        )
    }
}

@Composable
fun CategoriesRowComponent(mvvmViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val chosenCategoryIndex = mvvmViewModel.listContentState.firstVisibleItemIndex

    LaunchedEffect(chosenCategoryIndex) {
        scope.launch {
            mvvmViewModel.listCategoryState.animateScrollToItem(chosenCategoryIndex)
        }
    }

    LazyRow(
        state = mvvmViewModel.listCategoryState,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .offset { IntOffset(x = 0, y = mvvmViewModel.toolbarOffsetState.roundToInt()) },
        contentPadding = PaddingValues(top = TOOLBAR_HEIGHT),
        content = {
            itemsIndexed(listOf("Акции") + mvvmViewModel.categoriesContentState.map { it.categoryType }) { index, item ->
                val isChosen = index == chosenCategoryIndex
                Spacer(modifier = Modifier.padding(start = 5.dp))
                CategoryComponent(mvvmViewModel = mvvmViewModel, categoryName = item, positionInContent = index, isChosen = isChosen)
            }
        }
    )
}

@Composable
fun CategoryComponent(mvvmViewModel: MainViewModel, categoryName: String, positionInContent: Int, isChosen: Boolean) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                mvvmViewModel.listContentState.animateScrollToItem(positionInContent)
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isChosen) {
                MaterialTheme.colors.primary
            } else MaterialTheme.colors.background
        ),
        content = {
            Text(text = categoryName)
        }
    )
}

@Composable
fun ContentListComponent(mvvmViewModel: MainViewModel) {
    Log.d("getMainContent ", mvvmViewModel.categoriesContentState.toString())
    val offset = with(LocalDensity.current) { -mvvmViewModel.toolbarOffsetState.roundToInt().toDp() }
    Log.d("her padding: ", "${TOOLBAR_HEIGHT - offset}")
    LazyColumn (
        state = mvvmViewModel.listContentState,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = TOOLBAR_HEIGHT+TOOLBAR_HEIGHT-offset)
    ) {
        item {
            LazyRow {
                items(count = 10) {
                    Row(
                        content = {
                            PictureOneComponent(size = 200.dp, url = baseUrlPictureCategory)
                            if (it != 9) Spacer(modifier = Modifier.width(10.dp))
                        }
                    )
                }
            }
        }
        itemsIndexed(mvvmViewModel.categoriesContentState) { index, item ->
            //val countCategoryDishes = item.dishes.size
            for (content in item.dishes) {
                with (content) {
                    ContentCardComponent(
                        cardName = name + id,
                        hint = basePortion.size,
                        description = composition,
                        price = basePortion.price,
                        urlPicture = picturePaths.large
                    )
                }
            }

            if (index < mergedList.size - 1) {
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}