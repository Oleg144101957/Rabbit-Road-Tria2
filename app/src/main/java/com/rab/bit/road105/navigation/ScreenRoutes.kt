package com.rab.bit.road105.navigation

sealed class ScreenRoutes(val route: String) {

    data object SplashScreen : ScreenRoutes(SPLASH_SCREEN)
    data object HomeScreen : ScreenRoutes(HOME_SCREEN)
    data object NoNetworkScreen : ScreenRoutes(NO_NETWORK_SCREEN)
    data object SettingsScreen : ScreenRoutes(SETTINGS_SCREEN)
    data object AboutScreen : ScreenRoutes(ABOUT_SCREEN)
    data object CatcherScreen : ScreenRoutes(CATCHER_SCREEN)
    data object TopGamesScreen : ScreenRoutes(TOP_GAMES_SCREEN)
    data object ContentScreen : ScreenRoutes(CONTENT_SCREEN)

    companion object {
        private const val SPLASH_SCREEN = "Splash_Screen"
        private const val HOME_SCREEN = "Home_Screen"
        private const val NO_NETWORK_SCREEN = "No_Network_Screen"
        private const val SETTINGS_SCREEN = "Settings_Screen"
        private const val ABOUT_SCREEN = "About_Screen"
        private const val CATCHER_SCREEN = "Catcher_Screen"
        private const val TOP_GAMES_SCREEN = "Top_Games_screen"
        private const val CONTENT_SCREEN = "Content_Screen"
    }
}