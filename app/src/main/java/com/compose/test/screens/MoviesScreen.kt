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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.compose.test.components.HeroSection
import com.compose.test.components.MovieRow

@Composable
fun MoviesScreen() {
    val rows = (1..10).toList()
    val listState = rememberLazyListState()
    
    // Track focus position for each row
    val rowFocusPositions = remember { mutableStateMapOf<Int, Int>() }
    
    val currentVisibleRow by remember {
        derivedStateOf {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            if (firstVisibleIndex == 0) 1 else firstVisibleIndex + 1
        }
    }
    
    val heroTitle = "Row $currentVisibleRow Posters"
    val heroDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeroSection(
            title = heroTitle,
            description = heroDescription
        )
        
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f)
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
}