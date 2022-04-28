package com.example.ypackfood.viewModels


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoppingCartViewModel : ViewModel() {
    private var mainRepository: Repository

    init {
        val x = RetrofitBuilder.apiService
        mainRepository = Repository(x)
        Log.d("initCart", "init")
    }

    var count by mutableStateOf(mutableStateListOf(1, 2))
        private set

    fun incrementCount(ind: Int) {
        count[ind] = count[ind] + 1
    }
    fun decrementCount(ind: Int) {
        count[ind] = count[ind] - 1
    }

    fun composeDishInfo(dishList: List<Dish>, shopList: List<CartEntity>): List<CartDish> {
        val dishMap = dishList.associateBy { it.id }
        val resultDishList = shopList.map {
            val dishInfo = dishMap[it.dishId]!!
            val totalPriceWish = it.dishCount * it.dishPrice
            val totalPriceReal = it.dishCount * dishInfo.basePortion.priceNow.price
            CartDish(
                id = it.dishId,
                name = dishInfo.name,
                price = totalPriceReal,
                count = it.dishCount,
                category = dishInfo.category,
                composition = dishInfo.composition,
                urlPicture = dishInfo.picturePaths.large,
                addons = null,
                changedPrice = totalPriceWish == totalPriceReal
            )
        }

        Log.d("dishMap", dishMap.toString())
        return resultDishList
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