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
    var sequence by rememberSaveable { mutableStateOf("") }

    val orientation = LocalConfiguration.current.orientation
    // add the letter of the color pressed to the sequence
    fun onColorPressed(color: GameColor) {
        if (sequence.isEmpty()) sequence += color.printLetter()
        else sequence += ", ${color.printLetter()}"
    }

    // responsive layout based on orientation
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape: grid on the left, controls on the right
        Row(
            modifier = Modifier
                .fillMaxSize() // fills the entire screen
                .padding(40.dp), // spacing from phone edge
            verticalAlignment = Alignment.CenterVertically // center elements vertically
        ) {
            // grid on the left side
            Box(modifier = Modifier.padding(end = 24.dp)) {
                ColorGrid(onColorPressed = { onColorPressed(it) })
            }

            // command column
            Column(
                modifier = Modifier.width(400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SequenceText(sequence = sequence)
                ButtonsArea(
                    onClear = { sequence = "" },
                    onEndGame = {
                        onEndGame(sequence)
                        sequence = ""
                    }
                )
            }
        }
    } else {
        // Portrait: elements stacked vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally // center the grid and other elements
        ){
            ColorGrid(onColorPressed = { onColorPressed(it) })
            SequenceText(sequence = sequence)
            ButtonsArea(
                onClear = { sequence = "" },
                onEndGame = {
                    onEndGame(sequence)
                    sequence = ""
                }
            )
        }
    }

 }


@Composable
fun ColorGrid(onColorPressed: (GameColor) -> Unit) {
// list of color pairs
    val colors = listOf(
        GameColor('R') to androidx.compose.ui.graphics.Color.Red,
        GameColor('G') to androidx.compose.ui.graphics.Color.Green,
        GameColor('B') to androidx.compose.ui.graphics.Color.Blue,
        GameColor('M') to androidx.compose.ui.graphics.Color.Magenta,
        GameColor('Y') to androidx.compose.ui.graphics.Color.Yellow,
        GameColor('C') to androidx.compose.ui.graphics.Color.Cyan
    )

    Column {
        // iterate through rows to generate the grid
        for (row in 0..2) {
            Row {
                // iterate through columns
                for (col in 0..1) {
                    val index = row * 2 + col
                    val (gameColor, composeColor) = colors[index]
                    // clickable colored rectangle
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
// text area displaying the current sequence
fun SequenceText(sequence: String) {
    // state to manage the scroll position
    val scrollState = rememberScrollState()
    Text(
        text = sequence,
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
            // vertical scrolling
            .verticalScroll(scrollState)
            .padding(12.dp),


        // text styling
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        color = Color.DarkGray,
        lineHeight = 30.sp
    )
}

@Composable
// area with clear and end game buttons
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