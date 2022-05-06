package com.example.ypackfood.models.commonData

data class ErrorResponse(
    val message: String? = "nullString",
    val ids: List<Int>? = null,
    val entityName: String? = "nullString"
)
