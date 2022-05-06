package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.RequestTemplate.TOKEN
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel : ViewModel() {
    var countWishDishes by mutableStateOf(1)
        private set
    fun initCountWish() {
        countWishDishes = 1
    }
    fun incCountWish() {
        countWishDishes++
    }
    fun decCountWish() {
        countWishDishes--
    }


    var detailDishState: MutableLiveData<NetworkResult<DetailContent>> = MutableLiveData()

    fun getDetailContent(contentId: Int) {
        Log.d("requestDetail", "getDetailContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                detailDishState.postValue(NetworkResult.Loading())
                val response = mainRepository.getDetailContent(TOKEN, contentId)
                if (response.isSuccessful) {
                    detailDishState.postValue(NetworkResult.Success(response.body()!!))
                }
                else {
                    Log.d("getDetailContent not ok ", response.message().toString())
                    Log.d("getDetailContent not ok ", response.errorBody()?.string().toString())
                    detailDishState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("getDetailContent error ", e.toString() + "|||message: " + e.message)
                detailDishState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun buildDishInfo(id: Int, portionId: Int, priceId: Int, price: Int, count: Int, addons: String? = null): CartEntity {
        return CartEntity(
            dishId = id,
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count,
            dishAddons = addons
        )
    }
}