package com.example.ypackfood.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.MainViewModel

@Composable
fun ActionsRowComponent(navController: NavHostController, mainViewModel: MainViewModel) {
    LazyRow {
        itemsIndexed(mainViewModel.contentResp2.value!!.data as MutableList<ActionsItem>) { index, item ->
            val count = mainViewModel.contentResp2.value!!.data!!.size
            Row(
                content = {
                    PictureOneComponent(
                        size = 200.dp,
                        url = item.picturePaths.large,
                        modifier = Modifier.clickable { navController.navigate(Screens.Offers.createRoute(item.id)) }
                    )
                    if (index != count - 1) Spacer(modifier = Modifier.width(10.dp))
                }
            )
        }
    }
}