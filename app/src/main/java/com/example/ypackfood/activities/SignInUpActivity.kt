package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ypackfood.R
import com.example.ypackfood.common.Auth
import com.example.ypackfood.components.*
import com.example.ypackfood.extensions.translateError
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.AuthorizationData
import com.example.ypackfood.models.auth.RegistrationData
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.sealedClasses.SignOptions
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.SignInUpViewModel


@Composable
fun SignInUpScreen(
    navController: NavHostController,
    signViewModel: SignInUpViewModel,
    datastoreViewModel: DatastoreViewModel
) {
    val signState = signViewModel.signSwitcherState.observeAsState().value!!
    val registerState = signViewModel.registerState.observeAsState().value
    val authInfoState = datastoreViewModel.authInfoState.observeAsState().value


    LaunchedEffect(true) {
        Log.d("registerStateInit", "registerStateInit")
        signViewModel.registerStateInit()
        datastoreViewModel.clearAuthInfo()
    }

    LaunchedEffect(authInfoState) {
        if (authInfoState is NetworkResult.Success<*>) {
            Log.d("SignInUp LaunchedEffect(authInfoState)", Auth.authInfo.toString())
            navController.navigate(route = Screens.Main.route) {
                popUpTo(Screens.SignInUp.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(registerState) {
        if (registerState is NetworkResult.Success<*>) {
            Log.d("SignInUp", "${registerState.data}")
            Log.d("SignInUp", "registerState is NetworkResult.Success<*>")
//            datastoreViewModel.setAuthInfoState(
//                AuthInfo(
//                    personId = registerState.data!!.personId,
//                    accessToken = registerState.data.accessToken,
//                    refreshToken = registerState.data.refreshToken,
//                )
//            )
            datastoreViewModel.updateAuthInfo(
                idValue = registerState.data!!.personId,
                tokenValue = registerState.data.accessToken,
                refreshTokenValue = registerState.data.refreshToken
            )
            Log.d("SignInUp LaunchedEffect(registerState)", Auth.authInfo.toString())
//            navController.navigate(route = Screens.Main.route) {
//                popUpTo(Screens.SignInUp.route) { inclusive = true }
//            }
        }
    }

    Scaffold (
        topBar = { ToolbarEasyComponent() },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                content = {
                    when (registerState) {
                        is NetworkResult.Loading<*> -> { LoadingBarComponent() }
                        is NetworkResult.Empty<*> -> {}
                        is NetworkResult.HandledError<*>, is NetworkResult.Error<*>, null -> {
                            TabRowComponent(
                                currentOption = signState,
                                listOptions = SignOptions.getOptions(),
                                onClick = { newChosenOption ->
                                    signViewModel.signSwitcherState.postValue(newChosenOption)
                                    signViewModel.clearErrorEntering()
                                }
                            )
                            when(signState) {
                                is SignOptions.SignIn -> {
                                    SignFormComponent(
                                        signViewModel = signViewModel,
                                        buttonText = stringResource(R.string.sign_in_btn),
                                        onClick = {
                                            signViewModel.authorizeUser(
                                                AuthorizationData(
                                                    phoneNumber = signViewModel.phoneFieldState,
                                                    password = signViewModel.passwordFieldState)
                                            )
                                        }
                                    )
                                }
                                is SignOptions.SignUp -> {
                                    SignUpFormComponent(
                                        signViewModel = signViewModel,
                                        buttonText = stringResource(R.string.sign_up_btn),
                                        onClick = {
                                            signViewModel.registerUser(
                                                RegistrationData(
                                                    phoneNumber = signViewModel.phoneFieldState,
                                                    password = signViewModel.passwordFieldState,
                                                    name = signViewModel.userFieldState,
                                                )
                                            )
                                            Log.d("SignInUp", "Регистрация успешна")
                                        }
                                    )
                                }
                                else -> {}
                            }
                        }
                        else -> {}
                    }
                }
            )
        }
    )
}

@Composable
fun FieldsComponent(
    signViewModel: SignInUpViewModel,
    errorMessage: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        content = {
            TextFieldComponent(
                modifier = Modifier.fillMaxWidth(),
                currentValue = signViewModel.phoneFieldState,
                onValueChange = { value -> signViewModel.setPhoneField(value) },
                placeholder =  stringResource(R.string.enter_number_field),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldComponent(
                modifier = Modifier.fillMaxWidth(),
                currentValue = signViewModel.passwordFieldState,
                onValueChange = { value -> signViewModel.setPasswordField(value) },
                placeholder =  stringResource(R.string.enter_password_field),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )
            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = errorMessage.translateError())//signViewModel.translateError(errorMessage))
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    )
}

@Composable
fun FieldsUserComponent(signViewModel: SignInUpViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        content = {
            TextFieldComponent(
                modifier = Modifier.fillMaxWidth(),
                currentValue = signViewModel.userFieldState,
                onValueChange = { value -> signViewModel.setUserField(value) },
                placeholder =  stringResource(R.string.enter_userName_field),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
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
            FieldsComponent(signViewModel, signViewModel.errorEnteringState)
            Spacer(modifier = Modifier.height(10.dp))
            ButtonComponent(
                text = buttonText,
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (signViewModel.validateFields(
                            phone = signViewModel.phoneFieldState,
                            password = signViewModel.passwordFieldState)
                    ) onClick()
                }
            )
        }
    )
}

@Composable
fun SignUpFormComponent(
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
            FieldsUserComponent(signViewModel)
            Spacer(modifier = Modifier.height(10.dp))
            FieldsComponent(signViewModel, signViewModel.errorEnteringState)
            Spacer(modifier = Modifier.height(10.dp))
            ButtonComponent(
                text = buttonText,
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (signViewModel.validateFields(
                            name = signViewModel.userFieldState,
                            phone = signViewModel.phoneFieldState,
                            password = signViewModel.passwordFieldState)
                    )  onClick()
                }
            )
        }
    )
}
