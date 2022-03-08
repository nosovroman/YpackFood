package com.example.ypackfood.activities

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.ypackfood.common.Constants.mergedList
import com.example.ypackfood.enumClasses.MainCategory
import com.example.ypackfood.enumClasses.getAllCategories
import kotlinx.coroutines.launch

class MvvmViewModel : ViewModel() {
    lateinit var listContentState: LazyListState
        private set
    lateinit var listCategoryState: LazyListState
        private set
    lateinit var scaffoldState: ScaffoldState
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

}

@Composable
fun MainScreen() {
    val mvvmViewModel = MvvmViewModel()
    mvvmViewModel.listContentStateInit(rememberLazyListState())
    mvvmViewModel.listCategoryStateInit(rememberLazyListState())
    mvvmViewModel.scaffoldStateInit(rememberScaffoldState())

    Scaffold(
        scaffoldState = mvvmViewModel.scaffoldState,
        drawerContent = { Drawer() },
        topBar = { ToolBarComponent(mvvmViewModel) },
        content = {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                CategoriesRowComponent(mvvmViewModel)
                Spacer(modifier = Modifier.height(10.dp))
                ContentListComponent(mvvmViewModel)
            }
        }
    )
}

@Composable
fun Drawer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp),
        content = {
            Text("Пункт меню 1", fontSize = 28.sp)
            Text("Пункт меню 2", fontSize = 28.sp)
            Text("Пункт меню 3", fontSize = 28.sp)
        }
    )
}

@Composable
fun ToolBarComponent(mvvmViewModel: MvvmViewModel) {
    val scope = rememberCoroutineScope()

    TopAppBar {
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
fun CategoriesRowComponent(mvvmViewModel: MvvmViewModel) {
    val limits = listOf(Pair(0,2), Pair(3,5), Pair(6,8), Pair(9,11), Pair(12,14))
    val scope = rememberCoroutineScope()
    val chosenCategoryIndex = limits.indexOf(limits.find { mvvmViewModel.listContentState.firstVisibleItemIndex in it.first..it.second })

    LaunchedEffect(chosenCategoryIndex) {
        scope.launch {
            mvvmViewModel.listCategoryState.animateScrollToItem(chosenCategoryIndex)
        }
    }

    LazyRow(
        state = mvvmViewModel.listCategoryState,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
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
fun CategoryComponent(mvvmViewModel: MvvmViewModel, categoryName: MainCategory, positionInContent: Int, isChosen: Boolean) {
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
fun ContentListComponent(mvvmViewModel: MvvmViewModel) {
    LazyColumn (
        state = mvvmViewModel.listContentState,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
        content = {
            itemsIndexed(mergedList) { index, item ->
                ContentCardComponent(cardName = item)
                if (index < mergedList.size - 1) {
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
                }
            }
        }
    )
}

@Composable
fun ContentCardComponent(cardName: String) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        content = {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(170.dp),
                contentAlignment = Alignment.Center,
                content = {
                    Text(text = cardName, color = MaterialTheme.colors.onBackground)
                }
            )
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