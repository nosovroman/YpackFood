package com.example.ypackfood.models.mainContent

import com.example.ypackfood.models.commonData.Dish

data class Category(
    val categoryType: String,
    val dishes: List<Dish>
)