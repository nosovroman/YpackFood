package com.example.ypackfood.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.common.Constants.CHOSE_ADDRESS
import com.example.ypackfood.common.Constants.END_DELIVERY
import com.example.ypackfood.common.Constants.START_DELIVERY
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.enumClasses.getCityNames
import com.example.ypackfood.enumClasses.getPaymentOptions
import com.example.ypackfood.extensions.isDigitsOnly
import com.example.ypackfood.extensions.toTimeString
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.TokenData
import com.example.ypackfood.models.commonData.CartDish
import com.example.ypackfood.models.orders.OrderFull.Order
import com.example.ypackfood.models.orders.OrderMin.*
import com.example.ypackfood.models.user.ProfileInfo
import com.example.ypackfood.sealedClasses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class OrderViewModel : ViewModel() {
    var createOrderState: MutableLiveData<NetworkResult<Order>> = MutableLiveData()

    var refreshState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()
    var profileState: MutableLiveData<NetworkResult<ProfileInfo>> = MutableLiveData()

    fun initStates() {
        createOrderState.postValue(null)
        refreshState.postValue(null)
        profileState.postValue(null)
    }

    fun refreshToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshState.postValue(NetworkResult.Loading())
                Log.d("TokenRefresh with ", Auth.authInfo.refreshToken)
                val response = mainRepository.refreshToken(TokenData(Auth.authInfo.refreshToken))
                if (response.isSuccessful) {
                    Log.d("refreshToken ok", response.body().toString())
                    refreshState.postValue(NetworkResult.Success(response.body()!!.copy(personId = Auth.authInfo.personId)))
                }
                else if (response.code() != 500) {
                    Log.d("refreshToken not ok ", Auth.authInfo.toString())

                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    refreshState.postValue(NetworkResult.HandledError(errorCode))
                }
            }
            catch (e: Exception) {
                Log.d("refreshToken error ", e.toString())
                val error = e.translateException()
                refreshState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun createOrder(order: OrderMin) {
        Log.d("createOrder param", "$order")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                createOrderState.postValue(NetworkResult.Loading())
                val response = mainRepository.createOrder(Auth.authInfo.accessToken, order)
                if (response.isSuccessful) {
                    Log.d("createOrder ok ", response.body()!!.toString())
                    createOrderState.postValue(NetworkResult.Success(response.body()!!))
                }
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    createOrderState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                createOrderState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun getProfile() {
        Log.d("getProfile", "getProfile")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileState.postValue(NetworkResult.Loading())
                val response = mainRepository.getProfile(Auth.authInfo.accessToken)
                Log.d("getProfile", "get response")
                if (response.isSuccessful) {
                    profileState.postValue(NetworkResult.Success(response.body()!!))
                }
                else if (response.code() != 500) {
                    val jsonString = response.errorBody()!!.string()
                    val errorCode = RequestTemplate.getErrorFromJson(jsonString).errorCode.toString()
                    profileState.postValue(NetworkResult.HandledError(errorCode))
                }
            } catch (e: Exception) {
                val error = e.translateException()
                profileState.postValue(NetworkResult.Error(error))
            }
        }
    }

    fun makeOrder(dishMinList: List<CartDish>, addressMerged: String, totalCost: Int) {
        if (checkIsPICKUP() || checkAddressIsNotEmpty()) {
            if (checkIsFaster() || checkIsForTime() && checkTimeIsNotEmpty()) {
                if (beOnTime()) {
                    emptyFieldMsgState = ""
                    createOrder(composeOrder(dishMinList, addressMerged, totalCost))
                } else {
                    emptyFieldMsgState = "Мы не сможем выполнить заказ к этому времени"
                    setEmptyDataDialog(true)
                }
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
            DishForOrderPost(
                count = it.count,
                dish = DishMin(
                    id = it.dishId,
                    portion = BasePortionMin(id = it.portionId, price = PriceNowMin(it.priceId))
                )
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

    fun computeResultTotalCost(totalCost: Int): Int = if (checkIsDELIVERY()) totalCost + Constants.DELIVERY_COST else totalCost


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
    var deliveryDialogState by mutableStateOf(false)
        private set
    fun setDeliveryDialog(newState: Boolean) {
        deliveryDialogState = newState
    }

        // ADDRESS CHOOSING
    var addressOptionState: MutableLiveData<TabRowSwitchable> = MutableLiveData(AddressOptions.NEW_ADDRESS())
    var chosenAddressState by mutableStateOf(CHOSE_ADDRESS)
        private set
    fun setChosenAddress(newCity: String) {
        chosenAddressState = newCity
    }
    var expandedAddressState by mutableStateOf(false)
        private set
    fun setExpandedAddress(isExpanded: Boolean) {
        expandedAddressState = isExpanded
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
    fun setConfirmAddress(addressOptionState: TabRowSwitchable) {
        when(addressOptionState) {
            is AddressOptions.NEW_ADDRESS -> {
                cityState = chosenCityState
                addressState = addressFieldState
            }
            is AddressOptions.OLD_ADDRESS -> {
                if (chosenAddressState != CHOSE_ADDRESS) {
                    cityState = chosenAddressState.substringBefore(",")
                    addressState = chosenAddressState.substringAfter(", ")
                }
            }
        }

        setDeliveryDialog(false)
    }
    fun setDismissAddress(addressOptionState: TabRowSwitchable) {
        when(addressOptionState) {
            is AddressOptions.NEW_ADDRESS -> {
                setChosenCity(cityState)
                setAddressField(addressState)
            }
            is AddressOptions.OLD_ADDRESS -> {
            }
        }

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

            if (timeInWorkTime(chosenHour, chosenMinute)) {
                    setConfirmTime(chosenHour, chosenMinute)
            } else {
                errorEnteringTime = "Неверное время доставки"
            }
        }  else {
            errorEnteringTime = "Для установки времени заполните все поля"
        }
    }

    fun timeInWorkTime(hour: Int, minute: Int): Boolean {
        return (hour in START_DELIVERY until END_DELIVERY || hour == END_DELIVERY && minute == 0)
    }

//    fun beOnTime(): Boolean {
//        val currentTime = getCurrentTime()
//        val minTimeForOrder = computeMinTimeForOrder(currentTime.first, currentTime.second)
//
//        val wishTimeOrder = convertToMinutes(hourState.toInt(), minuteState.toInt())
//        if (wishTimeOrder > minTimeForOrder) {
//            val hourMinute = convertToHourMinute(minTimeForOrder)
//            val resultHour = hourMinute.first.toTimeString()
//            val resultMinute = hourMinute.second.toTimeString()
//            hourState = resultHour
//            minuteState = resultMinute
//
//            setHoursField(resultHour)
//            setMinutesField(resultMinute)
//
//            return true
//        }
//
//        return false
//    }

    fun beOnTime(): Boolean {
        val currentTime = getCurrentTime()
        val minTimeForOrder = computeMinTimeForOrder(currentTime.first, currentTime.second)

        if (checkIsFaster()) {
            val hourMinute = convertToHourMinute(minTimeForOrder)
            val resultHour = hourMinute.first.toTimeString()
            val resultMinute = hourMinute.second.toTimeString()
            hourState = resultHour
            minuteState = resultMinute

            setHoursField(resultHour)
            setMinutesField(resultMinute)
        }
        Log.d("hourState", hourState)
        Log.d("hourState, min", minuteState)
        Log.d("hourState, timeInWork", timeInWorkTime(hourState.toInt(), minuteState.toInt()).toString())

        val wishTimeOrder = convertToMinutes(hourState.toInt(), minuteState.toInt())

        return wishTimeOrder >= minTimeForOrder && timeInWorkTime(hourState.toInt(), minuteState.toInt())
    }

    fun convertToMinutes(hours: Int, minutes: Int): Int = hours * 60 + minutes
    fun convertToHourMinute(minutes: Int): Pair<Int, Int> = Pair(minutes / 60, minutes % 60)
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