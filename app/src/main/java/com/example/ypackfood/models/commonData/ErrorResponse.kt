package com.example.ypackfood.models.commonData

data class ErrorResponse(
    val message: String? = "",
    val ids: List<Int>? = null,
    val entityName: String? = "",
    val errorCode: String? = ""
)
