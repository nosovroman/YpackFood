package com.example.ypackfood.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropdownMenuComponent(
    chosenItemTitle: String = "",
    expanded: Boolean,
    onMenuClick: () -> Unit,
    items: List<String>,
    onItemClick: (itemName: String) -> Unit,
    onDismissRequest: () -> Unit
) {
    Box {
        TextRectangleComponent(
            text = chosenItemTitle,
            modifier = Modifier.clickable {
                onMenuClick()
                //orderViewModel.setExpandedCity(true)
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            content = {
                items.forEach {
                    DropdownMenuItem(
                        onClick = {
                            onItemClick(it)
                            //orderViewModel.setCity(it)
                            //orderViewModel.setExpandedCity(false)
                        },
                        content = { Text(it) }
                    )
                }
            }
        )
    }
}