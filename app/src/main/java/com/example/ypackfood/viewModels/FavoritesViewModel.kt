package com.example.ypackfood.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.TOKEN
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoritesViewModel: ViewModel() {
    var favoritesState: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()

    fun initContentResp() {
        favoritesState.postValue(NetworkResult.Empty())
    }

    fun getContentByListId(contentIdList: List<Int>?, roomViewModel: RoomViewModel) {
        Log.d("requestFavorites", "getContentByListId")
        contentIdList?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    favoritesState.postValue(NetworkResult.Loading())
                    val response = mainRepository.getContentByListId(Auth.authInfo.token, contentIdList)
                    when(response.code()) {
                        in 200..299 -> {
                            favoritesState.postValue(NetworkResult.Success(response.body()!!))
                            Log.d("getContentByListId", response.body()!!.toString())
                        }
                        400 -> {
                            val jsonString = response.errorBody()!!.string()
                            val res = RequestTemplate.getErrorFromJson(jsonString)
                            Log.d("getContentByListId x = ", res.ids.toString())
                            roomViewModel.setDeletingFavList(res.ids!!)
                            favoritesState.postValue(NetworkResult.Loading())
                        }
                        else -> {
                            Log.d("getContentByListId not ok ${response.code()}", response.raw().toString())
                            Log.d("getContentByListId not ok ", response.errorBody()?.string().toString())
                            favoritesState.postValue(NetworkResult.Error(response.message()))
                        }
                    }

                } catch (e: Exception) {
                    Log.d("getContentByListId error ", e.toString())
                    favoritesState.postValue(NetworkResult.Error(e.message))
                }
            }
        }
    }
}