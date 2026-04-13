package com.example.simongame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp

@Composable
fun SeconScreen(rounds: List<Round>) {
    LazyColumn {
        items(rounds) { round ->
            RoundItem(round = round)
        }
    }
}

// Singolo elemento della lista
@Composable
fun RoundItem(round: Round) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // numero colori premuti a sinistra
        Text(
            text = round.getCount().toString(),
            modifier = Modifier.padding(end = 30.dp)
        )
        // sequenza troncata a destra
        Text(
            text = round.printSequence(),
            maxLines = 1,
            overflow = Ellipsis
        )
    }
}