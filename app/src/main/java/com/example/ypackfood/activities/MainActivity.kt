package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.ypackfood.common.Constants.mergedList
import com.example.ypackfood.enumClasses.MainCategory
import com.example.ypackfood.enumClasses.getAllCategories
import kotlinx.coroutines.launch

class MvvmViewModel : ViewModel() {
    lateinit var listState: LazyListState
        private set
    lateinit var listState2: LazyListState
        private set

    fun listStateInit(lazyListState: LazyListState) {
        listState = lazyListState
    }
    fun listState2Init(lazyListState: LazyListState) {
        listState2 = lazyListState
    }
}

@Composable
fun MainScreen() {
    val mvvmViewModel = MvvmViewModel()
    mvvmViewModel.listState2Init(rememberLazyListState())

    Column (modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
        CategoriesRowComponent(mvvmViewModel)
        Spacer(modifier = Modifier.height(10.dp))
        ContentListComponent(mvvmViewModel)
    }
}


@Composable
fun CategoriesRowComponent(mvvmViewModel: MvvmViewModel) {
    val limits = listOf(Pair(0,2), Pair(3,5), Pair(6,8), Pair(9,11), Pair(12,14))
    LazyRow(state = mvvmViewModel.listState2) //(state = mvvmViewModel.listState2)
    {
        itemsIndexed(getAllCategories()) { index, item ->
            Spacer(modifier = Modifier.padding(start = 5.dp))
            CategoryComponent(categoryName = item, limit = limits[index], mvvmViewModel = mvvmViewModel)
        }
    }
}

@Composable
fun CategoryComponent(categoryName: MainCategory, limit: Pair<Int, Int>, mvvmViewModel: MvvmViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val coroutineScope2 = rememberCoroutineScope()
    val currentIndex = mvvmViewModel.listState.firstVisibleItemIndex
    Button(
        onClick = {
            coroutineScope.launch {
                mvvmViewModel.listState.animateScrollToItem(limit.first)
//                mvvmViewModel.listState2.animateScrollToItem(
//                    MainCategory.values().indexOf(categoryName))
                Log.d("HelloBtn---", limit.first.toString())
            }
            coroutineScope2.launch {
                mvvmViewModel.listState2.animateScrollToItem(
                    MainCategory.values().indexOf(categoryName))
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (currentIndex in limit.first..limit.second) {
                MaterialTheme.colors.primary
            } else MaterialTheme.colors.background
        )
    ) {
        Text(text = categoryName.categoryName)
    }
}

@Composable
fun ContentListComponent(mvvmViewModel: MvvmViewModel) {
    mvvmViewModel.listStateInit(rememberLazyListState())
    val highPriorityTasks = mvvmViewModel.listState

    LazyColumn (state = highPriorityTasks) {
        itemsIndexed(mergedList) { index, item ->
            ContentCardComponent(cardName = item)
            if (index < mergedList.size - 1) {
                Spacer(modifier = Modifier.padding(start = 5.dp))
                Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun ContentCardComponent(cardName: String) {
    Row(modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(170.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = cardName, color = MaterialTheme.colors.onBackground)
        }
    }
}


//    val highPriorityTasks by remember {
//        derivedStateOf {
//            mvvmViewModel.setFirstVisibleCardIndex(mvvmViewModel.listState.firstVisibleItemIndex)
//            Log.d("Hello3", mvvmViewModel.listState.firstVisibleItemIndex.toString())
//            mvvmViewModel.listState
//        }
//    }