package com.compose.test.components

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.compose.test.navigation.Destinations

@Composable
fun CustomNavItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .clip(RoundedCornerShape(24.dp))
            .background(
                when {
                    isFocused && !isSelected -> Color.White.copy(alpha = 0.6f)  // Highest for focus
                    isSelected -> Color.White.copy(alpha = 0.4f)              // Lower for selected  
                    else -> Color.Transparent                                   // None
                }
            )
    ) {
        Text(
            text = title,
            color = when {
                isSelected -> Color.White
                isFocused -> Color.Black
                else -> Color.White.copy(alpha = 0.7f)
            },
            fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 6.dp
            )
        )
    }
}

@Composable
fun TopNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationItems = listOf(
        "Search" to Destinations.Search.route,
        "Home" to Destinations.Home.route,
        "Movies" to Destinations.Movies.route,
        "TV series" to Destinations.TvShows.route,
        "Settings" to Destinations.Settings.route
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { (title, route) ->
                val isSelected = currentRoute == route
                
                CustomNavItem(
                    title = title,
                    isSelected = isSelected,
                    onClick = {
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}