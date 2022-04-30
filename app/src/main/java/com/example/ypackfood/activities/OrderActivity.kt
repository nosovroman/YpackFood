package com.example.ypackfood.activities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.DELIVERY_COST
import com.example.ypackfood.components.ToolbarComponent
import com.example.ypackfood.components.inOrder.OrderButtonComponent
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun OrderScreen(navController: NavHostController, totalCost: Int) {
    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Order.title) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            OrderButtonComponent(
                totalCost = if (true) totalCost + DELIVERY_COST else totalCost,
                onClick = {})
        },
        content = {
            OrderContent()
        }
    )
}

@Preview
@Composable
fun OrderContent() {
    LazyColumn (
        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        content = {
            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Доставка по адресу", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Коммунистический 110", fontSize = 18.sp)
            }

            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Как можно скорее/ко времени", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Коммунистический 110", fontSize = 18.sp)
            }

            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Способ оплаты", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Наличными", fontSize = 18.sp)
            }

            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Комментарий", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Пройти к турникетам", fontSize = 18.sp)
            }
        }
    )
}