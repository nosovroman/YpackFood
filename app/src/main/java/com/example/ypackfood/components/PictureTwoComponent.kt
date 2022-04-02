package com.example.ypackfood.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun PictureTwoComponent(
    //size: Dp = 120.dp,
    corner: Dp = 15.dp,
    description: String = "",
    url: String = Constants.baseUrlPictureContent
) {
    Image(
        painter = rememberImagePainter(url),
        contentDescription = description,
        modifier = Modifier
            .clip(RoundedCornerShape(corner))
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp),
        contentScale = ContentScale.FillWidth
    )
}