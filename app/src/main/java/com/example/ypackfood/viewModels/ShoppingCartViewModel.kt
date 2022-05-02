package com.example.ypackfood.viewModels


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Constants
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

    var dishesRoomState: List<CartEntity> = listOf()
        private set
    fun setDishesRoom(newList: List<CartEntity>) {
        dishesRoomState = newList
    }

    var floatingState by mutableStateOf(true)
        private set
    fun setFloating(newState: Boolean) {
        floatingState = newState
    }

    init {
        val x = RetrofitBuilder.apiService
        mainRepository = Repository(x)
        Log.d("initCart", "init")
    }

    fun composeDishInfo(dishList: List<Dish>, shopList: List<CartEntity>): List<CartDish> {
        val dishMap = dishList.associateBy { it.id }

//        Log.d("fe_dishMap shopList", shopList.map { it.dishId }.toString())
//        Log.d("fe_dishMap dishList", dishList.map{ it.id }.toString())

        val shopListFiltered = shopList.filter { it.dishId in dishMap }
        val resultDishList = shopListFiltered.map {
            val dishInfo = dishMap[it.dishId]!!
            CartDish(
                shoppingCartId = it.shoppingCartId!!,
                dishId = it.dishId,
                name = dishInfo.name,
                price = dishInfo.basePortion.priceNow.price,
                count = it.dishCount,
                category = dishInfo.category,
                composition = dishInfo.composition,
                urlPicture = dishInfo.picturePaths.large,
                addons = null,
                changedPrice = it.dishPrice == dishInfo.basePortion.priceNow.price
            )
        }

        return resultDishList
    }

    var contentResp: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()

    fun initContentResp() {
        contentResp.postValue(NetworkResult.Empty())
    }

    fun computeTotalPrice(): Int {
        var totalPrice = 0
        dishesRoomState.forEach{
            totalPrice += it.dishPrice * it.dishCount
        }

        return totalPrice
    }


    fun getContentByListId(contentIdList: List<Int>?) {
        contentIdList?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.d("fe_dishMap getContentByListId", "loading...")
                    contentResp.postValue(NetworkResult.Loading())
                    val response = mainRepository.getContentByListId(contentIdList)
                    if (response.isSuccessful) {

                        Log.d("fe_dishMap getContentByListId", "getting...")

                        contentResp.postValue(NetworkResult.Success(response.body()!!))
                        Log.d("getContentByListId", response.body()!!.toString())

                        Log.d("getContentByListId ${response.code()}", response.raw().toString())
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