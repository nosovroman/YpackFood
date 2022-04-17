package com.example.ypackfood.components

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.ypackfood.sealedClasses.NetworkResult


@Composable
fun RequestStateComponent(
    requestState: NetworkResult<*>?,
    byLoading: @Composable () -> Unit = {},
    bySuccess: @Composable () -> Unit = {},
    byError: @Composable () -> Unit = {})
{
    when (requestState) {
        is NetworkResult.Loading<*> -> byLoading()
        is NetworkResult.Success<*> -> bySuccess()
        is NetworkResult.Error<*> -> byError()
        else -> { Log.d("elseBranch", "else branch") }
    }
}