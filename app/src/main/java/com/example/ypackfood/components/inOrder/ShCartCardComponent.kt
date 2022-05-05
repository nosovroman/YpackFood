package com.example.ypackfood.components.inOrder

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ypackfood.components.CounterComponent
import com.example.ypackfood.components.PictureOneComponent
import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.viewModels.RoomViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun ShCartCardComponent(
    cartDish: CartDish,
    cartViewModel: ShoppingCartViewModel,
    roomViewModel: RoomViewModel
) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        content = {
            PictureOneComponent(url = cartDish.urlPicture)
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = cartDish.name, fontSize = 16.sp)
                Text(
                    text = "${cartDish.price * cartDish.count} ₽",
                    fontSize = 14.sp
                )

                CounterComponent(
                    count = cartDish.count,
                    lowerLimit = 0,
                    onIncClick = {  //updateRoom
                        roomViewModel.updateCart(
                            composeCartEntity(cartDish = cartDish, countIncrement = 1)
                        )
                    },
                    onDecClick = {  //updateOrDeleteRoom
                        if (cartDish.count > 1) {  // update
                            roomViewModel.updateCart(
                                composeCartEntity(cartDish = cartDish, countIncrement = -1)
                            )
                        } else {  // delete
                            roomViewModel.deleteFromCart(
                                composeCartEntity(cartDish = cartDish)
                            )
                        }
                    }
                )
            }
        }
    )
}

fun composeCartEntity(cartDish: CartDish, countIncrement: Int = 0): CartEntity {
    return with(cartDish) {
        CartEntity(
            dishId = dishId,
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count + countIncrement,
            shoppingCartId = shoppingCartId
        )
    }
}