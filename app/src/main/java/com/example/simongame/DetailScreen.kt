package com.example.simongame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable

fun DetailScreen(matchId: Int?, allRounds: List<String>) {
    // Retrieve match data from the list based on the provided matchId
    val roundData = remember(matchId) {
        if (matchId != null && matchId in allRounds.indices) {
            val r = Round()
            r.fromString(allRounds[matchId])
            r
        } else null
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

        if (roundData != null) {
            val fullSequence = roundData.printSequence()
            val score = roundData.getCount() - 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // left side: circle container for the score
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

                // Right side: match sequence details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sequence recorded:",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = fullSequence,
                        fontSize = 22.sp,
                        lineHeight = 30.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Use the system back button to return to the list",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}