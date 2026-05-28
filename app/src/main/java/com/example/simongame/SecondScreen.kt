package com.example.simongame

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simongame.data.GameMatch

@Composable
// Screen that displays the history of played rounds.
fun SecondScreen(
    matches: List<GameMatch>,
    onPlayClick: () -> Unit,
    onMatchClick: (Int) -> Unit
) {
    val orientation = LocalConfiguration.current.orientation
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onPlayClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = stringResource(id = R.string.btn_play)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.btn_play),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
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
                        text = stringResource(id = R.string.title_history),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            // Match list
            itemsIndexed(matches) { index, match ->
                RoundItem(match = match, onClick = { onMatchClick(index) })
            }
        }
    }
}

@Composable
// Represents a single element (row) within the match history list
fun RoundItem(match: GameMatch, onClick: () -> Unit) {
    val tempRound = Round()
    tempRound.fromString(match.fullSequence)
    val formattedSequence = tempRound.printSequence()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(70.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = match.maxCorrectLength.toString(),
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
                lineHeight = 30.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}