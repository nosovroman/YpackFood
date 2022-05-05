package com.example.ypackfood.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoritesViewModel: ViewModel() {
    var contentResp: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()

    fun getContentByListId(contentIdList: List<Int>?) {
        Log.d("requestFavorites", "getContentByListId")
        contentIdList?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    contentResp.postValue(NetworkResult.Loading())
                    val response = mainRepository.getContentByListId(contentIdList)
                    if (response.isSuccessful) {
                        contentResp.postValue(NetworkResult.Success(response.body()!!))
                        Log.d("getContentByListId", response.body()!!.toString())
                    }
                    else {
                        Log.d("getContentByListId not ok ${response.code()}", response.raw().toString())
                        contentResp.postValue(NetworkResult.Error(response.message()))
                    }
                } catch (e: Exception) {
                    Log.d("getContentByListId error ", e.toString())
                    contentResp.postValue(NetworkResult.Error(e.message))
                }
            }
        }
    }
}