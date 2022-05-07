package com.example.ypackfood.viewModels


import android.app.Application
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.*
import com.example.ypackfood.common.Auth
import com.example.ypackfood.dataStore.DataSettings
import com.example.ypackfood.models.auth.AuthInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DatastoreViewModel(context: Application) : AndroidViewModel(context) {
    var authInfoState: MutableLiveData<AuthInfo> = MutableLiveData()

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
            setAuthInfoState(AuthInfo(idValue, tokenValue))
        }
    }

    fun getAuthInfo() {
        viewModelScope.launch (Dispatchers.IO) {
            val idKey = preferencesKey<Int>(Auth.ID_KEY)
            val tokenKey = preferencesKey<String>(Auth.TOKEN_KEY)
            val data = dataStore.data.first()
            val authInfo = AuthInfo(personId = data[idKey] ?: -1, token = data[tokenKey] ?: "")

            setAuthInfoState(authInfo)
        }
    }

    fun clearAuthInfo() {
        updateAuthInfo(-1, "")
    }

    private fun setAuthInfoState(authInfo: AuthInfo) {
        Auth.authInfo = authInfo
        authInfoState.postValue(authInfo)
    }
}