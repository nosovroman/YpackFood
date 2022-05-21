package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.MAX_ORDERS_ON_PAGE
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.orders.OrderFull.OrderList
import com.example.ypackfood.models.orders.OrderMin.DishForOrderGet
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule

class HistoryViewModel : ViewModel() {

    var historyDishesState: MutableLiveData<NetworkResult<OrderList>> = MutableLiveData()
    var updateButtonState: MutableLiveData<Boolean> = MutableLiveData(false)
    fun setUpdateButton(newState: Boolean) {
        updateButtonState.postValue(newState)
    }

    var currentPageState by mutableStateOf(1)
        private set
    fun incrementCounter() {
        currentPageState++
    }
    fun decrementCounter() {
        currentPageState--
    }

    fun computeMaxPage(orders: Int): Int {
        var result = orders / MAX_ORDERS_ON_PAGE
        result += if (orders % MAX_ORDERS_ON_PAGE == 0) 0 else 1
        return result
    }

    fun setTimerForStatusUpdate() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val timer = Timer().schedule(2000) {
                    Log.d("Timer", "SetupButton")
                    setUpdateButton(true)
                    this.cancel()
                }
            } catch (e: Exception) {
                Log.d("errorTimer", e.toString())
            }
        }
    }


    var detailOrderDialogState by mutableStateOf(false)//mutableListOf<DishForOrderGet>()
        private set
    var chosenOrderDialogState by mutableStateOf(mutableListOf<DishForOrderGet>())//mutableListOf<DishForOrderGet>()
        private set
    fun setDetailOrderDialog(newState: Boolean, orderDishList: MutableList<DishForOrderGet>) {
        detailOrderDialogState = newState
        chosenOrderDialogState = orderDishList
    }
    fun clearDetailOrderDialog() {
        setDetailOrderDialog(false, mutableListOf())
    }
    fun detailOrderDialogIsEmpty() = !detailOrderDialogState

    var addedToCartState by mutableStateOf(false)
        private set
    fun setAddedToCart(newState: Boolean) {
        addedToCartState = newState
    }

    fun getHistoryContent(page: Int) {
        //val oldData = historyDishesState.value?.data ?: mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historyDishesState.postValue(NetworkResult.Loading())
                val response = mainRepository.getHistory(Auth.authInfo.accessToken, page)
                if (response.isSuccessful) {
                    if (response.body()!!.orders.isNullOrEmpty()) {
                        historyDishesState.postValue(NetworkResult.Empty())
                    } else {
                        historyDishesState.postValue(NetworkResult.Success(response.body()!!))
                    }
                    Log.d("getHistoryContent ok", response.body().toString())
                }
                else {
                    Log.d("getHistoryContent not ok ", Auth.authInfo.toString())

                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    Log.d("getHistoryContent errorCode", errorCode)
                    historyDishesState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                Log.d("getHistoryContent error ", e.toString())
                historyDishesState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun buildCartEntity(dishForOrderGet: List<DishForOrderGet>): List<CartEntity> {
        val dishList = dishForOrderGet.map { it.dish }
        val resultCartList = mutableListOf<CartEntity>()
        dishList.forEachIndexed() { index, elem ->
            resultCartList.add(
                with (elem) {
                    //Log.d("DetailContentDishList", it.toString())
                    CartEntity(
                        dishId = id,
                        userId = Auth.authInfo.personId,
                        portionId = portion.id,
                        dishPriceId = portion.priceNow.id,
                        dishPrice = portion.priceNow.price,
                        dishCount = dishForOrderGet[index].count ?: 1,
                        dishAddons = null
                    )
                }
            )
        }

        return resultCartList
    }

}