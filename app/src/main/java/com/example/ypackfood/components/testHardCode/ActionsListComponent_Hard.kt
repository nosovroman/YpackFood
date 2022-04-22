package com.example.ypackfood.components.testHardCode

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.PictureOneComponent

@Composable
fun ActionsListComponent_Hard() {
    LazyRow(
        content = {
            items(count = 10) {
                Row(
                    content = {
                        PictureOneComponent(size = 200.dp, url = Constants.baseUrlPictureCategory)
                        if (it != 9) Spacer(modifier = Modifier.width(10.dp))
                    }
                )
            }
        }
    )
}