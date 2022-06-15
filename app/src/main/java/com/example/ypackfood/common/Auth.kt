package com.example.ypackfood.common

import com.example.ypackfood.models.auth.AuthInfo

object Auth {
    const val ID_KEY = "userId"
    const val TOKEN_KEY = "token"
    const val REFRESH_TOKEN_KEY = "refreshToken"

    var authInfo: AuthInfo = AuthInfo()
}