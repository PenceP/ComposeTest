package com.compose.test.navigation

sealed class Destinations(val route: String) {
    object Home : Destinations("home")
    object Search : Destinations("search")
    object Movies : Destinations("movies")
    object TvShows : Destinations("tv_shows")
    object Settings : Destinations("settings")
}