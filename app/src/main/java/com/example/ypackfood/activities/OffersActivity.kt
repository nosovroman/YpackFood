package com.example.ypackfood.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.ypackfood.components.ContentCardComponent

@Composable
fun OffersScreen() {
    val scrollState = rememberScrollState()
    Scaffold {
        Column (
            Modifier
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState)
        ) {

            OfferPicture()
            OfferDescription()
        }
        FloatingActionButton(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            onClick = { }
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Добавить")
        }
    }

}

@Composable
fun OfferPicture() {
    Image(
        painter = rememberImagePainter(
            "https://sun9-26.userapi.com/impf/c849020/v849020562/12056a/xOiO0cHdtkk.jpg?size=604x604&quality=96&sign=2c11f0e48c64e290d0bde943da845fd6&type=album"
        ),
        contentDescription = "Вкусняха",
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun OfferDescription() {
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = "Описание", fontSize = 16.sp)
    Text(text = "Мега выгодное предложение от мегавыгодной сети ресторанов: 500 пицц по цене 1000 роллов", fontSize = 14.sp, color = Color.Gray)
    ContentCardComponent(cardName = "Акция1")
    ContentCardComponent(cardName = "Акция2")
}