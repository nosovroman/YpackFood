package com.example.ypackfood.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoritesViewModel: ViewModel() {
    var favoritesState: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()

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
                } else {
                    Log.d("getFavorites not ok ${response.code()}", response.raw().toString())
                    Log.d("getFavorites not ok ", response.errorBody()?.string().toString())
                    favoritesState.postValue(NetworkResult.Error(response.message()))
                }

            } catch (e: Exception) {
                Log.d("getFavorites error ", e.toString())
                favoritesState.postValue(NetworkResult.Error(e.message))
            }
        }
    }
}