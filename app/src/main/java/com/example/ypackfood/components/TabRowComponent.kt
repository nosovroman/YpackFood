package com.example.ypackfood.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ypackfood.sealedClasses.TabRowSwitchable

@Composable
fun TabRowComponent(
    currentOption: TabRowSwitchable,
    listOptions: List<TabRowSwitchable>,
    onClick: (newChosenOption: TabRowSwitchable) -> Unit
) {
    //val listOptions = DeliveryOptions.getOptions()
    val state = currentOption.index

    TabRow(
        selectedTabIndex = state,
        modifier = Modifier.padding(horizontal = 20.dp).height(40.dp),
        backgroundColor = Color.Transparent,
        indicator = {  },
        divider = {  }
    ) {
        listOptions.forEachIndexed { index, item ->
            Tab(
                text = { Text(item.title) },
                selected = state == index,
                onClick = { onClick(currentOption.getByIndex(index)) },
                selectedContentColor = Color.White,
                unselectedContentColor = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .background(
                        color = if (state == index) MaterialTheme.colors.primary else Color.Transparent,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
            )
        }
    }
}