package com.example.ypackfood.components.inOrder

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ypackfood.components.CounterComponent
import com.example.ypackfood.components.PictureOneComponent
import com.example.ypackfood.viewModels.RoomViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun ShCartCardComponent(
    cardName: String,
    cost: Int,
    count: Int,
    urlPicture: String,
    //ind: Int,
    cartViewModel: ShoppingCartViewModel,
    roomViewModel: RoomViewModel
) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        content = {
            PictureOneComponent(url = urlPicture)
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = cardName, fontSize = 16.sp)
                Text(
                    text = "$cost ₽",
                    fontSize = 14.sp
                )

                CounterComponent(
                    count = count,
                    onIncClick = {  },//updateRoom
                    onDecClick = {  }//updateOrDeleteRoom
                )
            }
        }
    )
}