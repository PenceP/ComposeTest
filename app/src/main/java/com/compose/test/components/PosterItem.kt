package com.compose.test.components

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PosterItem(
    rowNumber: Int,
    posterNumber: Int,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .width(120.dp)
            .aspectRatio(2f / 3f)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .focusable(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFocused) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) Color.White else Color.Gray
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isFocused) Color.White else Color.Gray
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${rowNumber}x${posterNumber}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (isFocused) Color.Black else Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}