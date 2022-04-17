package com.example.ypackfood.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants

@Composable
fun ToolbarComponent(navController: NavHostController, rightIcon: @Composable () -> Unit = { Spacer(Modifier.size(45.dp)) }
                     //, rightIconContent: @Composable () -> Unit = {}, onRightIconClick: () -> Unit = {}
) {
    TopAppBar (
        modifier = Modifier
            .height(Constants.TOOLBAR_HEIGHT),
        content = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                content = {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", modifier = Modifier.size(35.dp))
                }
            )

            Spacer(Modifier.weight(1f, true))
            Text("Упак.Еда", fontSize = 22.sp)
            Spacer(Modifier.weight(1f, true))

            rightIcon()
        }
    )
}