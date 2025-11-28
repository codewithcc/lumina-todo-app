package com.codewithcc.lumina

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codewithcc.lumina.model.Screens
import com.codewithcc.lumina.view.SplashScreen
import com.codewithcc.lumina.view.TodoScreen

@Composable
fun RootNavGraph(navHostController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Splash
    ) {
        composable<Screens.Splash> {
            SplashScreen {
                navHostController.popBackStack()
                navHostController.navigate(Screens.Main)
            }
        }

        composable<Screens.Main> {
            TodoScreen()
        }
    }
}