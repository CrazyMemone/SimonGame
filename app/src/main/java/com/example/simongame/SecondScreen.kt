package com.example.simongame

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
// screen that displays the history of played rounds.
fun SecondScreen(rounds: List<Round>) {
    val orientation = LocalConfiguration.current.orientation
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // landscape
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 64.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(rounds) { round ->
                RoundItem(round = round)
            }
        }
    } else {
        // portrait
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 0.dp) // all available horizontal space
        ) {
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
            items(rounds) { round ->
                RoundItem(round = round)
            }
        }
    }
}

// represents a single row in the rounds history list.
@Composable
fun RoundItem(round: Round) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // circular container displaying the total count of colors in the sequence
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
                    color = Color.DarkGray,
                    lineHeight = 30.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        // column containing the sequence text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = round.printSequence(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, // visual indicator for truncated text (...)
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                lineHeight = 30.sp
            )
        }
    }

}