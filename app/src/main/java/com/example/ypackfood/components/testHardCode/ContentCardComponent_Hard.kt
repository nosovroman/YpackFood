package com.example.ypackfood.components.testHardCode

import androidx.compose.runtime.Composable
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.ContentCardComponent

@Composable
fun ContentCardComponent_Hard() {
    ContentCardComponent(
        cardName = "Бургер -100",
        hint = "-250 грамм",
        description = "Булочки для бургера, котлета говяжья, пармезан, помидор, лук синий, майонез, кетчуп",
        urlPicture = Constants.baseUrlPictureContent,
        price = -100,
        //onCardClick = { navController.navigate(route = Screens.DetailContent.createRoute(id)) }
    )
}