package com.example.ypackfood.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ypackfood.enumClasses.DeliveryOptions
import com.example.ypackfood.enumClasses.TabRowSwitchable

class OrderViewModel : ViewModel() {

    var deliveryState: MutableLiveData<TabRowSwitchable> = MutableLiveData()
    init {
        deliveryState.postValue(DeliveryOptions.DELIVERY())
    }
}