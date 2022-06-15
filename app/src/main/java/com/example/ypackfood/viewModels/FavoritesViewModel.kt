package com.example.ypackfood.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.TokenData
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoritesViewModel: ViewModel() {
    var favoritesState: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()
    var refreshState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()

    fun initStates() {
        favoritesState.postValue(null)
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

    fun getFavorites() {
        Log.d("getFavorites", "getFavorites")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                favoritesState.postValue(NetworkResult.Loading())
                val response = mainRepository.getFavorites(Auth.authInfo.accessToken)
                Log.d("getFavorites", "get response")
                if (response.isSuccessful) {
                    if (!response.body().isNullOrEmpty()) {
                        favoritesState.postValue(NetworkResult.Success(response.body()!!))
                    } else {
                        favoritesState.postValue(NetworkResult.Empty())
                    }
                    Log.d("getFavorites", response.body()!!.toString())
                } else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    favoritesState.postValue(NetworkResult.HandledError(errorCode))
                }

            } catch (e: Exception) {
                val error = e.translateException()
                favoritesState.postValue(NetworkResult.Error(error))
            }
        }
    }
}