package com.example.ypackfood.components.testHardCode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailDescription_Hard(modifier: Modifier) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "ШефБургер", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))
        // if not combo
        Text(text = "250 грамм", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Описание", fontSize = 16.sp)
        Text(text = "Попробуйте уникальный, сочный, мясисто-булочный очень вкусный бургер, приготовленный нашими лучшими поворами", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))

        // if not combo
        Text(text = "Состав", fontSize = 16.sp)
        Text(text = "Булочки для бургера, котлета говяжья, пармезан, помидор, лук синий, майонез, кетчуп", fontSize = 14.sp, color = Color.Gray)
    }
}