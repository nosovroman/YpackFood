package com.example.ypackfood.common
import com.example.ypackfood.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.reflect.Modifier

object Constants {
    const val ROOM_FAVORITES = "Favorites"
    const val APP_NAME_RUS = "Боярин"
    const val YPACK_ADDRESS = "Старая Станица, парк Лога"
    val TOOLBAR_HEIGHT = 48.dp
    const val START_DELIVERY = 13
    const val END_DELIVERY = 23
    const val MIN_PASSWORD_LEN = 6
    const val MAX_ORDERS_ON_PAGE = 10

    val STANDARD_PADDING = 15.dp
    val CARDS_PADDING = 25.dp
    val TITLE_SIZE = 20.sp

    const val PERSON_ID_DEFAULT = -1
    const val TOKEN_DEFAULT = ""

    const val CHOSE_ADDRESS = "Выберите адрес"

    val fontFamily = FontFamily(
        Font(R.font.beauty_font_one)
    )

    const val PDPP = "https://docs.google.com/document/d/1dz6SKzs8CzYGhz5m89zkWEtqKegnDj0q/edit"

    const val SOCKET_TIMEOUT_EXCEPTION = "Проблема с интернет-подключением"
    const val UNKNOWN_HOST_EXCEPTION = "Проблема с интернет-подключением"
    const val ERROR_SERVER = "Проблема с сервером"

    const val HEADER_AUTH = "Authorization"

    const val TIME_ORDER_MESSAGE = "Заказать можно с $START_DELIVERY:00 до $END_DELIVERY:00, минимальное время ожидания 45 минут (установится автоматически при попытке заказа за меньший интервал времени)."
    const val MIN_TIME_ORDER = 45

    const val NAV_KEY__CONTENT_ID = "contentId"
    const val NAV_KEY__OFFER_ID = "offerId"
    const val NAV_KEY__ORDER_COST = "orderCost"

    const val DELIVERY_COST = 200 // ₽

    const val BASE_URL = "https://super-food17.herokuapp.com/"
    val baseUrlPictureCategory = "https://sun9-26.userapi.com/impf/c849020/v849020562/12056a/xOiO0cHdtkk.jpg?size=604x604&quality=96&sign=2c11f0e48c64e290d0bde943da845fd6&type=album"
    val baseUrlPictureContent = "https://yesofcorsa.com/wp-content/uploads/2018/09/Chicken-Burger-Desktop-Wallpaper.jpg"

    const val INFO_CONTENT = "\t\tВы проголодались на работе? Или удобно устроились перед телевизором, " +
            "чтобы посмотреть фильм? А может, к Вам внезапно нагрянули друзья, а в холодильнике пусто?" +
            " Большой асортимент блюд от ресторана Боярин – отличный выход для любой из этих ситуаций!" +
            " Вам остается только определиться, что Вы хотите попробовать сегодня.\r\n\t\t" +
            "Приходите всей семьёй и отведайте вкуснейших блюд в ресторане Боярин!"
}