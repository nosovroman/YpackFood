package com.example.ypackfood.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun ContentCardComponent(cardName: String) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        content = {
            Image(
                painter = rememberImagePainter(
                    "https://i09.fotocdn.net/s116/8d62d46b0c71620f/public_pin_l/2635032180.jpg"
                ),
                contentDescription = "Вкусняха",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = "Мегавкусное блюдо $cardName", fontSize = 16.sp)
                Text(text = "30 см / 540 г", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "Картошечка, смесь сыров чеддер и пармезан, моцарелла, бекон, соус альфредо, томаты, берем пучок укропу и пицца готова, оцарелла, бекон, соус альфредо, томаты, берем пучок укропу и пицца готова",
                    fontSize = 14.sp
                )
                OutlinedButton(onClick = {  }) {
                    Text(text = "от 369 ₽")
                }
            }
        }
    )
}