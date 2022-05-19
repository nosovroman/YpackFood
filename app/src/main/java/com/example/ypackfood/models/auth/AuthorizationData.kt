package com.example.ypackfood.models.auth

data class AuthorizationData(
    val email: String,
    val password: String,
)
data class RegistrationData(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String
)