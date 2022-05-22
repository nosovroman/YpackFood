package com.example.ypackfood.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun ContentSimpleListComponent(
    contentList: List<Dish>,
    showPrice: Boolean = false,
    onItemClick: (id: Int) -> Unit = {}
) {
    Column {
        contentList.forEach { content ->
            with(content) {
                ContentCardComponent (
                    cardName = name,
                    hint = basePortion.size,
                    description = composition,
                    urlPicture = picturePaths.large,
                    price = if (showPrice) basePortion.priceNow.price else -1,
                    onCardClick = { onItemClick(id) }
                )
            }
        }
    }
}

@Composable
fun ContentListComponentActions(
    contentList: List<Dish>,
    onItemClick: (item: Dish) -> Unit = {}
) {
    Column {
        contentList.forEach { content ->
            with(content) {
                ActionCardComponent (
                    cardName = name,
                    hint = basePortion.size,
                    description = composition,
                    urlPicture = picturePaths.large,
                    newPrice = basePortion.priceNow.price,
                    oldPrice = basePortion.oldPrice!!.price,
                    onCardClick = { onItemClick(content) }
                )
            }
        }
    }
}
