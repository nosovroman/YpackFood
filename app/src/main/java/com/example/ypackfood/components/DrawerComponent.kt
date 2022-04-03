package com.example.ypackfood.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.enumClasses.MainDrawer
import com.example.ypackfood.enumClasses.getDrawerItems

@Composable
fun DrawerComponent(navController: NavHostController) {
    val itemsDrawer = getDrawerItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp),
        content = {
            itemsDrawer.forEach { currentItem ->
                DrawerItemComponent(item = currentItem) {
                    // переход к нужной странице
                    navController.navigate(route = currentItem.route)
                }
            }
        }
    )
}

@Composable
fun DrawerItemComponent(item: MainDrawer, onItemClick: () -> Unit) {
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