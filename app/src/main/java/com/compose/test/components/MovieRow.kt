package com.compose.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MovieRow(
    rowNumber: Int,
    initialFocusPosition: Int = 0,
    onFocusPositionChanged: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var posters by remember { mutableStateOf((1..20).toList()) }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialFocusPosition)
    val focusRequesters = remember { List(400) { FocusRequester() } }
    
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= posters.size - 5 && posters.size < 400
        }
    }
    
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            val nextBatch = (posters.size + 1..minOf(posters.size + 20, 400)).toList()
            posters = posters + nextBatch
        }
    }
    
    // Track focus changes and report to parent
    LaunchedEffect(listState.firstVisibleItemIndex) {
        onFocusPositionChanged(listState.firstVisibleItemIndex)
    }
    
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Row $rowNumber",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            itemsIndexed(posters) { index, posterNumber ->
                PosterItem(
                    rowNumber = rowNumber,
                    posterNumber = posterNumber,
                    modifier = Modifier.focusRequester(focusRequesters[index])
                )
            }
        }
    }
}