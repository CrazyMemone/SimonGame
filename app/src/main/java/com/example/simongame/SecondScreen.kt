package com.example.simongame

import android.content.res.Configuration
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
// Screen that displays the history of played rounds.
fun SecondScreen(
    rawRounds: List<String>,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 64.dp else 0.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(70.dp))

                    Text(
                        text = "Match list",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            // Match list
            itemsIndexed(rawRounds) { index, rawString ->
                RoundItem(rawString = rawString, onClick = { onMatchClick(index) })
            }
        }
    }
}

@Composable
// Represents a single element (row) within the match history list
fun RoundItem(rawString: String, onClick: () -> Unit) {
    // Safety check: if the stored string is empty, stop execution and display nothing
    if (rawString.isEmpty()) return
    // Split the raw string into two parts using the separator character "|"
    val parts = rawString.split("|")
    // / Get the first part (the sequence of letters from the computer)
    val sequencePart = parts.getOrNull(0) ?: ""
    // Get the second part (the user's score), converting it to an integer
    val score = parts.getOrNull(1)?.toIntOrNull() ?: 0
    // Create a temporary instance
    val tempRound = Round()
    tempRound.fromString(sequencePart)
    val formattedSequence = tempRound.printSequence()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(70.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = score.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildAnnotatedString {
                    // Split the formatted sequence into a list of individual color strings
                    val colors = if (formattedSequence.isNotEmpty()) formattedSequence.split(", ") else emptyList()

                    // Iterate through each color in the list along with its index position
                    colors.forEachIndexed { index, color ->
                        // Check if the current item is at or past the point where the user made an error
                        val isErrorOrBeyond = index >= score

                        // Apply a specific text style dynamically based on the game result
                        withStyle(style = SpanStyle(
                            // Highlight failed or subsequent colors in red
                            color = if (isErrorOrBeyond) Color.Red else Color.Unspecified,
                            // Make the error sequence bold for better visual emphasis
                            fontWeight = if (isErrorOrBeyond) FontWeight.Bold else FontWeight.Normal
                        )) {
                            // Append the current color string to the annotated sequence builder
                            append(color)
                        }

                        // Add a comma and space separator between colors, except after the last element
                        if (index < colors.size - 1) append(", ")
                    }
                },
                fontSize = 22.sp,
                lineHeight = 30.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}