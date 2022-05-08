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
        val shoppingCartDao = instanceDB.shoppingCartDao()
        repositoryRoom = RepositoryRoom(shoppingCartDao)
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

    fun deleteAllFromCart() {
        Log.d("Cart", "deleteAllFromCart")
        viewModelScope.launch (Dispatchers.IO) {
            repositoryRoom.deleteAllFromCart()
        }
    }
}