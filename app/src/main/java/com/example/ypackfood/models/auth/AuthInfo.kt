package com.example.ypackfood.models.auth

import com.example.ypackfood.common.Constants.PERSON_ID_DEFAULT
import com.example.ypackfood.common.Constants.TOKEN_DEFAULT

data class AuthInfo(
    val personId: Int = PERSON_ID_DEFAULT,
    val accessToken: String = TOKEN_DEFAULT,
    val refreshToken: String = TOKEN_DEFAULT,
)