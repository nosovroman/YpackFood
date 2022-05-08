package com.example.ypackfood.common

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ypackfood.models.commonData.ErrorResponse
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.sealedClasses.NetworkResult
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

object RequestTemplate {
    var mainRepository: Repository
    var TOKEN: String

    fun getErrorFromJson(jsonString: String): ErrorResponse {
        return Gson().fromJson(jsonString, ErrorResponse::class.java)
    }

    init {
        val apiService = RetrofitBuilder.apiService
        mainRepository = Repository(apiService)

        TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd2VydEBnbWFpbC5jb20iLCJpZCI6MzUsImlhdCI6MTY1MTg0NjA4MCwiZXhwIjoxNjU0NTI0NDgwfQ.5u33FglKAK6fq3eTBvhl4uyaKy4IwITekt8Eftet2o8"
    }

//    suspend fun <T> executeRequest(
//        viewModelScope: CoroutineScope,
//        resultState: MutableLiveData<NetworkResult<*>>,
//        responseMethod: suspend () -> Response<T>,
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