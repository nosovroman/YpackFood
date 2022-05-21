package com.example.ypackfood.viewModels

import android.location.Address
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.models.user.ProfileInfo
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel: ViewModel() {
    var profileState: MutableLiveData<NetworkResult<ProfileInfo>> = MutableLiveData()

    fun getProfile() {
        Log.d("getProfile", "getProfile")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileState.postValue(NetworkResult.Loading())
                val response = mainRepository.getProfile(Auth.authInfo.accessToken)
                Log.d("getProfile", "get response")
                if (response.isSuccessful) {
                    profileState.postValue(NetworkResult.Success(response.body()!!))
                    Log.d("getProfile", response.body()!!.toString())
                } else {
                    Log.d("getProfile not ok ${response.code()}", response.raw().toString())
                    profileState.postValue(NetworkResult.Error(response.message()))
                }

            } catch (e: Exception) {
                Log.d("getProfile error ", e.toString())
                profileState.postValue(NetworkResult.Error(e.message))
            }
        }
    }

    fun deleteAddress(addressId: Int) {
        Log.d("deleteAddress", "deleteAddress")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileState.postValue(NetworkResult.Loading())
                val response = mainRepository.deleteAddress(Auth.authInfo.accessToken, addressId)
                if (response.isSuccessful) {
                    val newAddressList = profileState.value!!.data!!.addresses as MutableList<com.example.ypackfood.models.commonData.Address>
                    newAddressList.map { it.id != addressId }
                    profileState.postValue(NetworkResult.Success(profileState.value!!.data!!.copy(addresses = newAddressList)))
                    Log.d("deleteAddress", response.body()!!.toString())
                } else {
                    Log.d("deleteAddress not ok ${response.code()}", response.raw().toString())
                    profileState.postValue(NetworkResult.Error(response.message()))
                }

            } catch (e: Exception) {
                Log.d("deleteAddress error ", e.toString())
                profileState.postValue(NetworkResult.Error(e.message))
            }
        }
    }
}