package com.example.ypackfood.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.ypackfood.models.commonData.Dish

@Composable
fun ContentSimpleListComponent(contentList: List<Dish>) {
    Column {
        contentList.forEach { content ->
            with(content) {
                ContentCardComponent (
                    cardName = name,
                    hint = basePortion.size,
                    description = composition,
                    urlPicture = picturePaths.large
                )
            }
        }
    }
}