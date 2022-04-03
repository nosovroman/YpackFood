package com.example.ypackfood.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.dataClasses.mainContent2.Category
import com.example.ypackfood.viewModels.MainViewModel
import kotlin.math.roundToInt

@Composable
fun ContentListComponent(navController: NavHostController, mvvmViewModel: MainViewModel) {
    Log.d("getMainContent ", mvvmViewModel.contentResp.toString())
    val offset = with(LocalDensity.current) { -mvvmViewModel.toolbarOffsetState.roundToInt().toDp() }
    Log.d("her padding: ", "${Constants.TOOLBAR_HEIGHT - offset}")
    LazyColumn (
        state = mvvmViewModel.listContentState,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = Constants.TOOLBAR_HEIGHT + Constants.TOOLBAR_HEIGHT -offset)
    ) {
        item {
            LazyRow {
                items(count = 10) {
                    Row(
                        content = {
                            PictureOneComponent(size = 200.dp, url = Constants.baseUrlPictureCategory)
                            if (it != 9) Spacer(modifier = Modifier.width(10.dp))
                        }
                    )
                }
            }
        }
        itemsIndexed(mvvmViewModel.contentResp.value!!.data as MutableList<Category>) { index, item ->
            //val countCategoryDishes = item.dishes.size
            for (content in item.dishes) {
                with (content) {
                    ContentCardComponent(
                        contentCardId = id,
                        cardName = name + id,
                        hint = basePortion.size,
                        description = composition,
                        price = basePortion.price,
                        urlPicture = picturePaths.large,
                        navController = navController
                    )
                }
            }

            if (index < Constants.mergedList.size - 1) {
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}