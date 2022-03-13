package com.example.ypackfood.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.ypackfood.R

class SignInUpViewModel : ViewModel() {
    var emailFieldState by mutableStateOf("")
        private set
    fun setEmailField(newEmailField: String) {
        emailFieldState = newEmailField
    }

    var passwordFieldState by mutableStateOf("")
        private set
    fun setPasswordField(newPasswordField: String) {
        passwordFieldState = newPasswordField
    }
}

@Composable
fun SignInUpScreen() {
    val signViewModel = SignInUpViewModel()

    var state by remember { mutableStateOf(0) }
    val titles = listOf(stringResource(R.string.sign_in_lbl), stringResource(R.string.sign_up_lbl))

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        TabRow(
            selectedTabIndex = state,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            backgroundColor = Color.Transparent,
            indicator = {  },
            divider = {  }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = state == index,
                    onClick = { state = index },
                    selectedContentColor = Color.White,
                    unselectedContentColor = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .background(
                            color = if (state == index) Color.Blue else Color.Transparent,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)//RoundedCornerShape(10.dp)
                        )
                )
            }
        }
        if (state == 0) SignInComponent(signViewModel) else SignUpComponent(signViewModel)
    }
}

@Composable
fun FieldsComponent(signViewModel: SignInUpViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                //backgroundColor = Invisible,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                //cursorColor = HintColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = RoundedCornerShape(10.dp)
                ),
            value = signViewModel.emailFieldState,
            onValueChange = {
                signViewModel.setEmailField(it)
            },
            singleLine = true,
            textStyle = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 20.sp),
            placeholder = { Text(text = stringResource(R.string.enter_email_field)) }
        )
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                //backgroundColor = Invisible,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                //cursorColor = HintColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = RoundedCornerShape(10.dp)
                ),
            value = signViewModel.passwordFieldState,
            onValueChange = {
                signViewModel.setPasswordField(it)
            },
            singleLine = true,
            textStyle = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 20.sp),
            placeholder = { Text(text = stringResource(R.string.enter_password_field)) }
        )
    }
}

@Composable
fun SignInComponent(signViewModel: SignInUpViewModel) {
    Column {
        FieldsComponent(signViewModel)
        Button(onClick = {}) {
            Text(text = stringResource(R.string.sign_in_btn))
        }
    }
}

@Composable
fun SignUpComponent(signViewModel: SignInUpViewModel) {
    Column {
        FieldsComponent(signViewModel)
        Button(onClick = {}) {
            Text(text = stringResource(R.string.sign_up_btn))
        }
    }
}

