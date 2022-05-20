package com.example.ypackfood.activities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.components.ToolbarComponent
import com.example.ypackfood.sealedClasses.Screens

@Composable
fun ProfileScreen(navController: NavHostController) {
    //val scrollState = rememberScrollState()
    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Profile.title) },
        content = {
            ProfileContent()
//            Column (
//                Modifier
//                    .padding(start = 20.dp, end = 20.dp),
//      //              .verticalScroll(scrollState),
//                content = {
//
//                    Spacer(modifier = Modifier.size(20.dp))
//                    Text("Роман", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//                    Text("89181231234", fontSize = 18.sp)
//                    Spacer(modifier = Modifier.size(20.dp))
//                    ListAddressesComponent()
//                }
//            )
        }
    )
}

@Composable
fun ProfileContent() {
    LazyColumn (
        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        content = {
            item {
                Spacer(modifier = Modifier.size(20.dp))
                Text("Роман", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            item {
                Text("89181231234", fontSize = 18.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Список адресов", fontSize = 18.sp)
            }
            items(15) {
                DeliveryAddressComponent()
                Divider()
            }
        }
    )
}

@Composable
fun DeliveryAddressComponent() {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Таганрог, ул. Чехова, 22")
        IconButton(
            onClick = {  },
            content = {
                Icon(Icons.Outlined.Delete, contentDescription = "Удалить")
            }
        )
    }
}