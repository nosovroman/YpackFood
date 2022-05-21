package com.example.ypackfood.extensions

import com.example.ypackfood.enumClasses.ErrorEnum

fun String.isDigitsOnly() = all(Char::isDigit) && isNotEmpty()

fun String.translateError(): String {
    val error = when (this) {
        ErrorEnum.INCORRECT_PHONE_NUMBER_OR_PASSWORD.title -> "Неправильный номер телефона или пароль"
        ErrorEnum.USER_ALREADY_EXIST.title -> "Пользователь с таким номером телефона уже существует"
        ErrorEnum.USER_IS_BANNED.title -> "Ваш аккаунт заблокирован"
        ErrorEnum.ALLOWED_NUMBER_OF_LOGIN_ATTEMPTS_EXCEEDED.title -> "Ваша возможность входить в систему заморожена на 24 часа с момента последней безуспешной попытки"
        else -> this
    }
    return error
}