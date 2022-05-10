package com.example.ypackfood.components.inOrder

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AnimatedButton() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedBack by infiniteTransition.animateColor (
        initialValue = Color.Red,
        targetValue = Color.Yellow,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        )
    )

    IconButton(
        modifier = Modifier.background(color = animatedBack),
        onClick = {  },
        content = {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "Корзина",
                modifier = Modifier
                    .scale(-1.0f, 1.0f)
                    //.background(color = Color.Yellow)
            )
        }
    )
}