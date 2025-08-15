package com.compose.test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compose.test.screens.HomeScreen
import com.compose.test.screens.MoviesScreen
import com.compose.test.screens.SearchScreen
import com.compose.test.screens.SettingsScreen
import com.compose.test.screens.TvShowsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Home.route
    ) {
        composable(Destinations.Home.route) {
            HomeScreen()
        }
        composable(Destinations.Search.route) {
            SearchScreen()
        }
        composable(Destinations.Movies.route) {
            MoviesScreen()
        }
        composable(Destinations.TvShows.route) {
            TvShowsScreen()
        }
        composable(Destinations.Settings.route) {
            SettingsScreen()
        }
    }
}