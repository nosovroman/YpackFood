package com.example.ypackfood.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ypackfood.enumClasses.DeliveryOptions
import com.example.ypackfood.enumClasses.TabRowSwitchable

class OrderViewModel : ViewModel() {

    var deliveryDialogState by mutableStateOf(false)
        private set
    fun setDeliveryDialog(newState: Boolean) {
        deliveryDialogState = newState
    }

    var deliveryState: MutableLiveData<TabRowSwitchable> = MutableLiveData()
    init {
        deliveryState.postValue(DeliveryOptions.DELIVERY())
    }

    var commentFieldState by mutableStateOf("")
        private set
    fun setCommentField(newComment: String) {
        commentFieldState = newComment
    }

}