package com.example.ypackfood.components.specific

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
import com.example.ypackfood.components.PictureOneComponent
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun ActionsRowComponent(navController: NavHostController, actionList: MutableList<ActionsItem>) {
    LazyRow {
        itemsIndexed(actionList) { index, item ->
            val count = actionList.size
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