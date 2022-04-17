package com.example.ypackfood.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.ypackfood.R
import com.example.ypackfood.common.Constants

@Composable
fun PictureOneComponent(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    corner: Dp = 15.dp,
    description: String = "",
    url: String = Constants.baseUrlPictureContent,
) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                placeholder(R.mipmap.pizza)
                //crossfade(1000) -- bug with render actual data
            }
        ),
        contentDescription = description,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .border(1.dp, color = Color.Cyan)
    )
}