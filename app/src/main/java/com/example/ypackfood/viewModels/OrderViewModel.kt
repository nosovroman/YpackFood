package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ypackfood.common.Constants.CITY
import com.example.ypackfood.enumClasses.DeliveryOptions
import com.example.ypackfood.enumClasses.TabRowSwitchable
import com.example.ypackfood.enumClasses.getCityNames

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

    var expandedMenuCity by mutableStateOf(false)
        private set
    fun setExpandedCity(isExpanded: Boolean) {
        expandedMenuCity = isExpanded
    }

    var chosenCityState by mutableStateOf(getCityNames()[0])
        private set
    fun setCity(newCity: String) {
        chosenCityState = newCity
    }

    var addressState by mutableStateOf("")
        private set
    fun setConfirmAddress() {
        addressState = addressFieldState
        deliveryDialogState = false
    }
    fun setDismissAddress() {
        addressFieldState = addressState
        deliveryDialogState = false
    }
    fun getAddress(): String {
        return if (addressState.isBlank()) "Адресс доставки" else "$chosenCityState, $addressState"
    }

    var addressFieldState by mutableStateOf("")
        private set
    fun setAddressField(newAddress: String) {
        addressFieldState = newAddress
    }

    var commentFieldState by mutableStateOf("")
        private set
    fun setCommentField(newComment: String) {
        commentFieldState = newComment
    }

}