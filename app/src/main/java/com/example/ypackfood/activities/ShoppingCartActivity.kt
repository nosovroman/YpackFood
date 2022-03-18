package com.example.ypackfood.activities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.BackFABComponent
import com.example.ypackfood.components.PictureOneComponent
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun ShoppingCartScreen(shoppingCartViewModel: ShoppingCartViewModel) {
    val scrollState = rememberScrollState()
    val prices = listOf(100, 200)
    val x = shoppingCartViewModel.count.mapIndexed { index, i -> i * prices[index] }
    val totalCost = x.sum() //shoppingCartViewModel.count.forEachIndexed { index, i ->  }//reduceIndexed { index, acc, i -> acc + (i * prices[index]) }
    Scaffold (
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { OrderButtonComponent(totalCost) }
            ) {
        Column (
            Modifier
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState)
        ) {
            ShoppingCartCardComponent(cardName = "Блюдо1", cost = prices[0], ind = 0, shoppingCartViewModel)
            ShoppingCartCardComponent(cardName = "Блюдо2", cost = prices[1], ind = 1, shoppingCartViewModel)
        }
        BackFABComponent()
    }
}

@Composable
fun ShoppingCartCardComponent(cardName: String, cost: Int, ind: Int, shoppingCartViewModel: ShoppingCartViewModel) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        content = {
            PictureOneComponent(size = 120.dp, corner = 15.dp, description = "Вкусняха", url = Constants.baseUrlPictureContent)
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = "Мегавкусное блюдо $cardName", fontSize = 16.sp)
                Text(
                    text = "${cost*shoppingCartViewModel.count[ind]} ₽",
                    fontSize = 14.sp
                )
                CounterComponent(ind, shoppingCartViewModel)
            }
        }
    )
}

@Composable
fun CounterComponent(ind: Int, shoppingCartViewModel: ShoppingCartViewModel) {
    Row() {
        Button(
            onClick = { shoppingCartViewModel.decrementCount(ind) },
            enabled = shoppingCartViewModel.count[ind] != 1,
            content = { Text("-") },
        )
        Text(text = "${shoppingCartViewModel.count[ind]}", modifier = Modifier.align(alignment = Alignment.CenterVertically))
        Button(
            onClick = { shoppingCartViewModel.incrementCount(ind) },
            enabled = shoppingCartViewModel.count[ind] != 5,
            content = { Text("+") }
        )
    }
}

@Composable
fun OrderButtonComponent(totalCost: Int) {
    ExtendedFloatingActionButton(
        onClick = {  },
        text = { Text(text = "Оформить на $totalCost ₽", color = MaterialTheme.colors.onPrimary)},
        backgroundColor = MaterialTheme.colors.primary
    )
}