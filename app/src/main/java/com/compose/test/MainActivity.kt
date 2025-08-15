package com.compose.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.compose.test.components.BlurredWallpaperBackground
import com.compose.test.components.TopNavigationBar
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
    
    BlurredWallpaperBackground {
        Column {
            TopNavigationBar(navController = navController)
            NavGraph(navController = navController)
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