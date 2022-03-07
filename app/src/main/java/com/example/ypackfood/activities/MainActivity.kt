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
    lateinit var listContentState: LazyListState
        private set
    lateinit var listCategoryState: LazyListState
        private set

    fun listContentStateInit(newListState: LazyListState) {
        listContentState = newListState
    }
    fun listCategoryStateInit(newListState: LazyListState) {
        listCategoryState = newListState
    }
}

@Composable
fun MainScreen() {
    val mvvmViewModel = MvvmViewModel()
    mvvmViewModel.listContentStateInit(rememberLazyListState())
    mvvmViewModel.listCategoryStateInit(rememberLazyListState())

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
    val coroutineScope2 = rememberCoroutineScope()
    val chosenCategoryIndex = limits.indexOf(limits.find { mvvmViewModel.listContentState.firstVisibleItemIndex in it.first..it.second })
    //Log.d("|||her", chosenCategory.toString())
    LaunchedEffect(chosenCategoryIndex) {
        coroutineScope2.launch {
            mvvmViewModel.listCategoryState.animateScrollToItem(chosenCategoryIndex)
        }
    }


    LazyRow(state = mvvmViewModel.listCategoryState)
    {
        itemsIndexed(getAllCategories()) { index, item ->
            val isChosen = index == chosenCategoryIndex
            Spacer(modifier = Modifier.padding(start = 5.dp))
            CategoryComponent(mvvmViewModel = mvvmViewModel, categoryName = item, positionInContent = limits[index].first, isChosen = isChosen)
        }
    }
}

@Composable
fun CategoryComponent(mvvmViewModel: MvvmViewModel, categoryName: MainCategory, positionInContent: Int, isChosen: Boolean) {
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                mvvmViewModel.listContentState.animateScrollToItem(positionInContent)
                Log.d("HelloBtn---", positionInContent.toString())
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isChosen) {
                //Log.d("her $currentIndex: ", chosenCategory.toString())
                MaterialTheme.colors.primary
            } else MaterialTheme.colors.background
        )
    ) {
        Text(text = categoryName.categoryName)
    }
}

@Composable
fun ContentListComponent(mvvmViewModel: MvvmViewModel) {
    LazyColumn (state = mvvmViewModel.listContentState) {
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