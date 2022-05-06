package com.example.ypackfood.models.actionsContent

import com.example.ypackfood.models.commonData.PicturePaths

data class ActionsItem(
    val id: Int,
    val name: String,
    val picturePaths: PicturePaths,
    val categoryType: String = "Акции"
)