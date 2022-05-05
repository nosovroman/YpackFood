package com.example.ypackfood.components.specific

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.ContentCardComponent
import com.example.ypackfood.models.mainContent.Category
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun DishesColumnComponent(navController: NavHostController, category: Category, index: Int) {
    for (content in category.dishes) {
        with (content) {
            ContentCardComponent(
                cardName = name + id,
                hint = basePortion.size,
                description = composition,
                price = basePortion.priceNow.price,
                urlPicture = picturePaths.large,
                onCardClick = { navController.navigate(route = Screens.DetailContent.createRoute(id)) }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
    }
}