package com.example.ypackfood.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ypackfood.common.Constants
import com.example.ypackfood.sealedClasses.NetworkResult


@Composable
fun RequestStateComponent(
    requestState: NetworkResult<*>?,
    byLoading: @Composable () -> Unit = {
        Column {
            Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
            LoadingBarComponent()
        }
    },
    bySuccess: @Composable () -> Unit = {},
    byEmpty: @Composable () -> Unit = {},
    byError: @Composable (message: String?) -> Unit = {}
) {
    when (requestState) {
        is NetworkResult.Loading<*> -> byLoading()
        is NetworkResult.Success<*> -> bySuccess()
        is NetworkResult.Empty<*> -> byEmpty()
        is NetworkResult.Error<*> -> byError(requestState.message)
        else -> { Log.d("elseBranch", "else branch") }
    }
}