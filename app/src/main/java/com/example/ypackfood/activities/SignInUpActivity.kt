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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ypackfood.R
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants.MIN_PASSWORD_LEN
import com.example.ypackfood.common.Constants.REGEX_EMAIL
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.components.*
import com.example.ypackfood.extensions.isDigitsOnly
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.AuthorizationData
import com.example.ypackfood.models.auth.RegistrationData
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.sealedClasses.SignOptions
import com.example.ypackfood.sealedClasses.TabRowSwitchable
import com.example.ypackfood.viewModels.DatastoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SignInUpViewModel : ViewModel() {
    var signSwitcherState: MutableLiveData<TabRowSwitchable> = MutableLiveData(SignOptions.SignIn())

    var registerState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData(NetworkResult.Empty())
    fun registerStateInit() {
        registerState.postValue(NetworkResult.Empty())
    }

    var errorEnteringState by mutableStateOf("")
        private set
    fun setErrorEntering(newState: String) {
        errorEnteringState = newState
    }
    fun clearErrorEntering() {
        errorEnteringState = ""
    }

    var passwordFieldState by mutableStateOf("")
        private set
    fun setPasswordField(newPasswordField: String) {
        passwordFieldState = newPasswordField
    }

    var userFieldState by mutableStateOf("")
        private set
    fun setUserField(newState: String) {
        userFieldState = newState
    }

    var phoneFieldState by mutableStateOf("")
        private set
    fun setPhoneField(newState: String) {
        if (newState.length <= 12) {
            phoneFieldState = newState
        }
    }

    fun validateFields(phone: String, password: String): Boolean {
        return validatePhone(phone) && validatePassword(password)
    }


    fun validatePassword(password: String): Boolean {
        return (password.trim().length >= MIN_PASSWORD_LEN).also {
            if (!it) setErrorEntering("Длина пароля должна быть не менее $MIN_PASSWORD_LEN символов") else clearErrorEntering()
        }
    }

    fun validatePhone(value: String): Boolean {
        val phone = value//.trim()
        //return true
        return (phone.length == 12 && phone.substring(0, 2) == "+7" && phone.substringAfter("+").isDigitsOnly()).also {
            Log.d("validatePhone", phone + "${phone.length} ${phone.substring(0, 2)} ${phone.substringAfter("+").isDigitsOnly()}")
            if (!it) setErrorEntering("Номер телефона должен начинаться с '+7'") else clearErrorEntering()
        }
    }

    fun authorizeUser(auth: AuthorizationData) {
        Log.d("authorizeUser param", "$auth")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerState.postValue(NetworkResult.Loading())
                val response = RequestTemplate.mainRepository.authorizeUser(auth)
                if (response.isSuccessful) {
                    Log.d("authorizeUser ok ", response.body()!!.toString())
                    clearErrorEntering()
                    registerState.postValue(NetworkResult.Success(response.body()!!))
                }
                else {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    setErrorEntering(errorCode)

                    Log.d("authorizeUser not ok ", response.raw().toString())
                    Log.d("authorizeUser not ok ", response.errorBody()?.string().toString())
                    registerState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                setErrorEntering("Какая-то ошибка")
                Log.d("authorizeUser error ", e.toString() + "|||message: " + e.message)
                registerState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun registerUser(auth: RegistrationData) {
        Log.d("registerUser param", "$auth")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerState.postValue(NetworkResult.Loading())
                val response = RequestTemplate.mainRepository.registerUser(auth)
                if (response.isSuccessful) {
                    Log.d("registerUser ok ", response.body()!!.toString())
                    registerState.postValue(NetworkResult.Success(response.body()!!))
                }
                else {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    setErrorEntering(errorCode)

                    Log.d("registerUser not ok ", response.raw().toString())
                    Log.d("registerUser not ok ", response.errorBody()?.string().toString())
                    registerState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                setErrorEntering("Какая-то ошибка")
                Log.d("registerUser error ", e.toString() + "|||message: " + e.message)
                registerState.postValue(NetworkResult.Error(e.message))
            }
        }
    }
}

@Composable
fun SignInUpScreen(navController: NavHostController, signViewModel: SignInUpViewModel, datastoreViewModel: DatastoreViewModel) {

    val signState = signViewModel.signSwitcherState.observeAsState().value!!
    val authInfoState = datastoreViewModel.authInfoState.observeAsState().value // не используется в принципе
    val registerState = signViewModel.registerState.observeAsState().value

    LaunchedEffect(registerState) {
        if (registerState is NetworkResult.Success<*>) {
            Log.d("SignInUp", "registerState is NetworkResult.Success<*>")
            datastoreViewModel.setAuthInfoState(AuthInfo(registerState.data!!.personId, registerState.data.accessToken)).also { Log.d("SignInUp", "setAuthInfoState") }
            datastoreViewModel.updateAuthInfo(idValue = registerState.data!!.personId, tokenValue = registerState.data.accessToken)
            //signViewModel.registerStateInit()
            Log.d("SignInUp LaunchedEffect(registerState)", Auth.authInfo.toString())
            navController.navigate(route = Screens.Main.route) {
                popUpTo(Screens.SignInUp.route) { inclusive = true }
            }
        } else {
            //refresh token
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
                        is NetworkResult.Empty<*> -> {
                            TabRowComponent(
                                currentOption = signState,
                                listOptions = SignOptions.getOptions(),
                                onClick = { newChosenOption -> signViewModel.signSwitcherState.postValue(newChosenOption) }
                            )
                            when(signState) {
                                is SignOptions.SignIn -> {
                                    signViewModel.clearErrorEntering()
                                    SignFormComponent(
                                        signViewModel = signViewModel,
                                        datastoreViewModel,
                                        buttonText = stringResource(R.string.sign_in_btn),
                                        onClick = {
                                            signViewModel.authorizeUser(AuthorizationData(signViewModel.phoneFieldState, signViewModel.passwordFieldState))
                                            Log.d("SignInUp", "Вход успешен")
                                        }
                                    )
                                }
                                is SignOptions.SignUp -> {
                                    signViewModel.clearErrorEntering()
                                    SignUpFormComponent(
                                        signViewModel = signViewModel,
                                        datastoreViewModel,
                                        buttonText = stringResource(R.string.sign_up_btn),
                                        onClick = {
                                            signViewModel.registerUser(
                                                RegistrationData(
                                                    email = signViewModel.phoneFieldState,
                                                    password = signViewModel.passwordFieldState,
                                                    name = signViewModel.userFieldState,
                                                    phoneNumber = signViewModel.phoneFieldState
                                                )
                                            )
                                            Log.d("SignInUp", "Регистрация успешна")
                                        }
                                    )
                                }
                                else -> {}
                            }
                        }
                        is NetworkResult.Loading<*> -> {
                            LoadingBarComponent()
                        }
                        is NetworkResult.Error<*> -> {
                            signViewModel.registerState.postValue(NetworkResult.Empty())
                                //ShowErrorComponent(message = registerState.message, onButtonClick = { signViewModel.registerState.postValue(NetworkResult.Empty()) })
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
            if (signViewModel.errorEnteringState.isNotBlank()) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = signViewModel.errorEnteringState)
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
//            Spacer(modifier = Modifier.height(10.dp))
//            TextFieldComponent(
//                modifier = Modifier.fillMaxWidth(),
//                currentValue = signViewModel.phoneFieldState,
//                onValueChange = { value -> signViewModel.setPhoneField(value) },
//                placeholder =  stringResource(R.string.enter_number_field),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
//                //visualTransformation = PasswordVisualTransformation(),
//            )
        }
    )
}

@Composable
fun SignFormComponent(
    signViewModel: SignInUpViewModel,
    datastoreViewModel: DatastoreViewModel,
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
                onClick = {
//                    datastoreViewModel.updateAuthInfo(idValue = 12, tokenValue = "revenger11_TOKEN")
//                    datastoreViewModel.getAuthInfo()
                    if (signViewModel.validateFields(phone = signViewModel.phoneFieldState, password = signViewModel.passwordFieldState)) onClick()
                }
            )
        }
    )
}

@Composable
fun SignUpFormComponent(
    signViewModel: SignInUpViewModel,
    datastoreViewModel: DatastoreViewModel,
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
            FieldsComponent(signViewModel)
            Spacer(modifier = Modifier.height(10.dp))
            ButtonComponent(
                text = buttonText,
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (signViewModel.validateFields(phone = signViewModel.phoneFieldState, password = signViewModel.passwordFieldState)) onClick()
                }
            )
        }
    )
}
