package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ypackfood.R
import com.example.ypackfood.common.Constants.MIN_PASSWORD_LEN
import com.example.ypackfood.common.Constants.REGEX_EMAIL
import com.example.ypackfood.components.*
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.sealedClasses.SignOptions
import com.example.ypackfood.sealedClasses.TabRowSwitchable
import com.example.ypackfood.ui.theme.Shapes

class SignInUpViewModel : ViewModel() {
    var signState: MutableLiveData<TabRowSwitchable> = MutableLiveData(SignOptions.SignIn())

    var errorEnteringState by mutableStateOf("")
        private set
    fun setErrorEntering(newState: String) {
        errorEnteringState = newState
    }
    fun clearErrorEntering() {
        errorEnteringState = ""
    }

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

    fun validateFields(email: String, password: String): Boolean {

        return validateEmail(email) && validatePassword(password)
    }

    fun validateEmail(email: String): Boolean {
        return REGEX_EMAIL.matches(email).also {
            if (!it) setErrorEntering("Некорректный формат почты") else clearErrorEntering()
        }
    }

    fun validatePassword(password: String): Boolean {
        return (password.trim().length >= MIN_PASSWORD_LEN).also {
            if (!it) setErrorEntering("Длина пароля должна быть не менее $MIN_PASSWORD_LEN символов") else clearErrorEntering()
        }
    }
}

@Composable
fun SignInUpScreen(signViewModel: SignInUpViewModel) {

    val signState = signViewModel.signState.observeAsState().value!!

    Scaffold (
        topBar = { ToolbarEasyComponent() },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                content = {
                    TabRowComponent(
                        currentOption = signState,
                        listOptions = SignOptions.getOptions(),
                        onClick = { newChosenOption -> signViewModel.signState.postValue(newChosenOption) }
                    )
                    when(signState) {
                        is SignOptions.SignIn -> {
                            SignFormComponent(
                                signViewModel = signViewModel,
                                buttonText = stringResource(R.string.sign_in_btn),
                                onClick = {
                                    Log.d("SignInUp", "Вход успешен")
                                }
                            )
                        }
                        is SignOptions.SignUp -> {
                            SignFormComponent(
                                signViewModel = signViewModel,
                                buttonText = stringResource(R.string.sign_up_btn),
                                onClick = {
                                    Log.d("SignInUp", "Регистрация успешна")
                                }
                            )
                        }
                        else -> {}
                    }
                }
            )
        }
    )
}

@Composable
fun FieldsComponent(signViewModel: SignInUpViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        content = {
            TextFieldComponent(
                modifier = Modifier.fillMaxWidth(),
                currentValue = signViewModel.emailFieldState,
                onValueChange = { newEmail -> signViewModel.setEmailField(newEmail) },
                placeholder =  stringResource(R.string.enter_email_field),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldComponent(
                modifier = Modifier.fillMaxWidth(),
                currentValue = signViewModel.passwordFieldState,
                onValueChange = { newPassword -> signViewModel.setPasswordField(newPassword) },
                placeholder =  stringResource(R.string.enter_password_field),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )
            if (signViewModel.errorEnteringState.isNotBlank()) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = signViewModel.errorEnteringState)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    )
}

@Composable
fun SignFormComponent(
    signViewModel: SignInUpViewModel,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(15.dp),
        content = {
            FieldsComponent(signViewModel)
            Spacer(modifier = Modifier.height(10.dp))
            ButtonComponent(
                text = buttonText,
                shape = RoundedCornerShape(10.dp),
                onClick = { if (signViewModel.validateFields(signViewModel.emailFieldState, signViewModel.passwordFieldState)) onClick() }
            )
        }
    )
}
