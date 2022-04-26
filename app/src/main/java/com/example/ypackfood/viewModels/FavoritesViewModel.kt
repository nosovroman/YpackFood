package com.example.ypackfood.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoritesViewModel: ViewModel() {
    private var mainRepository: Repository

    init {
        val x = RetrofitBuilder.apiService
        mainRepository = Repository(x)
        Log.d("initFavorites", "init")
    }


    var contentResp: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()
    //var contentResp: MutableLiveData<NetworkResult<MutableList<Category>>> = MutableLiveData()

    fun getContentByListId(contentIdList: List<Int>?) {
        Log.d("requestFavorites", "getContentByListId")
        contentIdList?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    contentResp.postValue(NetworkResult.Loading())
                    val response = mainRepository.getContentByListId(contentIdList)
                    if (response.isSuccessful) {
                        //contentResp.postValue(NetworkResult.Success(response.body()!!.dishes))
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

//    fun getMainContent_TEMP() {
//        Log.d("twer", "getMainContent_TEMP")
//        val oldData = contentResp.value?.data ?: mutableListOf()
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                contentResp.postValue(NetworkResult.Loading(oldData))
//                val response = mainRepository.getMainContent()
//                if (response.isSuccessful) {
//                    contentResp.postValue(NetworkResult.Success(response.body()!!.categories))
//                }
//                else {
//                    Log.d("getMainContent not ok ", response.message().toString())
//                    contentResp.postValue(NetworkResult.Error(response.message(), oldData))
//                }
//            } catch (e: Exception) {
//                Log.d("getMainContent error ", e.toString())
//                contentResp.postValue(NetworkResult.Error(e.message, oldData))
//            }
//        }
//    }
}