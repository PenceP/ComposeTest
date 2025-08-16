package com.compose.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.compose.test.components.BlurredWallpaperBackground
import com.compose.test.components.SidebarNavigation
import com.compose.test.components.TopNavigationBar
import com.compose.test.data.AppSettings
import com.compose.test.data.NavigationType
import com.compose.test.navigation.NavGraph
import com.compose.test.ui.theme.ComposeTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    var appSettings by remember { mutableStateOf(AppSettings()) }
    var sidebarExpanded by remember { mutableStateOf(false) }
    
    val contentPadding by animateDpAsState(
        targetValue = if (sidebarExpanded) 16.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "content_padding"
    )
    
    BlurredWallpaperBackground {
        when (appSettings.navigationType) {
            NavigationType.TOP_NAVIGATION -> {
                Column {
                    TopNavigationBar(navController = navController)
                    NavGraph(
                        navController = navController,
                        currentSettings = appSettings,
                        onSettingsChanged = { newSettings -> 
                            appSettings = newSettings 
                        }
                    )
                }
            }
            NavigationType.SIDEBAR_NAVIGATION -> {
                Row {
                    SidebarNavigation(
                        navController = navController,
                        onWidthChanged = { expanded ->
                            sidebarExpanded = expanded
                        }
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = contentPadding)
                    ) {
                        NavGraph(
                            navController = navController,
                            currentSettings = appSettings,
                            onSettingsChanged = { newSettings -> 
                                appSettings = newSettings 
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    ComposeTestTheme {
        MainContent()
    }
}