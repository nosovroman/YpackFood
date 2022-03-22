package com.example.ypackfood.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.ypackfood.common.Constants

@Composable
fun PictureOneComponent(
    size: Dp = 120.dp,
    corner: Dp = 15.dp,
    description: String = "",
    url: String = Constants.baseUrlPictureContent
) {
    Image(
        painter = rememberImagePainter(url),
        contentDescription = description,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
    )
}