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
import com.example.simongame.data.GameMatch

@Composable
fun DetailScreen(match: GameMatch?) {
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
        if (match!=null) {
            val tempRound = Round()
            // Passes the clean color string to the class method to rebuild the list of GameColor objects in memory
            tempRound.fromString(match.fullSequence)
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
                            text =match.maxCorrectLength.toString(),
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
                            val colors = if (formattedSequence.isNotEmpty()) formattedSequence.split(", ") else emptyList()
                            colors.forEachIndexed { index, color ->
                                val isErrorOrBeyond = index >= match.maxCorrectLength
                                withStyle(style = SpanStyle(
                                    color = if (isErrorOrBeyond) Color.Red else Color.Unspecified,
                                    fontWeight = if (isErrorOrBeyond) FontWeight.Bold else FontWeight.Normal
                                )) {
                                    append(color)
                                }
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