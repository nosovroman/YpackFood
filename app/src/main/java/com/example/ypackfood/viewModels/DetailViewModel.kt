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
import com.example.ypackfood.common.RequestTemplate.mainRepository
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

    fun getDetailContent(contentId: Int) {
        Log.d("requestDetail", "getDetailContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                detailDishState.postValue(NetworkResult.Loading())
                val response = mainRepository.getDetailContent(Auth.authInfo.accessToken, contentId)
                if (response.isSuccessful) {
                    detailDishState.postValue(NetworkResult.Success(response.body()!!))
                }
                else {
                    Log.d("getDetailContent not ok ", response.message().toString())
                    Log.d("getDetailContent not ok ", response.errorBody()?.string().toString())
                    detailDishState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("getDetailContent error ", e.toString() + "|||message: " + e.message)
                detailDishState.postValue(NetworkResult.Error(e.message))
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
                else {
                    Log.d("getFavorites not ok ", response.message().toString())
                    Log.d("getFavorites not ok ", response.errorBody()?.string().toString())
                    favoritesState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("getFavorites error ", e.toString() + "|||message: " + e.message)
                favoritesState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun addFavorite(contentId: Int) {
        Log.d("changeFavorite addFavorite", "addFavorite")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //favoritesToggledState.postValue(NetworkResult.Loading())
                val response = mainRepository.addFavorite(Auth.authInfo.accessToken, contentId)
                if (response.isSuccessful) {
                    //favoritesToggledState.postValue(NetworkResult.Success(response.body()!!))
                    val newFavoritesList = favoritesState.value!!.data!!//.add(contentId)
                    newFavoritesList.add(contentId)
                    favoritesState.postValue(NetworkResult.Success(newFavoritesList))
                    Log.d("changeFavorite addFavorite ok ", newFavoritesList.toString())
                    setEnabledIButton(false).also {
                        delay(3000)
                        setEnabledIButton(true)
                    }
                }
                else {
                    Log.d("changeFavorite addFavorite not ok ", response.message().toString())
                    Log.d("changeFavorite addFavorite not ok ", response.errorBody()?.string().toString())
                    //favoritesToggledState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("changeFavorite addFavorite error ", e.toString() + "|||message: " + e.message)
                //favoritesToggledState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun deleteFavorite(contentId: Int) {
        Log.d("changeFavorite deleteFavorite", "deleteFavorite")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //favoritesState.postValue(NetworkResult.Loading())
                val response = mainRepository.deleteFavorite(Auth.authInfo.accessToken, contentId)
                if (response.isSuccessful) {
                    //favoritesState.postValue(NetworkResult.Success(response.body()!!))
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
                    Log.d("changeFavorite deleteFavorite not ok ", response.message().toString())
                    Log.d("changeFavorite deleteFavorite not ok ", response.errorBody()?.string().toString())
                    //favoritesState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("changeFavorite deleteFavorite error ", e.toString() + "|||message: " + e.message)
                //favoritesState.postValue(NetworkResult.Error(e.message))
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
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count,
            dishAddons = addons
        )
    }
}