package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Components
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.TokenData
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel : ViewModel() {
    var countWishDishes by mutableStateOf(1)
        private set
    fun initCountWish() {
        countWishDishes = 1
    }
    fun incrementCounter() {
        countWishDishes++
    }
    fun decrementCounter() {
        countWishDishes--
    }

    var indexOptionState by mutableStateOf(0)
        private set
    fun setIndexOption(newState: Int) {
        indexOptionState = newState
    }

    var enabledIButtonState by mutableStateOf(true)
        private set
    fun setEnabledIButton(newState: Boolean) {
        enabledIButtonState = newState
    }

    var currentIconState by mutableStateOf(Components.defaultIcon)
        private set
    fun setCurrentIcon(newState: ImageVector) {
        currentIconState = newState
    }

    var detailDishState: MutableLiveData<NetworkResult<DetailContent>> = MutableLiveData()
    var favoritesState: MutableLiveData<NetworkResult<MutableList<Int>>> = MutableLiveData()

    var refreshState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()

    fun initStates() {
        detailDishState.postValue(null)
        refreshState.postValue(null)
        initCountWish()
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
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
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

    fun getDetailContent(contentId: Int) {
        Log.d("requestDetail", "getDetailContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                detailDishState.postValue(NetworkResult.Loading())
                val response = mainRepository.getDetailContent(Auth.authInfo.accessToken, contentId)
                if (response.isSuccessful) {
                    detailDishState.postValue(NetworkResult.Success(response.body()!!))
                }
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    detailDishState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                detailDishState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun getFavoritesId() {
        Log.d("getFavorites", "getFavorites")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                favoritesState.postValue(NetworkResult.Loading())
                val response = mainRepository.getFavoritesId(Auth.authInfo.accessToken)
                if (response.isSuccessful) {
                    favoritesState.postValue(NetworkResult.Success(response.body()!!))
                }
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    favoritesState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                favoritesState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun addFavorite(contentId: Int) {
        Log.d("changeFavorite addFavorite", "addFavorite")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = mainRepository.addFavorite(Auth.authInfo.accessToken, contentId)
                if (response.isSuccessful) {
                    val newFavoritesList = favoritesState.value!!.data!!
                    newFavoritesList.add(contentId)
                    favoritesState.postValue(NetworkResult.Success(newFavoritesList))
                    Log.d("changeFavorite addFavorite ok ", newFavoritesList.toString())
                    setEnabledIButton(false).also {
                        delay(3000)
                        setEnabledIButton(true)
                    }
                }
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    favoritesState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                favoritesState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun deleteFavorite(contentId: Int) {
        Log.d("changeFavorite deleteFavorite", "deleteFavorite")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = mainRepository.deleteFavorite(Auth.authInfo.accessToken, contentId)
                if (response.isSuccessful) {
                    val newFavoritesList = favoritesState.value!!.data!!//.add(contentId)
                    newFavoritesList.remove(contentId)
                    favoritesState.postValue(NetworkResult.Success(newFavoritesList))
                    Log.d("changeFavorite deleteFavorite ok ", newFavoritesList.toString())
                    setEnabledIButton(false).also {
                        delay(3000)
                        setEnabledIButton(true)
                    }
                }
                else {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    favoritesState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                favoritesState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun onClickFavoritesIcon(contentId: Int, listOfFavorites: MutableList<Int>) {
        try {
            currentIconState = if (!listOfFavorites.contains(contentId)) {
                addFavorite(contentId)
                Components.filledFavoriteIcon
            } else {
                deleteFavorite(contentId)
                Components.outlinedFavoriteIcon
            }
        }  catch (e: Exception) {}
    }

    fun getRealCurrentIcon(contentId: Int, listOfFavorites: MutableList<Int>): ImageVector {
        currentIconState = if (listOfFavorites.contains(contentId)) {
            Components.filledFavoriteIcon
        } else {
            Components.outlinedFavoriteIcon
        }
        return currentIconState
    }

    fun buildDishInfo(dishId: Int, portionId: Int, priceId: Int, price: Int, count: Int, addons: String? = null): CartEntity {
        return CartEntity(
            dishId = dishId,
            userId = Auth.authInfo.personId,
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count,
            dishAddons = addons
        )
    }
}