package com.example.ypackfood.models.auth

data class AuthorizationData(
    val phoneNumber: String,
    val password: String,
)
data class RegistrationData(
    val phoneNumber: String,
    val password: String,
    val name: String
)