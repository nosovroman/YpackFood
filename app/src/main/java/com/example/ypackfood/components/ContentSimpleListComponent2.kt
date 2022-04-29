package com.example.ypackfood.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.ypackfood.components.inOrder.ShCartCardComponent
import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.viewModels.RoomViewModel
import com.example.ypackfood.viewModels.ShoppingCartViewModel

@Composable
fun ContentSimpleListComponent2(
    contentList: List<CartDish>,
    cartViewModel: ShoppingCartViewModel,
    roomViewModel: RoomViewModel
) {
    LazyColumn {
        items (contentList) { item ->
            ShCartCardComponent(
                cartDish = item,
                cartViewModel = cartViewModel,
                roomViewModel = roomViewModel
            )
        }
    }
}