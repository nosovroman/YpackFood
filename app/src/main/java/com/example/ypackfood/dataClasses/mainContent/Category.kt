package com.example.ypackfood.dataClasses.mainContent

import com.example.ypackfood.dataClasses.commonData.Dishe

data class Category(
    val categoryType: String,
    val dishes: List<Dishe>
)