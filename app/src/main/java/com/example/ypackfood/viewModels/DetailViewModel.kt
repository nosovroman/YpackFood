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
import com.example.ypackfood.common.RequestTemplate.TOKEN
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.room.entities.CartEntity
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel : ViewModel() {
    var countWishDishes by mutableStateOf(1)
        private set
    fun initCountWish() {
        countWishDishes = 1
    }
    fun incCountWish() {
        countWishDishes++
    }
    fun decCountWish() {
        countWishDishes--
    }

    var currentIcon by mutableStateOf(Components.defaultIcon)
        private set

    var detailDishState: MutableLiveData<NetworkResult<DetailContent>> = MutableLiveData()
    var favoritesState: MutableLiveData<NetworkResult<MutableList<Int>>> = MutableLiveData()
    var favoritesToggledState: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()

    fun getDetailContent(contentId: Int) {
        Log.d("requestDetail", "getDetailContent")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                detailDishState.postValue(NetworkResult.Loading())
                val response = mainRepository.getDetailContent(Auth.authInfo.token, contentId)
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

    fun getFavorites() {
        Log.d("getFavorites", "getFavorites")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                favoritesState.postValue(NetworkResult.Loading())
                val response = mainRepository.getFavorites(Auth.authInfo.token)
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
        Log.d("addFavorite", "addFavorite")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //favoritesToggledState.postValue(NetworkResult.Loading())
                val response = mainRepository.addFavorite(Auth.authInfo.token, contentId)
                if (response.isSuccessful) {
                    //favoritesToggledState.postValue(NetworkResult.Success(response.body()!!))
                    val newFavoritesList = favoritesState.value!!.data!!//.add(contentId)
                    newFavoritesList.add(contentId)
                    favoritesState.postValue(NetworkResult.Success(newFavoritesList))
                    Log.d("addFavorite ok ", newFavoritesList.toString())
                }
                else {
                    Log.d("addFavorite not ok ", response.message().toString())
                    Log.d("addFavorite not ok ", response.errorBody()?.string().toString())
                    //favoritesToggledState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("addFavorite error ", e.toString() + "|||message: " + e.message)
                //favoritesToggledState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun deleteFavorite(contentId: Int) {
        Log.d("deleteFavorite", "deleteFavorite")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //favoritesState.postValue(NetworkResult.Loading())
                val response = mainRepository.deleteFavorite(Auth.authInfo.token, contentId)
                if (response.isSuccessful) {
                    //favoritesState.postValue(NetworkResult.Success(response.body()!!))
                    val newFavoritesList = favoritesState.value!!.data!!//.add(contentId)
                    newFavoritesList.remove(contentId)
                    favoritesState.postValue(NetworkResult.Success(newFavoritesList))
                    Log.d("deleteFavorite ok ", newFavoritesList.toString())
                }
                else {
                    Log.d("deleteFavorite not ok ", response.message().toString())
                    Log.d("deleteFavorite not ok ", response.errorBody()?.string().toString())
                    //favoritesState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("deleteFavorite error ", e.toString() + "|||message: " + e.message)
                //favoritesState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun onClickFavoritesIcon(contentId: Int, listOfFavorites: MutableList<Int>) {
        try {
            currentIcon = if (!listOfFavorites.contains(contentId)) {
                addFavorite(contentId)
                Components.filledFavoriteIcon
            } else {
                deleteFavorite(contentId)
                Components.outlinedFavoriteIcon
            }
        }  catch (e: Exception) {}
    }

    fun getRealCurrentIcon(contentId: Int, listOfFavorites: MutableList<Int>): ImageVector {
        currentIcon = if (listOfFavorites.contains(contentId)) {
            Components.filledFavoriteIcon
        } else {
            Components.outlinedFavoriteIcon
        }
        return currentIcon
    }

    fun buildDishInfo(id: Int, portionId: Int, priceId: Int, price: Int, count: Int, addons: String? = null): CartEntity {
        return CartEntity(
            dishId = id,
            portionId = portionId,
            dishPriceId = priceId,
            dishPrice = price,
            dishCount = count,
            dishAddons = addons
        )
    }
}