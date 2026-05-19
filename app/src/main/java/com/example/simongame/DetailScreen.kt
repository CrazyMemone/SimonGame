package com.example.simongame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailScreen(matchId: Int?, allRounds: List<String>) {
    // Retrieve match data from the list based on the provided matchId
    val rawData = remember(matchId) {
        if (matchId != null && matchId in allRounds.indices) {
            allRounds[matchId]
        } else ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Space to lower the title from the top edge
        Spacer(modifier = Modifier.height(70.dp))

        // Screen title
        Text(
            text = "Match Details",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Safety check e
        if (rawData.isNotEmpty()) {
            // Splits the raw data string into two separate parts using  "|"
            val parts = rawData.split("|")
            // Extracts the first element of the array (the color sequence)
            val sequencePart = parts.getOrNull(0) ?: ""
            // Extracts the second element (the score)
            val score = parts.getOrNull(1)?.toIntOrNull() ?: 0
            // Creates a temporary instance
            val tempRound = Round()
            // Passes the clean color string to the class method to rebuild the list of GameColor objects in memory
            tempRound.fromString(sequencePart)
            // Generates the final formatted color string based on the rules defined inside the class itself (e.g., via printLetter())
            val formattedSequence = tempRound.printSequence()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular container displaying the total count of colors in the sequence (scaled up)
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

                // Column containing the sequence text with dynamic color highlights (scaled up)
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
                        lineHeight = 30.sp
                    )
                }
            }
        }
    }
}