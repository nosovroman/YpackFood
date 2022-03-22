package com.example.ypackfood.activities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ypackfood.components.BackFABComponent

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()
    Scaffold {
        Column (
            Modifier
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Text("revenger13")
            ListAddressesComponent()
        }
        BackFABComponent()
    }
}

@Composable
fun ListAddressesComponent() {
    Text(text = "Список адресов")
    DeliveryAddressComponent()
    DeliveryAddressComponent()
    DeliveryAddressComponent()
    DeliveryAddressComponent()
    DeliveryAddressComponent()
}

@Composable
fun DeliveryAddressComponent() {
    Row (
    ) {
        Text(text = "Таганрог, ул. Чухова, 22")
        IconButton(
            onClick = {  },
            content = {
                Icon(Icons.Outlined.Delete, contentDescription = "Удалить")
            }
        )
    }
}