package com.example.ypackfood.common

import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder

object RequestTemplate {
    var mainRepository: Repository

    init {
        val apiService = RetrofitBuilder.apiService
        mainRepository = Repository(apiService)
    }

//    fun executeRequest(
//        viewModelScope: CoroutineScope,
//        resultState: MutableLiveData<NetworkResult<*>>,
//        responseMethod: Response<*>,
//        nameForLog: String = "something"
//    ) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                resultState.postValue(NetworkResult.Loading(data = null))
//                val response = responseMethod
//                if (response.isSuccessful) {
//                    Log.d("$nameForLog ok ", response.body()!!.toString())
//                    resultState.postValue(NetworkResult.Success(response.body()!!))
//                } else {
//                    Log.d("$nameForLog not ok ", response.body()!!.toString())
//                    resultState.postValue(NetworkResult.Error(response.message(), null))
//                }
//            }
//            catch (e: Exception) {
//                Log.d("$nameForLog error ", e.message.toString())
//                resultState.postValue(NetworkResult.Error(e.message.toString(), null))
//            }
//        }
//    }
}