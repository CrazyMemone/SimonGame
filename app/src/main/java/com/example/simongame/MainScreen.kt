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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import android.content.res.Configuration

@Composable
fun MainScreen(onEndGame: (String) -> Unit) {
    //sequenza corrente come stringa

    var sequenza by rememberSaveable { mutableStateOf("") }
//aggiunge lettera colore premuto
    val orientation = LocalConfiguration.current.orientation

    fun onColorPressed(color: GameColor) {
        if (sequenza.isEmpty()) sequenza += color.printLetter()
        else sequenza += ", ${color.printLetter()}"
    }
// disposizione elementi
    // Layout diverso in base all'orientamento
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape: matrice a sinistra, testo e bottoni a destra
        Row {
            ColorGrid(onColorPressed = { onColorPressed(it) })
            Column {
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
    } else {
        // Portrait:  uno sotto l'altro
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

}

@Composable
fun ColorGrid(onColorPressed: (GameColor) -> Unit) {
//lista coppie
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
                    //rettangolo colorato cliccabile
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
//area testo con lista corrente
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
//area con pulsanti cancella e fine partita
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