package com.example.ypackfood.models.mainContent

import com.example.ypackfood.models.commonData.Dish

data class FilteredDishes(
    val categoryTypeDto: String,
    val dishes: List<Dish>
)