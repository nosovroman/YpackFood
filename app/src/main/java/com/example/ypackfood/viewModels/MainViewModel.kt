package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.TokenData
import com.example.ypackfood.models.mainContent.FilteredDishes
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {
    lateinit var listContentState: LazyListState
        private set
    fun listContentStateInit(newListState: LazyListState) {
        listContentState = newListState
    }

    lateinit var listCategoryState: LazyListState
        private set
    fun listCategoryStateInit(newListState: LazyListState) {
        listCategoryState = newListState
    }

    lateinit var scaffoldState: ScaffoldState
        private set
    fun scaffoldStateInit(newScaffoldState: ScaffoldState) {
        scaffoldState = newScaffoldState
    }

    var toolbarOffsetState by mutableStateOf(0f)
        private set
    fun setToolbarOffset(newOffsetPx: Float) {
        toolbarOffsetState = newOffsetPx
    }

    fun computeCategoryList(): List<String> {
        val dishes = dishesState.value?.data?.map { it.categoryTypeDto } ?: listOf()
        val action = if (actionsState.value?.data.isNullOrEmpty()) listOf() else listOf("Акции")
        return action+dishes
    }

    var dishesState: MutableLiveData<NetworkResult<MutableList<FilteredDishes>>> = MutableLiveData()
    var actionsState: MutableLiveData<NetworkResult<MutableList<ActionsItem>>> = MutableLiveData()
    var refreshState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()

    fun initStates() {
        dishesState.postValue(null)
        actionsState.postValue(null)
        refreshState.postValue(null)
    }

    fun refreshToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshState.postValue(NetworkResult.Loading(data = null))
                Log.d("TokenRefresh with ", Auth.authInfo.refreshToken)
                val response = mainRepository.refreshToken(TokenData(Auth.authInfo.refreshToken))
                if (response.isSuccessful) {
                    Log.d("refreshToken ok", response.body().toString())
                    refreshState.postValue(NetworkResult.Success(response.body()!!.copy(personId = Auth.authInfo.personId)))
                }
                else if (response.code() != 500) {
                    Log.d("refreshToken not ok ", Auth.authInfo.toString())

                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    refreshState.postValue(NetworkResult.HandledError(errorCode, null))
                }
            }
            catch (e: Exception) {
                Log.d("refreshToken error ", e.toString())
                val error = e.translateException()
                refreshState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun getMainContent() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dishesState.postValue(NetworkResult.Loading())
                val response = mainRepository.getMainContent(Auth.authInfo.accessToken)
                if (response.isSuccessful) {
                    if (!response.body().isNullOrEmpty()) {
                        dishesState.postValue(NetworkResult.Success(response.body()!!))
                    } else {
                        dishesState.postValue(NetworkResult.Empty())
                    }
                    Log.d("getMainContent ok", "response.body().toString()")
                }
                else if (response.code() != 500) {
                    Log.d("getMainContent not ok ", Auth.authInfo.toString())

                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    dishesState.postValue(NetworkResult.HandledError(errorCode))
                }
            }
            catch (e: Exception) {
                val error = e.translateException()
                dishesState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun getActionsContent() {
        Log.d("getActionsContent", "getActionsContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                actionsState.postValue(NetworkResult.Loading())
                val response = mainRepository.getActions(Auth.authInfo.accessToken)
                if (response.isSuccessful) {
                    if (!response.body().isNullOrEmpty()) {
                        actionsState.postValue(NetworkResult.Success(response.body()!!))
                    } else {
                        actionsState.postValue(NetworkResult.Empty())
                    }
                    Log.d("getActionsContent ok ", "response.body().toString()")
                }
                else if (response.code() != 500) {
                    Log.d("getActionsContent not ok ", Auth.authInfo.toString())
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    actionsState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                actionsState.postValue(NetworkResult.Error(error))
            }
        }
    }
}