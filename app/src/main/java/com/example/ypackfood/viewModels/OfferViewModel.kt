package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.TokenData
import com.example.ypackfood.models.detailAction.DetailAction
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class OfferViewModel : ViewModel() {
//    var totalOldPriceState by mutableStateOf(0)
//        private set
//    fun setTotalOldPrice(totalOldPrice: Int) {
//        totalOldPriceState = totalOldPrice
//    }

//    var totalNewPriceState by mutableStateOf(0)
//        private set
//    fun setTotalNewPrice(totalNewPrice: Int) {
//        totalNewPriceState = totalNewPrice
//    }

    var actionState: MutableLiveData<NetworkResult<DetailAction>> = MutableLiveData()
    var refreshState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()

    fun initStates() {
        actionState.postValue(null)
        refreshState.postValue(null)
    }

    fun refreshToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshState.postValue(NetworkResult.Loading())
                Log.d("TokenRefresh with ", Auth.authInfo.refreshToken)
                val response = mainRepository.refreshToken(TokenData(Auth.authInfo.refreshToken))
                if (response.isSuccessful) {
                    Log.d("refreshToken ok", response.body().toString())
                    refreshState.postValue(NetworkResult.Success(response.body()!!.copy(personId = Auth.authInfo.personId)))
                }
                else if (response.code() != 500) {
                    Log.d("refreshToken not ok ", Auth.authInfo.toString())

                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    refreshState.postValue(NetworkResult.HandledError(errorCode))
                }
            }
            catch (e: Exception) {
                Log.d("refreshToken error ", e.toString())
                val error = e.translateException()
                refreshState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun getOfferContent(actionId: Int) {
        Log.d("requestOffer", "getOfferContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                actionState.postValue(NetworkResult.Loading())
                val response = mainRepository.getDetailAction(Auth.authInfo.accessToken, actionId)
                if (response.isSuccessful) {
                    //if (!response.body()?.dishes.isNullOrEmpty()) {
                        actionState.postValue(NetworkResult.Success(response.body()!!))
//                    }
//                    else {
//                        actionState.postValue(NetworkResult.Empty())
//                    }
                }
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    actionState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                actionState.postValue(NetworkResult.Error(error))
            }
        }
    }
}