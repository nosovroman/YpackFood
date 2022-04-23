package com.example.ypackfood.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun ContentSimpleListComponent(contentList: List<Dish>, showPrice: Boolean = false, onItemClick: (id: Int) -> Unit = {}) {
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