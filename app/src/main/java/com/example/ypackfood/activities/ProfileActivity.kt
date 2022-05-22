package com.example.ypackfood.activities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Auth
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.LoadingBarComponent
import com.example.ypackfood.components.ShowErrorComponent
import com.example.ypackfood.components.ToolbarComponent
import com.example.ypackfood.enumClasses.ErrorEnum
import com.example.ypackfood.models.commonData.Address
import com.example.ypackfood.sealedClasses.NetworkResult
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.viewModels.DatastoreViewModel
import com.example.ypackfood.viewModels.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    datastoreViewModel: DatastoreViewModel
) {
    val profileState = profileViewModel.profileState.observeAsState().value

    LaunchedEffect(true) {
        profileViewModel.initStates()
        profileViewModel.getProfile()
    }

    val refreshState = profileViewModel.refreshState.observeAsState().value
    LaunchedEffect(refreshState) {
        when (refreshState) {
            is NetworkResult.Success<*> -> {
                Log.d("TokenRefresh success ", Auth.authInfo.refreshToken)
                datastoreViewModel.setAuthInfoState(refreshState.data!!)
                profileViewModel.getProfile()
            }
            is NetworkResult.HandledError<*> -> {
                Log.d("TokenRefresh HandledError ", refreshState.message.toString())
                navController.navigate(route = Screens.SignInUp.route) {
                    popUpTo(Screens.Profile.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(profileState) {
        when (profileState) {
            is NetworkResult.HandledError<*> -> {
                when (val errorCode = profileState.message.toString()) {
                    ErrorEnum.ACCESS_TOKEN_EXPIRED_OR_INVALID.title -> {
                        Log.d("TokenRefresh", "refreshing")
                        profileViewModel.refreshToken()
                    }
                    ErrorEnum.AUTHENTICATION_REQUIRED.title -> {
                        Log.d("TokenRefresh detailDishState Logout", errorCode)
                        navController.navigate(route = Screens.SignInUp.route) {
                            popUpTo(Screens.Profile.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    Scaffold (
        topBar = { ToolbarComponent(navController = navController, title = Screens.Profile.title) },
        content = {
            when(refreshState) {
                is NetworkResult.Error<*> -> {
                    ShowErrorComponent(message = refreshState.message, onButtonClick = { profileViewModel.getProfile() })
                }
                else -> {}
            }
            when(profileState) {
                is NetworkResult.Loading<*> -> {
                    Column {
                        Spacer(modifier = Modifier.height(Constants.TOOLBAR_HEIGHT + 15.dp))
                        LoadingBarComponent()
                    }
                }
                is NetworkResult.Success<*> -> {
                    LazyColumn (
                        modifier = Modifier.padding(horizontal = 20.dp),
                        content = {
                            item {
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(profileState.data!!.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                            item {
                                Text(profileState.data!!.phoneNumber, fontSize = 18.sp)
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(text = "Список адресов", fontSize = 18.sp)
                            }
                            items(profileState.data!!.addresses) {
                                DeliveryAddressComponent(
                                    address = it,
                                    onClick = {
                                        addressId -> profileViewModel.deleteAddress(addressId)
                                    }
                                )
                                Divider()
                            }
                        }
                    )
                }
                is NetworkResult.Error<*> -> {
                    ShowErrorComponent(
                        message = profileState.message,
                        onButtonClick = { profileViewModel.getProfile() }
                    )
                }
                else -> {}
            }
        }
    )
}

@Composable
fun DeliveryAddressComponent(address: Address,  onClick: (id: Int) -> Unit) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Text(text = address.address)
            IconButton(
                onClick = { onClick(address.id) },
                content = {
                    Icon(Icons.Outlined.Delete, contentDescription = "Удалить")
                }
            )
        }
    )
}