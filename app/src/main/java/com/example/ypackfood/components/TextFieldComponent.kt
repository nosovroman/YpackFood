package com.example.ypackfood.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldComponent(
    //getCurrentValue: () -> String,
    currentValue: String,
    onValueChange: (newValue: String) -> Unit,
    placeholder: String = "",
    maxLines: Int = 3,
    shape: CornerBasedShape = RoundedCornerShape(10.dp)
) {
    TextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            //backgroundColor = Invisible,
            //cursorColor = HintColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.onBackground,
                shape = shape
            ),
        value = currentValue,
        onValueChange = { onValueChange(it) },
        shape = shape,
        //singleLine = true,
        maxLines = maxLines,
        textStyle = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 20.sp),
        placeholder = { Text(text = placeholder) }
    )
}