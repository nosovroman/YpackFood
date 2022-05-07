package com.example.ypackfood.components.inOrder

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.viewModels.MainViewModel
import kotlin.math.roundToInt

@Composable
fun ContentListComponent2(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    itemsOfList: LazyListScope. () -> Unit
) {
    val offset = with(LocalDensity.current) { -mainViewModel.toolbarOffsetState.roundToInt().toDp() }
    LazyColumn (
        state = mainViewModel.listContentState,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = Constants.TOOLBAR_HEIGHT + Constants.TOOLBAR_HEIGHT -offset),
        content = itemsOfList
    )
}