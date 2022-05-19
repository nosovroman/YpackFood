package com.example.ypackfood.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.enumClasses.MainDrawer
import com.example.ypackfood.enumClasses.getDrawerItems
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun DrawerComponent(navController: NavHostController, onExitClick: () -> Unit) {
    val itemsDrawer = getDrawerItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        content = {
            DrawerHeader()
            Divider()
            itemsDrawer.forEach { currentItem ->
                if (itemsDrawer.last() == currentItem) {
                    Spacer(Modifier.weight(1f, true))
                    Divider()
                }
                DrawerItemComponent(
                    item = currentItem,
                    onItemClick = {
                        // переход к нужной странице
                        if (itemsDrawer.last() == currentItem) {
                            Log.d("SignInUp", "onExitClick")
                            onExitClick()
                            navController.navigate(route = Screens.SignInUp.route) {
                                popUpTo(Screens.Main.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(route = currentItem.route) { launchSingleTop = true }
                        }
                    }
                )
            }
        }
    )
}
@Preview
@Composable
fun DrawerHeader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        content = {
            Text(
                text = Constants.APP_NAME_RUS,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 45.sp,
                    fontFamily = Constants.fontFamily,
                    fontStyle = FontStyle.Normal
                )
            )
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
            .padding(start = 0.dp),
        content = {
            Icon(
                painterResource(item.icon),
                contentDescription = item.itemName
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = item.itemName, fontSize = 18.sp)
        }
    )
}