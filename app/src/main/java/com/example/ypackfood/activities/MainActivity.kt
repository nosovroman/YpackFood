package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.ypackfood.enumClasses.MainCategory
import com.example.ypackfood.enumClasses.getAllCategories

class MvvmViewModel : ViewModel() {
    var currentCategoryState by mutableStateOf(MainCategory.values()[0].categoryName)
        private set

    fun setCurrentCategory(chosenCategory: String) {
        currentCategoryState = chosenCategory
    }
}

@Composable
fun MainScreen() {
    val mvvmViewModel = MvvmViewModel()

    Column (modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
        CategoriesRowComponent(mvvmViewModel)
        Spacer(modifier = Modifier.height(10.dp))
        ContentListComponent()
    }
}


@Composable
fun CategoriesRowComponent(mvvmViewModel: MvvmViewModel) {
    LazyRow {
        itemsIndexed(getAllCategories()) { index, item ->
            Spacer(modifier = Modifier.padding(start = 5.dp))
            CategoryComponent(categoryName = item.categoryName, mvvmViewModel = mvvmViewModel)
        }
    }
}

@Composable
fun CategoryComponent(categoryName: String, mvvmViewModel: MvvmViewModel) {
    Button(
        onClick = {
            mvvmViewModel.setCurrentCategory(categoryName)
            Log.d("Hello", mvvmViewModel.currentCategoryState)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (categoryName == mvvmViewModel.currentCategoryState)
                MaterialTheme.colors.primary else MaterialTheme.colors.background
        )
    ) {
        Text(text = categoryName)
    }
}

@Composable
fun ContentListComponent() {
    val pizza = listOf("Пицца1", "Пицца2", "Пицца3")
    val roll = listOf("Ролл1", "Ролл2", "Ролл3")
    val watter = listOf("Напиток1", "Напиток2", "Напиток3")
    val mergedList = pizza+roll+watter

    LazyColumn {
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