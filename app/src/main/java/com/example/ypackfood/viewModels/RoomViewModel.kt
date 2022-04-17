package com.example.ypackfood.viewModels


import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.*
import com.example.ypackfood.common.Components
import com.example.ypackfood.repository.RepositoryRoom
import com.example.ypackfood.room.database.FavoritesDatabase
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    var favorites: LiveData<List<Int>>
    private var repositoryRoom: RepositoryRoom

    //var contentResp: MutableLiveData<NetworkResult<ImageVector>> = MutableLiveData()

    var currentIcon by mutableStateOf(Components.defaultIcon)
        private set

    init {
        val x = FavoritesDatabase.getInstance(application).favoritesDao()
        repositoryRoom = RepositoryRoom(x)
        favorites = repositoryRoom.favorites
    }

//    fun checkFavoriteById(favoriteId: Int) {
//        val oldData = contentResp.value?.data ?: Components.outlinedFavoriteIcon
//
//        viewModelScope.launch (Dispatchers.IO) {
//            try {
//                //contentResp.postValue(NetworkResult.Loading(oldData))
//                val response = checkExistFavoriteById(favoriteId)// repositoryRoom.checkFavoriteById(favoriteId)
//                Log.d("roomRequest $favoriteId", response.toString())
//                val renderingIcon = if (response) Components.filledFavoriteIcon else Components.outlinedFavoriteIcon
//                contentResp.postValue(NetworkResult.Success(renderingIcon))
//            } catch (e: Exception) {
//                contentResp.postValue(NetworkResult.Error(e.message, oldData))
//            }
//        }
//    }

    fun addFavorite(favoriteId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            repositoryRoom.addFavorite(favoriteId)
        }
        Log.d("roomRequest", "adding $favoriteId")
    }

    fun deleteFavorite(favoriteId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            repositoryRoom.deleteFavorite(favoriteId)
        }
        Log.d("roomRequest", "deleting $favoriteId")
    }

    private fun checkExistFavoriteById(favoriteId: Int): Boolean {
        return favorites.value?.contains(favoriteId) ?: false
    }

    fun initFavoriteIcon(contentId: Int) {
        currentIcon = if (checkExistFavoriteById(contentId)) {
            Components.filledFavoriteIcon
        } else {
            Components.outlinedFavoriteIcon
        }
    }

    fun setFavoritesIcon(contentId: Int) {
        try {
            currentIcon = if (!checkExistFavoriteById(contentId)) {
                addFavorite(contentId)
                Components.filledFavoriteIcon
            } else {
                deleteFavorite(contentId)
                Components.outlinedFavoriteIcon
            }
            Log.d("roomRequest", currentIcon.name)
        } catch (e: Exception) {
            Log.d("roomRequest", "error setFavoritesIcon")
        }
    }
}