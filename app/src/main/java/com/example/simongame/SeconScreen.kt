package com.example.simongame

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SeconScreen(rounds: List<Round>) {
    Column {
        Text(text = "Partite concluse: ${rounds.size}")
    }
}
