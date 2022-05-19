package com.example.ypackfood.viewModels


import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.*
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants.PERSON_ID_DEFAULT
import com.example.ypackfood.common.Constants.TOKEN_DEFAULT
import com.example.ypackfood.dataStore.DataSettings
import com.example.ypackfood.models.auth.AuthInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DatastoreViewModel(context: Application) : AndroidViewModel(context) {
    var authInfoState: MutableLiveData<AuthInfo> = MutableLiveData()

    private val dataStore = DataSettings.getInstance(context)

    fun updateAuthInfo(idValue: Int, tokenValue: String, refreshTokenValue: String) {
        viewModelScope.launch (Dispatchers.IO) {
            val idKey = preferencesKey<Int>(Auth.ID_KEY)
            val tokenKey = preferencesKey<String>(Auth.TOKEN_KEY)
            val refreshTokenKey = preferencesKey<String>(Auth.REFRESH_TOKEN_KEY)
            dataStore.edit { settings ->
                settings.clear()
                settings[idKey] = idValue
                settings[tokenKey] = tokenValue
                settings[refreshTokenKey] = refreshTokenValue
            }
        }
    }

    fun getAuthInfo() {
        viewModelScope.launch (Dispatchers.IO) {
            val idKey = preferencesKey<Int>(Auth.ID_KEY)
            val tokenKey = preferencesKey<String>(Auth.TOKEN_KEY)
            val data = dataStore.data.first()
            val authInfo = AuthInfo(
                personId = data[idKey] ?: PERSON_ID_DEFAULT,
                accessToken = data[tokenKey] ?: TOKEN_DEFAULT,
                refreshToken = data[tokenKey] ?: TOKEN_DEFAULT,
            )

            setAuthInfoState(authInfo)
        }
    }

    fun clearAuthInfo() {
        updateAuthInfo(PERSON_ID_DEFAULT, TOKEN_DEFAULT, TOKEN_DEFAULT)
    }

    fun setAuthInfoState(authInfo: AuthInfo) {
        Log.d("authInfo", authInfo.toString())
        Auth.authInfo = authInfo
        authInfoState.postValue(authInfo)
    }
}