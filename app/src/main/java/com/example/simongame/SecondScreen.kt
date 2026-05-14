package com.example.simongame

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
// Screen that displays the history of played rounds.
fun SecondScreen(
    rounds: List<Round>,
    onPlayClick: () -> Unit,
    onMatchClick: (Int) -> Unit
) {
    val orientation = LocalConfiguration.current.orientation
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPlayClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Play")
            }
        }
    ) { paddingValues ->
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 64.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Use itemsIndexed to pass the position to the click listener
                itemsIndexed(rounds) { index, round ->
                    RoundItem(round = round, onClick = { onMatchClick(index) })
                }
            }
        } else {
            // Portrait
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 0.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }

                // Use itemsIndexed to pass the position to the click listener
                itemsIndexed(rounds) { index, round ->
                    RoundItem(round = round, onClick = { onMatchClick(index) })
                }
            }
        }
    }
}

// Represents a single row in the rounds history list.
@Composable
fun RoundItem(round: Round, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Handle click on the whole row
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular container displaying the total count of colors in the sequence
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(45.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = round.getCount().toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineHeight = 30.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Column containing the sequence text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = round.printSequence(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, // Visual indicator for truncated text (...)
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 30.sp
            )
        }
    }
}