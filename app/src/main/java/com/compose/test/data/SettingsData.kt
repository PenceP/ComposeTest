package com.compose.test.data

enum class NavigationType {
    TOP_NAVIGATION,
    SIDEBAR_NAVIGATION
}

data class AppSettings(
    val navigationType: NavigationType = NavigationType.TOP_NAVIGATION
)