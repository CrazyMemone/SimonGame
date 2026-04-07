package com.example.simongame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(onEndGame: (String) -> Unit) {

    var sequenza by rememberSaveable { mutableStateOf("") }

    fun onColorPressed(color: GameColor) {
        if (sequenza.isEmpty()) sequenza += color.printLetter()
        else sequenza += ", ${color.printLetter()}"
    }

    Column {
        ColorGrid(onColorPressed = { onColorPressed(it) })
        SequenceText(sequenza = sequenza)
        ButtonsArea(
            onClear = { sequenza = "" },
            onEndGame = {
                onEndGame(sequenza)
                sequenza = ""
            }
        )
    }
}
@Composable
fun ColorGrid(onColorPressed: (GameColor) -> Unit) {

    val colors = listOf(
        GameColor('R') to androidx.compose.ui.graphics.Color.Red,
        GameColor('G') to androidx.compose.ui.graphics.Color.Green,
        GameColor('B') to androidx.compose.ui.graphics.Color.Blue,
        GameColor('M') to androidx.compose.ui.graphics.Color.Magenta,
        GameColor('Y') to androidx.compose.ui.graphics.Color.Yellow,
        GameColor('C') to androidx.compose.ui.graphics.Color.Cyan
    )

    Column {
        for (row in 0..2) {
            Row {
                for (col in 0..1) {
                    val index = row * 2 + col
                    val (gameColor, composeColor) = colors[index]
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .background(composeColor)
                            .clickable { onColorPressed(gameColor) }
                    )
                }
            }
        }
    }
}
@Composable
fun SequenceText(sequenza: String) {
    Text(
        text = sequenza,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp),
        maxLines = 5
    )
}

@Composable
fun ButtonsArea(
    onClear: () -> Unit,
    onEndGame: () -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        Button(
            onClick = onClear,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(text = "Cancella")
        }

        Button(
            onClick = onEndGame
        ) {
            Text(text = "Fine partita")
        }
    }
}