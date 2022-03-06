package com.example.ypackfood.activities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

@Composable
fun MainScreen() {
    Column (modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
        CategoriesRowComponent()
        Spacer(modifier = Modifier.height(10.dp))
        ContentListComponent()
    }
}

@Composable
fun CategoriesRowComponent() {
    val categories = listOf("Пицца", "Роллы", "Напитки")
    //val categoryCount = listOf(2, 3, 4)
    LazyRow {
        itemsIndexed(categories+categories) { index, item ->
            Spacer(modifier = Modifier.padding(start = 5.dp))
            CategoryComponent(categoryName = item)
        }
    }
}

@Composable
fun CategoryComponent(categoryName: String) {
    Button(onClick = {}) {
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