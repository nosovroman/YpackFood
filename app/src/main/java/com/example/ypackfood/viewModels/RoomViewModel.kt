package com.example.ypackfood.viewModels


import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.ypackfood.common.Components
import com.example.ypackfood.repository.RepositoryRoom
import com.example.ypackfood.room.database.DishDatabase
import com.example.ypackfood.room.entities.CartEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    //var favorites: LiveData<List<Int>>
    var shopList: LiveData<List<CartEntity>>
    private var repositoryRoom: RepositoryRoom

    var currentIcon by mutableStateOf(Components.defaultIcon)
        private set

    var deletingCartListState by mutableStateOf(listOf<Int>())
        private set
    fun setDeletingCartList(newState: List<Int>) {
        deletingCartListState = newState
    }

    var deletingFavListState by mutableStateOf(listOf<Int>())
        private set
    fun setDeletingFavList(newState: List<Int>) {
        deletingFavListState = newState
    }

    init {
        val instanceDB = DishDatabase.getFavoritesInstance(application)
        //val favoritesDao = instanceDB.favoritesDao()
        val shoppingCartDao = instanceDB.shoppingCartDao()
        repositoryRoom = RepositoryRoom(shoppingCartDao)
        //favorites = repositoryRoom.favorites
        shopList = repositoryRoom.shopList
    }

    // ------------------ ShoppingCart
    fun addToCart(cartEntity: CartEntity) {
        Log.d("Cart", "addToCart")
        viewModelScope.launch (Dispatchers.IO) {
            repositoryRoom.addToCart(cartEntity)
        }
    }

    fun updateCart(cartEntity: CartEntity) {
        Log.d("Cart", "addToCart")
        viewModelScope.launch (Dispatchers.IO) {
            repositoryRoom.updateCart(cartEntity)
        }
    }

    fun deleteFromCart(cartEntity: CartEntity) {
        Log.d("Cart", "deleteFromCart")
        viewModelScope.launch (Dispatchers.IO) {
            repositoryRoom.deleteFromCart(cartEntity)
        }
    }

    fun deleteFromCartByListId(ids: List<Int>) {
        Log.d("Cart", "deleteFromCartByListId")
        viewModelScope.launch (Dispatchers.IO) {
            repositoryRoom.deleteFromCartByListId(ids)
        }
    }

//    // ------------------ Favorites
//    private fun addFavorite(favoriteId: Int) {
//        viewModelScope.launch (Dispatchers.IO) {
//            repositoryRoom.addFavorite(favoriteId)
//        }
//        Log.d("roomRequest", "adding $favoriteId")
//    }
//
//    private fun deleteFavorite(favoriteId: Int) {
//        viewModelScope.launch (Dispatchers.IO) {
//            repositoryRoom.deleteFavorite(favoriteId)
//        }
//        Log.d("roomRequest", "deleting $favoriteId")
//    }
//
//    fun deleteFromFavoritesByListId(ids: List<Int>) {
//        Log.d("Cart", "deleteFromFavoritesByListId")
//        viewModelScope.launch (Dispatchers.IO) {
//            repositoryRoom.deleteFromFavoritesByListId(ids)
//        }
//    }
//
//    private fun checkExistFavoriteById(favoriteId: Int): Boolean {
//        return favorites.value?.contains(favoriteId) ?: false
//    }
//
//    fun initFavoriteIcon(contentId: Int) {
//        currentIcon = if (checkExistFavoriteById(contentId)) {
//            Components.filledFavoriteIcon
//        } else {
//            Components.outlinedFavoriteIcon
//        }
//    }
//
//    fun setFavoritesIcon(contentId: Int) {
//        try {
//            currentIcon = if (!checkExistFavoriteById(contentId)) {
//                addFavorite(contentId)
//                Components.filledFavoriteIcon
//            } else {
//                deleteFavorite(contentId)
//                Components.outlinedFavoriteIcon
//            }
//            Log.d("roomRequest", currentIcon.name)
//        } catch (e: Exception) {
//            Log.d("roomRequest", "error setFavoritesIcon")
//        }
//    }
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