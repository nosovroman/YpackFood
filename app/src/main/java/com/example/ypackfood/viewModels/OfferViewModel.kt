package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.dataClasses.detailAction.DetailAction
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class OfferViewModel : ViewModel() {
    private var mainRepository: Repository

    init {
        val x = RetrofitBuilder.apiService
        mainRepository = Repository(x)
        Log.d("initOffer", "init")
    }

    var totalOldPriceState by mutableStateOf(0)
        private set
    fun setTotalOldPrice(totalOldPrice: Int) {
        totalOldPriceState = totalOldPrice
    }

    var totalNewPriceState by mutableStateOf(0)
        private set
    fun setTotalNewPrice(totalNewPrice: Int) {
        totalNewPriceState = totalNewPrice
    }


    var contentResp: MutableLiveData<NetworkResult<DetailAction>> = MutableLiveData()

    fun getOfferContent(actionId: Int) {
        Log.d("requestOffer", "getOfferContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                contentResp.postValue(NetworkResult.Loading())
                val response = mainRepository.getDetailAction(actionId)
                if (response.isSuccessful) {
                    contentResp.postValue(NetworkResult.Success(response.body()!!))
                }
                else {
                    Log.d("getOfferContent not ok ", response.message().toString())
                    contentResp.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("getOfferContent error ", e.toString() + "|||message: " + e.message)
                contentResp.postValue(NetworkResult.Error(e.message))
            }
        }
    }
}