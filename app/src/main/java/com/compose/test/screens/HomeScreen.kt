package com.compose.test.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import com.compose.test.components.HeroSection
import com.compose.test.components.HomeRow
import com.compose.test.components.MovieRow

@Composable
fun HomeScreen() {
    var showGridView by remember { mutableStateOf(false) }
    val heroFocusPosition = remember { mutableStateOf(0) }
    
    if (!showGridView) {
        // Hero + Single Row View
        HeroWithSingleRow(
            onScrollDown = { showGridView = true },
            focusPosition = heroFocusPosition.value,
            onFocusPositionChanged = { heroFocusPosition.value = it }
        )
    } else {
        // Grid View with LazyColumn of LazyRows
        GridView(
            onScrollUp = { showGridView = false }
        )
    }
}

@Composable
private fun HeroWithSingleRow(
    onScrollDown: () -> Unit,
    focusPosition: Int,
    onFocusPositionChanged: (Int) -> Unit
) {
    val heroTitle = "Row 1 Posters"
    val heroDescription = "Welcome to your personalized home. Scroll down from the row below to explore more content organized in a grid layout."
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionDown) {
                    onScrollDown()
                    true
                } else {
                    false
                }
            }
    ) {
        HeroSection(
            title = heroTitle,
            description = heroDescription
        )
        
        HomeRow(
            rowNumber = 1,
            initialFocusPosition = focusPosition,
            onFocusPositionChanged = onFocusPositionChanged,
            onScrollDown = onScrollDown,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GridView(
    onScrollUp: () -> Unit
) {
    val rows = (1..20).toList()
    val listState = rememberLazyListState()
    val rowFocusPositions = remember { mutableStateMapOf<Int, Int>() }
    
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionUp && 
                    listState.firstVisibleItemIndex == 0) {
                    onScrollUp()
                    true
                } else {
                    false
                }
            }
    ) {
        items(rows) { rowNumber ->
            MovieRow(
                rowNumber = rowNumber,
                initialFocusPosition = rowFocusPositions[rowNumber] ?: 0,
                onFocusPositionChanged = { position ->
                    rowFocusPositions[rowNumber] = position
                }
            )
        }
    }
}