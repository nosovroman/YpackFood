package com.example.ypackfood.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.ypackfood.dataClasses.commonData.Dishe

@Composable
fun ContentSimpleListComponent(contentList: List<Dishe>) {
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