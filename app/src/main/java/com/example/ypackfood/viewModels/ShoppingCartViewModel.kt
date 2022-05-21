package com.example.ypackfood.viewModels


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate.getErrorFromJson
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.models.commonData.Dish
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoppingCartViewModel : ViewModel() {

    var totalPriceState by mutableStateOf(0)
        private set
    private fun setTotalPrice(newState: Int) {
        totalPriceState = newState
    }

    var dishesRoomState: List<CartEntity> = listOf()
        private set
    fun setDishesRoom(newList: List<CartEntity>) {
        dishesRoomState = newList
    }


    var resultDishState: List<CartDish> = listOf()
        private set
    private fun setResultDish(newList: List<CartDish>) {
        resultDishState = newList
    }

    fun composeDishInfo(dishList: List<Dish>, shopList: List<CartEntity>) {
        val dishMap = dishList.associateBy { it.id }

        val shopListFiltered = shopList.filter { it.dishId in dishMap }
        val resultDishList = shopListFiltered.map {
            val dishInfo = dishMap[it.dishId]!!
            CartDish(
                shoppingCartId = it.shoppingCartId!!,
                dishId = it.dishId,
                name = dishInfo.name,
                portionId = it.portionId,
                priceId = it.dishPriceId,
                price = it.dishPrice,//dishInfo.basePortion.priceNow.price,
                count = it.dishCount,
                category = dishInfo.category,
                composition = dishInfo.composition,
                urlPicture = dishInfo.picturePaths.large,
                addons = null,
                changedPrice = it.dishPrice == dishInfo.basePortion.priceNow.price
            )
        }

        setResultDish(resultDishList)
    }

    var contentResp: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()

    fun initContentResp() {
        contentResp.postValue(NetworkResult.Empty())
    }

    fun computeTotalPrice() {
        var totalPrice = 0
        dishesRoomState.forEach{
            totalPrice += it.dishPrice * it.dishCount
        }

        setTotalPrice(totalPrice)
    }


    fun getContentByListId(contentIdList: List<Int>?, roomViewModel: RoomViewModel) {
        contentIdList?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.d("fe_dishMap getContentByListId", "loading...")
                    contentResp.postValue(NetworkResult.Loading())
                    val response = mainRepository.getContentByListId(Auth.authInfo.accessToken, contentIdList)
                    when(response.code()) {
                        in 200..299 -> {
                            contentResp.postValue(NetworkResult.Success(response.body()!!))
                            Log.d("getContentByListId ${response.code()}", response.raw().toString())
                        }
                        400 -> {
                            val jsonString = response.errorBody()!!.string()
                            val res = getErrorFromJson(jsonString)
                            Log.d("getContentByListId x = ", res.ids.toString())
                            roomViewModel.setDeletingCartList(res.ids!!)
                            contentResp.postValue(NetworkResult.Error("Некоторые блюда были исключены из меню ресторана"))
                        }
                        else -> {
                            Log.d("getContentByListId not ok ${response.code()}", response.raw().toString())
                            Log.d("getContentByListId", response.errorBody()?.string().toString())
                            contentResp.postValue(NetworkResult.Error(response.message()))
                        }
                    }
                } catch (e: Exception) {
                    Log.d("getContentByListId error ", e.toString())
                    contentResp.postValue(NetworkResult.Error(e.message))
                }
            }
        }
    }
}