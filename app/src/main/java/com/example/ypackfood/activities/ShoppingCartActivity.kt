package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.components.PictureOneComponent
import com.example.ypackfood.components.ToolbarComponent
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun ShoppingCartScreen(navController: NavHostController, shoppingCartViewModel: ShoppingCartViewModel) {
    val scrollState = rememberScrollState()
    val prices = listOf(100, 200)
    val x = shoppingCartViewModel.count.mapIndexed { index, i -> i * prices[index] }
    val totalCost = x.sum() //shoppingCartViewModel.count.forEachIndexed { index, i ->  }//reduceIndexed { index, acc, i -> acc + (i * prices[index]) }
    Scaffold (
        topBar = {
            ToolbarComponent(navController = navController)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { OrderButtonComponent(totalCost) }
            ) {
        Column (
            Modifier
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState)
        ) {
            ShoppingCartCardComponent(cardName = "ШефБургер Детский", cost = prices[0], ind = 0, shoppingCartViewModel)
            ShoppingCartCardComponent(cardName = "ШефБургер", cost = prices[1], ind = 1, shoppingCartViewModel)
        }
    }
}

@Composable
fun ShoppingCartCardComponent(cardName: String, cost: Int, ind: Int, shoppingCartViewModel: ShoppingCartViewModel) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        content = {
            PictureOneComponent()
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = cardName, fontSize = 16.sp)
                Text(
                    text = "${cost*shoppingCartViewModel.count[ind]} ₽",
                    fontSize = 14.sp
                )

                CounterComponent2(ind, shoppingCartViewModel)
            }
        }
    )
}

//@Composable
//fun CounterComponent(ind: Int, shoppingCartViewModel: ShoppingCartViewModel) {
//    Row() {
//        Button(
//            onClick = { shoppingCartViewModel.decrementCount(ind) },
//            enabled = shoppingCartViewModel.count[ind] != 1,
//            content = { Text("-") },
//        )
//        Text(text = "${shoppingCartViewModel.count[ind]}", modifier = Modifier.align(alignment = Alignment.CenterVertically))
//        Button(
//            onClick = { shoppingCartViewModel.incrementCount(ind) },
//            enabled = shoppingCartViewModel.count[ind] != 5,
//            content = { Text("+") }
//        )
//    }
//}

@Composable
fun CounterComponent2(ind: Int, shoppingCartViewModel: ShoppingCartViewModel)
{
    val counterButtonSize: Dp = 30.dp
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        //onClick = { shoppingCartViewModel.decrementCount(ind) },
        //enabled = shoppingCartViewModel.count[ind] != 1,
        IconButton(
            modifier = Modifier
                .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                .size(counterButtonSize),
            enabled = shoppingCartViewModel.count[ind] > 1,
            onClick = { shoppingCartViewModel.decrementCount(ind) },
            content = {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "-", tint = MaterialTheme.colors.primary)
            }
        )
        //Text(text = "1 шт.", fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp, end = 5.dp))
        Text(text = "${shoppingCartViewModel.count[ind]} шт.", fontSize = 16.sp, modifier = Modifier.align(alignment = Alignment.CenterVertically).padding(start = 5.dp, end = 5.dp))
        IconButton(
            modifier = Modifier
                .background(color = MaterialTheme.colors.primary, shape = CircleShape)
                .size(counterButtonSize),
            enabled = shoppingCartViewModel.count[ind] < 5,
            onClick = { shoppingCartViewModel.incrementCount(ind) },
            content = {
                Icon(imageVector = Icons.Default.Add, contentDescription = "+", tint = MaterialTheme.colors.onPrimary)
            }
        )
    }
}


@Composable
fun OrderButtonComponent(totalCost: Int) {
    ExtendedFloatingActionButton(
        onClick = {  },
        text = { Text(text = "Оформить на $totalCost ₽", color = MaterialTheme.colors.onPrimary)},
        backgroundColor = MaterialTheme.colors.primary,
        //shape = RoundedCornerShape(4.dp)
    )
}