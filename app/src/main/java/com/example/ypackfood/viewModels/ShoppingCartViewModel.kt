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
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.TokenData
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
            val changedPrice: Boolean = run {
                //dishInfo.por
                true
            }
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
                changedPrice = changedPrice //it.dishPrice == dishInfo.basePortion.priceNow.price
            )
        }

        setResultDish(resultDishList)
    }

    var cartState: MutableLiveData<NetworkResult<MutableList<Dish>>> = MutableLiveData()

//    fun initContentResp() {
//        cartState.postValue(NetworkResult.Empty())
//    }

    fun computeTotalPrice() {
        var totalPrice = 0
        dishesRoomState.forEach{
            totalPrice += it.dishPrice * it.dishCount
        }

        setTotalPrice(totalPrice)
    }

    var refreshState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()

    fun initStates() {
        cartState.postValue(null)
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
                    val errorCode = getErrorFromJson(jsonString).errorCode.toString()
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

    fun getContentByListId(contentIdList: List<Int>?, roomViewModel: RoomViewModel) {
        contentIdList?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.d("fe_dishMap getContentByListId", "loading...")
                    cartState.postValue(NetworkResult.Loading())
                    val response = mainRepository.getContentByListId(Auth.authInfo.accessToken, contentIdList)
                    if (response.isSuccessful) {
                        if (!response.body().isNullOrEmpty()) {
                            cartState.postValue(NetworkResult.Success(response.body()!!))
                        } else {
                            cartState.postValue(NetworkResult.Empty())
                        }
                    }
                    else if (response.code() != 500) {
                        val jsonString = response.errorBody()!!.string()
                        val errorRes = getErrorFromJson(jsonString)
                        if (errorRes.message == ErrorEnum.RESOURCE_NOT_FOUND.title) {
                            roomViewModel.setDeletingCartList(errorRes.ids!!)
                        }
                        cartState.postValue(NetworkResult.HandledError(errorRes.errorCode.toString()))
                    }
                } catch (e: Exception) {
                    val error = e.translateException()
                    cartState.postValue(NetworkResult.Error(error))
                }
            }
        }
    }
}