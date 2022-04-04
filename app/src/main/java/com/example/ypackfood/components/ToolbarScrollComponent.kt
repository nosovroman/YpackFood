package com.example.ypackfood.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.ypackfood.common.Constants
import com.example.ypackfood.viewModels.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ToolbarScrollComponent(mvvmViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()

    TopAppBar (modifier = Modifier
        .height(Constants.TOOLBAR_HEIGHT)
        .offset { IntOffset(x = 0, y = mvvmViewModel.toolbarOffsetState.roundToInt()) }) {
        IconButton(
            onClick = {
                scope.launch {
                    mvvmViewModel.scaffoldState.drawerState.open()
                }
            },
            content = {
                Icon(Icons.Filled.Menu, contentDescription = "Меню")
            }
        )

        Spacer(Modifier.weight(1f, true))
        Text("Упак.Еда", fontSize = 22.sp)
        Spacer(Modifier.weight(1f, true))

        IconButton(
            onClick = { },
            content = {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = "Корзина", Modifier.scale(-1.0f, 1.0f) )
            }
        )
    }
}