package com.example.ypackfood.viewModels

import android.location.Address
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.RequestTemplate
import com.example.ypackfood.common.RequestTemplate.mainRepository
import com.example.ypackfood.extensions.translateException
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.auth.TokenData
import com.example.ypackfood.models.user.ProfileInfo
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel: ViewModel() {
    var profileState: MutableLiveData<NetworkResult<ProfileInfo>> = MutableLiveData()
    var refreshState: MutableLiveData<NetworkResult<AuthInfo>> = MutableLiveData()

    fun initStates() {
        profileState.postValue(null)
        refreshState.postValue(null)
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

    fun deleteAddress(addressId: Int) {
        Log.d("deleteAddress", "deleteAddress")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var profile = profileState.value!!.data!!
                val newAddressList = profile.addresses as MutableList<com.example.ypackfood.models.commonData.Address>
                profileState.postValue(NetworkResult.Loading())

                val response = mainRepository.deleteAddress(Auth.authInfo.accessToken, addressId)
                if (response.isSuccessful) {
                    Log.d("deleteAddress1", newAddressList.toString())
                    newAddressList.map { it.id != addressId }
                    Log.d("deleteAddress2", newAddressList.toString())
                    profile = profile.copy(addresses = newAddressList)
                    Log.d("deleteAddress3", profile.toString())
                    profileState.postValue(NetworkResult.Success(profile.copy(addresses = newAddressList)))
                    Log.d("deleteAddress", response.body()!!.toString())
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
}