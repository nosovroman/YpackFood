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
import com.example.ypackfood.common.Constants.ERROR_INTERNET
import com.example.ypackfood.models.actionsContent.ActionsItem
import com.example.ypackfood.models.mainContent.Category
import com.example.ypackfood.repository.Repository
import com.example.ypackfood.retrofit.RetrofitBuilder
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {
    private var mainRepository: Repository

    init {
        val x = RetrofitBuilder.apiService
        mainRepository = Repository(x)
    }


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


    var dishesState: MutableLiveData<NetworkResult<MutableList<Category>>> = MutableLiveData()
    var actionsState: MutableLiveData<NetworkResult<MutableList<ActionsItem>>> = MutableLiveData()

    init {
        getActionsContent()
        getMainContent()
    }

    fun getMainContent() {
        Log.d("getMainContent", "getMainContent")
        val oldData = dishesState.value?.data ?: mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dishesState.postValue(NetworkResult.Loading(oldData))
                val response = mainRepository.getMainContent()
                if (response.isSuccessful) {
                    dishesState.postValue(NetworkResult.Success(response.body()!!.categories))
                }
                else {
                    Log.d("getMainContent not ok ", response.raw().toString())
                    dishesState.postValue(NetworkResult.Error(response.message(), oldData))
                }
            } catch (e: Exception) {
                Log.d("getMainContent error ", e.toString())
                dishesState.postValue(NetworkResult.Error(ERROR_INTERNET, oldData))
            }
        }
    }

    fun getActionsContent() {
        Log.d("getActionsContent", "getActionsContent")
        val oldData = actionsState.value?.data ?: mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                actionsState.postValue(NetworkResult.Loading(oldData))
                val response = mainRepository.getActions()
                if (response.isSuccessful) {
                    actionsState.postValue(NetworkResult.Success(response.body()!!.actions))
                    Log.d("getActionsContent ok ", response.body().toString())
                }
                else {
                    Log.d("getActionsContent not ok ", response.message().toString())
                    actionsState.postValue(NetworkResult.Error(response.message(), oldData))
                }
            } catch (e: Exception) {
                Log.d("getActionsContent error ", e.toString())
                actionsState.postValue(NetworkResult.Error(ERROR_INTERNET, oldData))
            }
        }
    }
}