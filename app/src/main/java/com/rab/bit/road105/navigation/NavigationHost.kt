package com.rab.bit.road105.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rab.bit.road105.ui.screens.AboutScreen
import com.rab.bit.road105.ui.screens.ContentScreen
import com.rab.bit.road105.ui.screens.HomeScreen
import com.rab.bit.road105.ui.screens.NoNetworkScreen
import com.rab.bit.road105.ui.screens.TopGamesScreen
import com.rab.bit.road105.ui.screens.games.CatcherScreen
import com.rab.bit.road105.ui.screens.settings.SettingsScreen
import com.rab.bit.road105.ui.screens.splash.SplashScreen

@Composable
fun NavigationHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = ScreenRoutes.SplashScreen.route) {
        composable(route = ScreenRoutes.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(route = ScreenRoutes.HomeScreen.route) {
            HomeScreen(navController, innerPadding)
        }

        composable(route = ScreenRoutes.NoNetworkScreen.route) {
            NoNetworkScreen(navController, innerPadding)
        }

        composable(route = ScreenRoutes.SettingsScreen.route) {
            SettingsScreen(navController, innerPadding)
        }

        composable(route = ScreenRoutes.AboutScreen.route) {
            AboutScreen(innerPadding)
        }

        composable(route = ScreenRoutes.CatcherScreen.route) {
            CatcherScreen(navController, innerPadding)
        }

        composable(route = ScreenRoutes.TopGamesScreen.route) {
            TopGamesScreen(navController, innerPadding)
        }

        composable(
            route = "${ScreenRoutes.ContentScreen.route}/{url}",
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            ContentScreen(navController, paddingValues = innerPadding, url)
        }
    }
}
