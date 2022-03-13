package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.ypackfood.common.Constants.TOOLBAR_HEIGHT
import com.example.ypackfood.common.Constants.mergedList
import com.example.ypackfood.enumClasses.MainCategory
import com.example.ypackfood.enumClasses.MainDrawer
import com.example.ypackfood.enumClasses.getAllCategories
import com.example.ypackfood.enumClasses.getDrawerItems
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {
    lateinit var listContentState: LazyListState
        private set
    lateinit var listCategoryState: LazyListState
        private set
    lateinit var scaffoldState: ScaffoldState
        private set
    var toolbarOffsetState by mutableStateOf(0f)
        private set

    fun listContentStateInit(newListState: LazyListState) {
        listContentState = newListState
    }
    fun listCategoryStateInit(newListState: LazyListState) {
        listCategoryState = newListState
    }
    fun scaffoldStateInit(newScaffoldState: ScaffoldState) {
        scaffoldState = newScaffoldState
    }
    fun setToolbarOffset(newOffsetPx: Float) {
        toolbarOffsetState = newOffsetPx
    }

}

@Composable
fun MainScreen(navController: NavHostController) {
    val mainViewModel = MainViewModel()
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
    var limits = listOf(Pair(-1,2), Pair(3,5), Pair(6,8), Pair(9,11), Pair(12,14))
    limits = limits.map { each -> Pair(each.first+1, each.second+1) }
    Log.d("her", limits.toString())
    val scope = rememberCoroutineScope()
    var chosenCategoryIndex = limits.indexOf(limits.find { mvvmViewModel.listContentState.firstVisibleItemIndex in it.first..it.second })
    if (chosenCategoryIndex < 0) chosenCategoryIndex = 0

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
            itemsIndexed(getAllCategories()) { index, item ->
                val isChosen = index == chosenCategoryIndex
                Spacer(modifier = Modifier.padding(start = 5.dp))
                CategoryComponent(mvvmViewModel = mvvmViewModel, categoryName = item, positionInContent = limits[index].first, isChosen = isChosen)
            }
        }
    )
}

@Composable
fun CategoryComponent(mvvmViewModel: MainViewModel, categoryName: MainCategory, positionInContent: Int, isChosen: Boolean) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                mvvmViewModel.listContentState.animateScrollToItem(positionInContent)
                Log.d("HelloBtn---", positionInContent.toString())
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isChosen) {
                //Log.d("her $currentIndex: ", chosenCategory.toString())
                MaterialTheme.colors.primary
            } else MaterialTheme.colors.background
        ),
        content = {
            Text(text = categoryName.categoryName)
        }
    )
}

@Composable
fun ContentListComponent(mvvmViewModel: MainViewModel) {
    val offset = with(LocalDensity.current) { -mvvmViewModel.toolbarOffsetState.roundToInt().toDp() }
    Log.d("her padding: ", "${TOOLBAR_HEIGHT - offset}")
    LazyColumn (
        state = mvvmViewModel.listContentState,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = TOOLBAR_HEIGHT+TOOLBAR_HEIGHT-offset),// top = TOOLBAR_HEIGHT
        //contentPadding = PaddingValues(top = TOOLBAR_HEIGHT+TOOLBAR_HEIGHT-offset),
    ) {
        item {
            LazyRow {
                items(count = 10) {
                    Row(
//                        modifier = Modifier
//                            .border(width = 2.dp, color = MaterialTheme.colors.onBackground),
                        content = {
                            Image(
                                painter = rememberImagePainter(
                                    "https://sun9-26.userapi.com/impf/c849020/v849020562/12056a/xOiO0cHdtkk.jpg?size=604x604&quality=96&sign=2c11f0e48c64e290d0bde943da845fd6&type=album"
                                ),
                                contentDescription = "Вкусняха",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(15.dp))
                            )
                            if (it != 9) Spacer(modifier = Modifier.width(10.dp))
                        }
                    )
                }
            }
        }
        itemsIndexed(mergedList) { index, item ->
            ContentCardComponent(cardName = item)
            if (index < mergedList.size - 1) {
                Spacer(modifier = Modifier.height(5.dp))
                //Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun ContentCardComponent(cardName: String) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        content = {
            Image(
                painter = rememberImagePainter(
                    "https://i09.fotocdn.net/s116/8d62d46b0c71620f/public_pin_l/2635032180.jpg"
                ),
                contentDescription = "Вкусняха",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = "Мегавкусное блюдо $cardName", fontSize = 16.sp)
                Text(text = "30 см / 540 г", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "Картошечка, смесь сыров чеддер и пармезан, моцарелла, бекон, соус альфредо, томаты, берем пучок укропу и пицца готова, оцарелла, бекон, соус альфредо, томаты, берем пучок укропу и пицца готова",
                    fontSize = 14.sp
                )
                OutlinedButton(onClick = {  }) {
                    Text(text = "от 369 ₽")
                }
            }
        }
    )
}


//    val highPriorityTasks by remember {
//        derivedStateOf {
//            mvvmViewModel.setFirstVisibleCardIndex(mvvmViewModel.listState.firstVisibleItemIndex)
//            Log.d("Hello3", mvvmViewModel.listState.firstVisibleItemIndex.toString())
//            mvvmViewModel.listState
//        }
//    }