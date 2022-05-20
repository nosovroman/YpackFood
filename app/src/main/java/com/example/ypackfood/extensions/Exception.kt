package com.example.ypackfood.extensions

import com.example.ypackfood.common.Constants
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Exception.translateException(): String {
    val error = when(this) {
        is SocketTimeoutException -> {
            Constants.SOCKET_TIMEOUT_EXCEPTION
        }
        is UnknownHostException -> {
            Constants.UNKNOWN_HOST_EXCEPTION
        }
        else -> {
            Constants.ERROR_SERVER
        }
    }

    return error
}