package com.example.ypackfood.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ypackfood.sealedClasses.Screens
import com.example.ypackfood.ui.theme.YpackFoodTheme

class NavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YpackFoodTheme {
                Navigator()
            }
        }
    }
}

@Composable
fun Navigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.SignInUp.route
    ) {
        composable(route = Screens.Main.route) { MainScreen() }
        composable(route = Screens.SignInUp.route) { SignInUpScreen() }
    }
}