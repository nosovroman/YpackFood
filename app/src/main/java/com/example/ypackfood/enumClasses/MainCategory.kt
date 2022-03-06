package com.example.ypackfood.enumClasses

import com.example.ypackfood.enumClasses.MainCategory.*

enum class MainCategory(val categoryName: String) {
    PIZZA("Пицца"),
    ROLLS("Роллы"),
    SALADS("Салаты"),
    SOUPS("Супы"),
    DRINKS("Напитки")
}

fun getAllCategories():  List<MainCategory> {
    return listOf(PIZZA, ROLLS, SALADS, SOUPS, DRINKS)
}

fun getCategory(value: String): MainCategory? {
    val map = MainCategory.values().associateBy(MainCategory::categoryName)
    return map[value]
}