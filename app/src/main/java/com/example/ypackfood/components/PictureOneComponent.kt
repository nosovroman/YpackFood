package com.example.ypackfood.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil.compose.rememberImagePainter

@Composable
fun PictureOneComponent(size: Dp, corner: Dp, description: String, url: String) {
    Image(
        painter = rememberImagePainter(url),
        contentDescription = description,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
    )
}