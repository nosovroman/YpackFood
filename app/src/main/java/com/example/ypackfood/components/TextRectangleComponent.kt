package com.example.ypackfood.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ypackfood.ui.theme.Invisible


@Composable
fun TextRectangleComponent(
    modifier: Modifier = Modifier,
    text: String = "TextRectangleComponent",
    invisibleBorder: Boolean = false,
    shape: CornerBasedShape = RoundedCornerShape(10.dp)
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (!invisibleBorder) MaterialTheme.colors.onBackground else Invisible,
                shape = shape
            )
            .padding(10.dp),
        fontSize = 16.sp,
        color = MaterialTheme.colors.onBackground,
    )
}