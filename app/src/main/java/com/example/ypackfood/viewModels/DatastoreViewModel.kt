package com.example.ypackfood.viewModels


import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.*
import com.example.ypackfood.common.Auth
import com.example.ypackfood.dataStore.DataSettings
import com.example.ypackfood.models.auth.AuthInfo
import com.example.ypackfood.models.detailContent.DetailContent
import com.example.ypackfood.sealedClasses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DatastoreViewModel(context: Application) : AndroidViewModel(context) {
    var authInfoState: MutableLiveData<AuthInfo>? = MutableLiveData()

    private val dataStore = DataSettings.getInstance(context)

    fun updateAuthInfo(idValue: Int, tokenValue: String) {
        viewModelScope.launch (Dispatchers.IO) {
            val idKey = preferencesKey<Int>(Auth.ID_KEY)
            val tokenKey = preferencesKey<String>(Auth.TOKEN_KEY)
            dataStore.edit { settings ->
                settings.clear()
                settings[idKey] = idValue
                settings[tokenKey] = tokenValue
            }
            setAuthInfo(AuthInfo(idValue, tokenValue))
        }
    }

    fun getAuthInfo() {
        viewModelScope.launch (Dispatchers.IO) {
            val idKey = preferencesKey<Int>(Auth.ID_KEY)
            val tokenKey = preferencesKey<String>(Auth.TOKEN_KEY)
            val data = dataStore.data.first()
            val authInfo = AuthInfo(personId = data[idKey], token = data[tokenKey])

            setAuthInfo(authInfo)
        }
    }

    fun setAuthInfo(authInfo: AuthInfo) {
        Auth.authInfo = authInfo
        authInfoState?.postValue(authInfo)
    }
}