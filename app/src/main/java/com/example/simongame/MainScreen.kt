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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(onEndGame: (String) -> Unit) {
    // sequenza corrente come stringa

    var sequenza by rememberSaveable { mutableStateOf("") }
// aggiunge lettera colore premuto
    val orientation = LocalConfiguration.current.orientation

    fun onColorPressed(color: GameColor) {
        if (sequenza.isEmpty()) sequenza += color.printLetter()
        else sequenza += ", ${color.printLetter()}"
    }

// disposizione elementi
    // Layout diverso in base all'orientamento
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape: matrice a sinistra, testo e bottoni a destra
        Row(
            modifier = Modifier
                .fillMaxSize() // occupa tutto lo schermo
                .padding(40.dp), // spazio dai bordi del telefono
            verticalAlignment = Alignment.CenterVertically // centra tutto verticalmente
        ) {
            // matrice a sinistra
            Box(modifier = Modifier.padding(end = 24.dp)) {
                ColorGrid(onColorPressed = { onColorPressed(it) })
            }

            // colonna comandi
            Column(
                modifier = Modifier.width(400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally // centra la matrice e gli altri elementi
        ){
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
// lista coppie
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
                    // rettangolo colorato cliccabile
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
// area testo con lista corrente
fun SequenceText(sequenza: String) {
    // stato per gestire la posizione dello scroll
    val scrollState = rememberScrollState()
    Text(
        text = sequenza,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(4.dp))
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
            // scorrimento verticale
            .verticalScroll(scrollState)
            .padding(12.dp),


        // stile testo
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        color = Color.DarkGray,
        lineHeight = 30.sp
    )
}

@Composable
// area con pulsanti cancella e fine partita
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
            Text(text = stringResource(R.string.cancella))
        }

        Button(
            onClick = onEndGame
        ) {
            Text(text = stringResource(R.string.fine_partita))
        }
    }
}