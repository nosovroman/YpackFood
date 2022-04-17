package com.example.ypackfood.components

import android.util.Log
import androidx.compose.foundation.clickable
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
import com.example.ypackfood.dataClasses.actionsContent.ActionsItem
import com.example.ypackfood.dataClasses.mainContent.Category
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.MainViewModel
import kotlin.math.roundToInt

@Composable
fun ContentListComponent(navController: NavHostController, mvvmViewModel: MainViewModel) {
    Log.d("getMainContent ", mvvmViewModel.contentResp.toString())
    val offset = with(LocalDensity.current) { -mvvmViewModel.toolbarOffsetState.roundToInt().toDp() }
    LazyColumn (
        state = mvvmViewModel.listContentState,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = Constants.TOOLBAR_HEIGHT + Constants.TOOLBAR_HEIGHT -offset)
    ) {
        item {
            LazyRow {
                itemsIndexed(mvvmViewModel.contentResp2.value!!.data as MutableList<ActionsItem>) { index, item ->
                    val count = mvvmViewModel.contentResp2.value!!.data!!.size
                    Row(
                        content = {
                            PictureOneComponent(
                                size = 200.dp,
                                url = item.picturePaths.large,
                                modifier = Modifier.clickable { navController.navigate(Screens.Offers.createRoute(item.id)) },
                                ///onImageClick = { navController.navigate(Screens.Offers.createRoute(item.id)) }
                                //url = Constants.baseUrlPictureCategory
                            )
                            if (index != count - 1) Spacer(modifier = Modifier.width(10.dp))
                        }
                    )
                }
//                items(count = 10) {
//                    Row(
//                        content = {
//                            PictureOneComponent(size = 200.dp, url = Constants.baseUrlPictureCategory)
//                            if (it != 9) Spacer(modifier = Modifier.width(10.dp))
//                        }
//                    )
//                }
            }
        }
        itemsIndexed(mvvmViewModel.contentResp.value!!.data as MutableList<Category>) { index, item ->
            for (content in item.dishes) {
                with (content) {
                    ContentCardComponent(
                        //cardName = name + id,
                        cardName = "Бургер $id",
                        hint = "250 грамм",
                        description = "Булочки для бургера, котлета говяжья, пармезан, помидор, лук синий, майонез, кетчуп",
                        urlPicture = Constants.baseUrlPictureContent,
                        //hint = basePortion.size,
                        //description = composition,
                        price = basePortion.priceNow.price,
                        //urlPicture = picturePaths.large,
                        onCardClick = { navController.navigate(route = Screens.DetailContent.createRoute(id)) }
                    )
                }
            }

            if (index < Constants.mergedList.size - 1) {
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}