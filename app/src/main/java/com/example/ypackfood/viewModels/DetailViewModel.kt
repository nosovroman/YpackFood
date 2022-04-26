package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel : ViewModel() {
    private var mainRepository: Repository

    init {
        val x = RetrofitBuilder.apiService
        mainRepository = Repository(x)
        Log.d("initDetail", "init")
    }

    var contentResp: MutableLiveData<NetworkResult<DetailContent>> = MutableLiveData()


    var countWishDishes by mutableStateOf(1)
        private set
    fun incCountWish() {
        countWishDishes++
    }
    fun decCountWish() {
        countWishDishes--
    }


    fun getDetailContent(contentId: Int) {
        Log.d("requestDetail", "getDetailContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                contentResp.postValue(NetworkResult.Loading())
                val response = mainRepository.getDetailContent(contentId)
                if (response.isSuccessful) {
                    contentResp.postValue(NetworkResult.Success(response.body()!!))
                }
                else {
                    Log.d("getDetailContent not ok ", response.message().toString())
                    contentResp.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("getDetailContent error ", e.toString() + "|||message: " + e.message)
                contentResp.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun buildDishInfo(id: Int, price: Int, count: Int, addons: String? = null): CartEntity {
        return CartEntity(
            dishId = id,
            dishPrice = price,
            dishCount = count,
            dishAddons = addons
        )
//        val detailDish = contentResp.value!!.data!!
//        return CartEntity(
//            dishId = detailDish.id,
//            dishPrice = detailDish.basePortion.priceNow.price,
//            dishCount = countWishDishes,
//            dishAddons = null
//        )
    }
}