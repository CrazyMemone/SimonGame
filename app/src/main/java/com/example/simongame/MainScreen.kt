package com.example.simongame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun MainScreen(onEndGame: (String) -> Unit) {

    var sequenza by rememberSaveable { mutableStateOf("") }

    fun onColorPressed(color: Color) {
        if (sequenza.isEmpty()) {
            sequenza += color.printLetter()
        } else {
            sequenza += ", ${color.printLetter()}"
        }
    }

    fun onClear() { sequenza = "" }

    fun endGame() {
        onEndGame(sequenza)
        sequenza = ""
    }
}