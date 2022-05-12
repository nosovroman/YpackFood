package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants.END_DELIVERY
import com.example.ypackfood.common.Constants.START_DELIVERY
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.enumClasses.getCityNames
import com.example.ypackfood.enumClasses.getPaymentOptions
import com.example.ypackfood.extensions.isDigitsOnly
import com.example.ypackfood.extensions.toTimeString
import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.models.orders.OrderFull.Order
import com.example.ypackfood.models.orders.OrderMin.*
import com.example.ypackfood.sealedClasses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class OrderViewModel : ViewModel() {
    var createOrderState: MutableLiveData<NetworkResult<Order>> = MutableLiveData()
    fun createOrderInit() {
        createOrderState.postValue(NetworkResult.Empty())
    }

    fun createOrder(order: OrderMin) {
        Log.d("createOrder param", "$order")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                createOrderState.postValue(NetworkResult.Loading())
                val response = mainRepository.createOrder(Auth.authInfo.token, order)
                if (response.isSuccessful) {
                    Log.d("createOrder ok ", response.body()!!.toString())
                    createOrderState.postValue(NetworkResult.Success(response.body()!!))
                }
                else {
                    Log.d("createOrder not ok ", response.raw().toString())
                    Log.d("createOrder not ok ", response.errorBody()?.string().toString())
                    createOrderState.postValue(NetworkResult.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.d("createOrder error ", e.toString() + "|||message: " + e.message)
                createOrderState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun makeOrder(dishMinList: List<CartDish>, addressMerged: String, totalCost: Int) {
        if (checkIsPICKUP() || checkAddressIsNotEmpty()) {
            if (checkIsFaster() || checkIsForTime() && checkTimeIsNotEmpty()) {
                validateTime()
                emptyFieldMsgState = ""
                createOrder(composeOrder(dishMinList, addressMerged, totalCost))
            } else {
                emptyFieldMsgState = "Установите время доставки"
                setEmptyDataDialog(true)
            }
        } else if (!checkAddressIsNotEmpty()) {
            emptyFieldMsgState = "Установите адрес доставки"
            setEmptyDataDialog(true)
        }
    }

    fun composeOrder(dishMinList: List<CartDish>, addressMerged: String, totalCost: Int): OrderMin {
        val dishesMin = dishMinList.map {
            DishMin(
                id = it.dishId,
                count = it.count,
                portion = BasePortionMin(id = it.portionId, price = PriceNowMin(it.priceId))
            )
        }

        val address = if (deliveryState.value is DeliveryOptions.DELIVERY) AddressMin(address = addressMerged)
        else null

        return OrderMin(
            deliveryTime = "$hourState:$minuteState",
            totalPrice = totalCost,
            address = address,
            dishes = dishesMin,
            wayToGet = deliveryState.value?.javaClass?.simpleName.toString()
        )
    }

    fun checkIsPICKUP(): Boolean = deliveryState.value is DeliveryOptions.PICKUP
    fun checkIsDELIVERY(): Boolean = deliveryState.value is DeliveryOptions.DELIVERY
    fun checkIsFaster(): Boolean = timeState.value is TimeOptions.Faster
    fun checkIsForTime(): Boolean = timeState.value is TimeOptions.ForTime
    fun checkTimeIsNotEmpty(): Boolean = hourState != "00"
    fun checkAddressIsNotEmpty(): Boolean = addressState.isNotBlank()

        // EMPTY FIELDS ALERT
    var emptyFieldMsgState by mutableStateOf("")
        private set
    var emptyDataDialogState by mutableStateOf(false)
        private set
    fun setEmptyDataDialog(newState: Boolean) {
        emptyDataDialogState = newState
    }
    fun hideEmptyDataDialog() {
        emptyFieldMsgState = ""
        setEmptyDataDialog(false)
    }

        // DELIVERY CHOOSING
    var deliveryState: MutableLiveData<TabRowSwitchable> = MutableLiveData(DeliveryOptions.DELIVERY())
    fun setDelivery(newState: TabRowSwitchable) {
        deliveryState.postValue(newState)
    }
    var deliveryDialogState by mutableStateOf(false)
        private set
    fun setDeliveryDialog(newState: Boolean) {
        deliveryDialogState = newState
    }

        // TIME CHOOSING
    var timeState: MutableLiveData<TabRowSwitchable> = MutableLiveData(TimeOptions.Faster())
    var timeDialogState by mutableStateOf(false)
        private set
    fun setTimeDialog(newState: Boolean) {
        timeDialogState = newState
    }

        // CITY CHOOSING
    var chosenCityState by mutableStateOf(getCityNames()[0])
        private set
    fun setChosenCity(newCity: String) {
        chosenCityState = newCity
    }
    var expandedMenuCityState by mutableStateOf(false)
        private set
    fun setExpandedMenuCity(isExpanded: Boolean) {
        expandedMenuCityState = isExpanded
    }

        // PAYMENT CHOOSING
    var paymentState by mutableStateOf(getPaymentOptions()[0])
        private set
    fun setPayment(newPayment: String) {
        paymentState = newPayment
    }
    var expandedMenuPayState by mutableStateOf(false)
        private set
    fun setExpandedMenuPay(isExpanded: Boolean) {
        expandedMenuPayState = isExpanded
    }

        // ADDRESS FULL
    var fullAddressState by mutableStateOf(getCityNames()[0])
        private set
    fun setFullAddress(newState: String) {
        fullAddressState = newState
    }
        // ADDRESS REAL
    var cityState by mutableStateOf(getCityNames()[0])
        private set
    var addressState by mutableStateOf("")
        private set
    fun setConfirmAddress() {
        cityState = chosenCityState
        addressState = addressFieldState
        setDeliveryDialog(false)
    }
    fun setDismissAddress() {
        setChosenCity(cityState)
        setAddressField(addressState)
        setDeliveryDialog(false)
    }
    fun getAddress(): String {
        return if (addressState.isBlank()) "Адрес доставки" else "$cityState, $addressState"
    }



        // TIME
    var hourState by mutableStateOf("00")
        private set
    var minuteState by mutableStateOf("00")
        private set
    fun setConfirmTime(hour: Int, minute: Int) {
        hourState = hour.toTimeString()
        minuteState = minute.toTimeString()
        errorEnteringTime = ""
        setTimeDialog(false)
    }
    fun setDismissTime() {
        setHoursField(hourState)
        setMinutesField(minuteState)
        errorEnteringTime = ""
        setTimeDialog(false)
    }

    fun getCurrentTime(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        return Pair(hours, minutes)
    }

    var errorEnteringTime by mutableStateOf("")
        private set

    fun tryConfirmTime() {
        if (hoursFieldState.isNotBlank() && minutesFieldState.isNotBlank()) {
            val chosenHour = hoursFieldState.toInt()
            val chosenMinute = minutesFieldState.toInt()

            if ((chosenHour in START_DELIVERY until END_DELIVERY || chosenHour == END_DELIVERY && chosenMinute == 0)) {
                    setConfirmTime(chosenHour, chosenMinute)
            } else {
                errorEnteringTime = "Неверное время доставки"
            }
        }  else {
            errorEnteringTime = "Для установки времени заполните все поля"
        }
    }

    fun validateTime() {
        val currentTime = getCurrentTime()
        val minTimeForOrder = computeMinTimeForOrder(currentTime.first, currentTime.second)
        val wishTimeOrder = convertToMinutes(hourState.toInt(), minuteState.toInt())
        if (wishTimeOrder < minTimeForOrder) {
            val hourMinute = convertToHours(minTimeForOrder)
            val resultHour = hourMinute.first.toTimeString()
            val resultMinute = hourMinute.second.toTimeString()
            hourState = resultHour
            minuteState = resultMinute

            setHoursField(resultHour)
            setMinutesField(resultMinute)
        }
    }

    fun convertToMinutes(hours: Int, minutes: Int): Int = hours * 60 + minutes
    fun convertToHours(minutes: Int): Pair<Int, Int> = Pair(minutes / 60, minutes % 60)
    fun computeMinTimeForOrder(currentHours: Int, currentMinutes: Int): Int = convertToMinutes(currentHours, currentMinutes) + 45


        // ADDRESS_FIELD
    var addressFieldState by mutableStateOf("")
        private set
    fun setAddressField(newState: String) {
        addressFieldState = newState
    }

        // COMMENT_FIELD
    var commentFieldState by mutableStateOf("")
        private set
    fun setCommentField(newState: String) {
        commentFieldState = newState
    }

        // HORS_FIELD
    var hoursFieldState by mutableStateOf("")
        private set
    fun setHoursField(newState: String) {
        if (newState.length in 0..2 && newState.isDigitsOnly()) {
            hoursFieldState = if (newState.toInt() < 24) newState else "23"
        }
    }

        // MINUTES_FIELD
    var minutesFieldState by mutableStateOf("")
        private set
    fun setMinutesField(newState: String) {
        if (newState.length in 0..2 && newState.isDigitsOnly()) {
            minutesFieldState = if (newState.toInt() < 60) newState else "59"
        }
    }
}