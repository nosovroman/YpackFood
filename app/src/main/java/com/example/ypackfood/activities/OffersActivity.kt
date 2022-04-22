package com.example.ypackfood.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ypackfood.common.Constants
import com.example.ypackfood.components.*
import com.example.ypackfood.viewModels.OfferViewModel

@Preview
@Composable
fun Temp() {
    Column(
        modifier = Modifier.shadow(elevation = 5.dp, shape = RectangleShape)
            .padding(top = 5.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)

        ,
        //.border(1.dp, color = MaterialTheme.colors.onBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            buildAnnotatedString {
                append("Стоимость: ")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    append("120 ₽")
                }
                append(" 100 ₽")
            }
        )
        Button(
            onClick = {  },
            content = { Text("Добавить в корзину") },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun OffersScreen(navController: NavHostController, offerViewModel: OfferViewModel, offerId: Int) {
    LaunchedEffect(offerId) {
        offerViewModel.getOfferContent(offerId)
    }

    val requestState = offerViewModel.contentResp.observeAsState().value

    val scrollState = rememberScrollState()
    Scaffold (
        topBar = { ToolbarComponent(navController = navController) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RectangleShape)
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),
                    //.border(1.dp, color = MaterialTheme.colors.onBackground),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    buildAnnotatedString {
                        append("Стоимость:\t")
                        withStyle(style = SpanStyle(color = Color.Gray, textDecoration = TextDecoration.LineThrough)) {
                            append(offerViewModel.totalOldPriceState.toString() + " ₽")
                        }
                        append(" " + offerViewModel.totalNewPriceState.toString() + " ₽")
                    }
                )
                Button(
                    onClick = {  },
                    content = { Text("Добавить в корзину") },
                    shape = RoundedCornerShape(20.dp)
                )
            }
        },
//        floatingActionButton = {
//            Button(
//                onClick = {  },
//                content = { Text("Добавить в корзину на ${offerViewModel.totalNewPriceState} ₽") }
//            )
//        },
//        floatingActionButtonPosition = FabPosition.Center,
        content = {
            if (!offerViewModel.contentResp.value?.data?.name.isNullOrEmpty()) {
                val response = offerViewModel.contentResp.value!!.data!!
                offerViewModel.setTotalOldPrice(response.dishes.sumOf { it.basePortion.oldPrice?.price ?: 0 })
                offerViewModel.setTotalNewPrice(response.dishes.sumOf { it.basePortion.priceNow.price })

                Column(
                    Modifier
                        .verticalScroll(scrollState)
                ) {
                    PictureTwoComponent(url = offerViewModel.contentResp.value!!.data!!.picturePaths.large)
                    OfferDescription(offerViewModel, Modifier.padding(start = 20.dp, end = 20.dp))
                }

                RequestStateComponent(
                    requestState = requestState,
                    byError = {
                        ShowErrorComponent(onButtonClick = { offerViewModel.getOfferContent(offerId) })
                    }
                )
            }
        }
    )
}

@Composable
fun OfferDescription(offerViewModel: OfferViewModel, modifier: Modifier = Modifier) {
    val response = offerViewModel.contentResp.value!!.data!!

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Описание", fontSize = 16.sp)
        Text(text = response.name, fontSize = 14.sp, color = Color.Gray)

        if (response.dishes.isNotEmpty()) {
            ContentSimpleListComponent(contentList = response.dishes)
            Spacer(modifier = Modifier.size(100.dp))
        }
        //Text(text = "Мега выгодное предложение от мегавыгодной сети ресторанов: 500 пицц по цене 1000 роллов", fontSize = 14.sp, color = Color.Gray)
        //ContentCardComponent(cardName = "Акция1")
        //ContentCardComponent(cardName = "Акция2")
    }
}

//@Composable
//fun OfferPicture() {
//    Image(
//        painter = rememberImagePainter(
//            "https://sun9-26.userapi.com/impf/c849020/v849020562/12056a/xOiO0cHdtkk.jpg?size=604x604&quality=96&sign=2c11f0e48c64e290d0bde943da845fd6&type=album"
//        ),
//        contentDescription = "Вкусняха",
//        modifier = Modifier
//            .clip(RoundedCornerShape(15.dp))
//            .fillMaxWidth()
//            .defaultMinSize(minHeight = 100.dp),
//        contentScale = ContentScale.FillWidth
//    )
//}

