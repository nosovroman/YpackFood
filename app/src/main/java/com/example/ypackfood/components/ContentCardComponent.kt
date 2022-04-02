package com.example.ypackfood.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants.baseUrlPictureContent
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun ContentCardComponent(
    contentCardId: Int = -1,
    cardName: String,
    hint: String = "",
    description: String = "",
    price: Int = -1,
    urlPicture: String = baseUrlPictureContent,
    navController: NavHostController,
) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(route = Screens.DetailContent.createRoute(contentCardId))
            },
        content = {
            PictureOneComponent(url = urlPicture)
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = cardName, fontSize = 16.sp)
                Text(text = hint, fontSize = 12.sp, color = Color.Gray)
                Text(text = description, fontSize = 14.sp)
                OutlinedButton(onClick = {  }) {
                    Text(text = "от $price ₽")
                }
            }
        }
    )
}