package com.example.ypackfood.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ypackfood.common.Constants
import com.example.ypackfood.viewModels.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
        contentPadding = PaddingValues(top = Constants.TOOLBAR_HEIGHT),
        content = {
            mvvmViewModel.dishesState.value?.data?.let {
                itemsIndexed(listOf("Акции") + mvvmViewModel.dishesState.value!!.data!!.map { it.categoryType }) { index, item ->
                    val name = when (item) {
                        "Акции" -> "Акции"
                        "BURGERS" -> "Бургеры"
                        "PIZZA" -> "Пицца"
                        "ROLLS" -> "Роллы"
                        "COMBO" -> "Комбо"
                        else -> "Новинки"
                    }
                    val isChosen = index == chosenCategoryIndex
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    CategoryComponent(mvvmViewModel = mvvmViewModel, categoryName = name, positionInContent = index, isChosen = isChosen)
                }
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