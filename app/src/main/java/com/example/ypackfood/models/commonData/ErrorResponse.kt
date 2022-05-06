package com.example.ypackfood.models.commonData

import com.squareup.moshi.Json

data class ErrorResponse(
    //@field:Json(name = "message")
    val message: String? = "nullString",
    val ids: List<Int>? = null,
    val entityName: String? = "nullString",
    val fieldName: String? = "nullString",

)
