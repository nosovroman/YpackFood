package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.extensions.isDigitsOnly
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.AuthorizationData
import com.example.ypackfood.models.auth.RegistrationData
import com.example.ypackfood.models.detailAction.DetailAction
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.SignOptions
import com.example.ypackfood.sealedClasses.TabRowSwitchable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SignInUpViewModel : ViewModel() {
    var signSwitcherState: MutableLiveData<TabRowSwitchable> = MutableLiveData(SignOptions.SignIn())

    var registerState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()
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

    fun validateFields(name: String, phone: String, password: String): Boolean {
        return validateName(name) && validatePhone(phone) && validatePassword(password)
    }


    fun validatePassword(password: String): Boolean {
        return (password.trim().length >= Constants.MIN_PASSWORD_LEN).also {
            if (!it) setErrorEntering("Длина пароля должна быть не менее ${Constants.MIN_PASSWORD_LEN} символов") else clearErrorEntering()
        }
    }

    fun validateName(value: String): Boolean {
        return (value.trim().isNotBlank()).also {
            if (!it) setErrorEntering("Заполните имя") else clearErrorEntering()
        }
    }

    fun validatePhone(value: String): Boolean {
        val phone = value
        return (phone.length == 12 && phone.substring(0, 2) == "+7" && phone.substringAfter("+").isDigitsOnly()).also {
            if (!it) setErrorEntering("Номер телефона должен начинаться с '+7'") else clearErrorEntering()
        }
    }

    fun authorizeUser(auth: AuthorizationData) {
        Log.d("authorizeUser param", "$auth")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerState.postValue(NetworkResult.Loading())
                val response = mainRepository.authorizeUser(auth)
                if (response.isSuccessful) {
                    Log.d("authorizeUser ok ", response.body()!!.toString())
                    clearErrorEntering()
                    registerState.postValue(NetworkResult.Success(response.body()!!))
                }
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    Log.d("authorizeUser not ok ", errorCode)
                    setErrorEntering(errorCode)

                    registerState.postValue(NetworkResult.HandledError(errorCode))
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
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    setErrorEntering(errorCode)

                    Log.d("registerUser not ok ", errorCode)
                    registerState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                setErrorEntering("Какая-то ошибка")
                Log.d("registerUser error ", e.toString() + "|||message: " + e.message)
                registerState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun translateError(value: String): String {
        val error = when (value) {
            ErrorEnum.INCORRECT_PHONE_NUMBER_OR_PASSWORD.title -> "Неправильный номер телефона или пароль"
            ErrorEnum.USER_ALREADY_EXIST.title -> "Пользователь с таким номером телефона уже существует"
            ErrorEnum.USER_IS_BANNED.title -> "Ваш аккаунт заблокирован"
            ErrorEnum.ALLOWED_NUMBER_OF_LOGIN_ATTEMPTS_EXCEEDED.title -> "Ваша возможность входить в систему заморожена на 24 часа с момента последней безуспешной попытки"
            else -> value
        }
        return error
    }
}