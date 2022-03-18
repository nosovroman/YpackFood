package com.example.ypackfood.viewModels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ShoppingCartViewModel : ViewModel() {

    var count by mutableStateOf(mutableStateListOf(1, 2))
        private set

    fun incrementCount(ind: Int) {
        count[ind] = count[ind] + 1
    }
    fun decrementCount(ind: Int) {
        count[ind] = count[ind] - 1
    }


}