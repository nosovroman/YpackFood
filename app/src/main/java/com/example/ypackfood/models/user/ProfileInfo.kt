package com.example.ypackfood.models.user

import com.example.ypackfood.models.commonData.Address

data class ProfileInfo(
    val addresses: List<Address>,
    val favorites: List<Favorite>,
    val id: Int,
    val name: String,
    val phoneNumber: String
)