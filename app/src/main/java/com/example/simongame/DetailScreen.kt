package com.example.simongame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        Spacer(modifier = Modifier.height(130.dp))

        // Screen title - Centered and Enlarged
        Text(
            text = stringResource(id = R.string.title_detail),
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(40.dp))

        // Safety check
        if (match!=null) {
            val tempRound = Round()
            // Passes the clean color string to the class method to rebuild the list of GameColor objects
            tempRound.fromString(match.fullSequence)
            // Generates the final formatted color string based on the rules defined inside the class itself
            val formattedSequence = tempRound.printSequence()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular container displaying the total count of colors in the sequence (scaled up)
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.size(120.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = match.maxCorrectLength.toString(),
                                fontSize = 54.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(30.dp))

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
                            fontSize = 32.sp,
                            lineHeight = 42.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}