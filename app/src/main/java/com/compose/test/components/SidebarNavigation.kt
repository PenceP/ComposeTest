package com.compose.test.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.compose.test.navigation.Destinations

@Composable
fun SidebarNavItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    isExpanded: Boolean,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChanged(isFocused)
            }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessHigh
                )
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isFocused && !isSelected -> Color.White.copy(alpha = 0.6f)  // Highest for focus
                    isSelected -> Color.White.copy(alpha = 0.4f)              // Lower for selected  
                    else -> Color.Transparent                                   // None
                }
            )
    ) {
        if (isExpanded) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = when {
                        isSelected -> Color.White
                        isFocused -> Color.Black
                        else -> Color.White.copy(alpha = 0.7f)
                    },
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = title,
                    color = when {
                        isSelected -> Color.White
                        isFocused -> Color.Black
                        else -> Color.White.copy(alpha = 0.7f)
                    },
                    fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = when {
                        isSelected -> Color.White
                        isFocused -> Color.Black
                        else -> Color.White.copy(alpha = 0.7f)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun SidebarNavigation(
    navController: NavController,
    onWidthChanged: (Boolean) -> Unit = {}
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    var focusedItems by remember { mutableStateOf(setOf<String>()) }
    val isExpanded = focusedItems.isNotEmpty()
    var shouldRequestFocus by remember { mutableStateOf(false) }
    
    val sidebarWidth by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 72.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "sidebar_width"
    )
    
    // Notify parent about width changes
    onWidthChanged(isExpanded)

    val navigationItems = listOf(
        Triple(Icons.Default.Search, "Search", Destinations.Search.route),
        Triple(Icons.Default.Home, "Home", Destinations.Home.route),
        Triple(Icons.Default.Movie, "Movies", Destinations.Movies.route),
        Triple(Icons.Default.Tv, "TV Shows", Destinations.TvShows.route),
        Triple(Icons.Default.Settings, "Settings", Destinations.Settings.route)
    )
    
    // Create focus requesters for each item
    val focusRequesters = remember {
        navigationItems.associate { (_, _, route) -> route to FocusRequester() }
    }
    
    // Focus on selected item when expanding
    LaunchedEffect(isExpanded, currentRoute) {
        if (isExpanded && currentRoute != null && !shouldRequestFocus) {
            focusRequesters[currentRoute]?.requestFocus()
            shouldRequestFocus = true
        } else if (!isExpanded) {
            shouldRequestFocus = false
        }
    }

    Surface(
        modifier = Modifier
            .width(sidebarWidth)
            .fillMaxHeight(),
        color = Color.Black.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = if (isExpanded) 16.dp else 8.dp,
                    vertical = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = if (isExpanded) Alignment.Start else Alignment.CenterHorizontally
        ) {
            navigationItems.forEach { (icon, title, route) ->
                val isSelected = currentRoute == route
                
                SidebarNavItem(
                    icon = icon,
                    title = title,
                    isSelected = isSelected,
                    isExpanded = isExpanded,
                    focusRequester = focusRequesters[route]!!, 
                    onFocusChanged = { focused ->
                        focusedItems = if (focused) {
                            focusedItems + route
                        } else {
                            focusedItems - route
                        }
                    },
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