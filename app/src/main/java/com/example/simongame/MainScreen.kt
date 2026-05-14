package com.example.simongame

import android.content.res.Configuration
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(onEndGame: (String, Int) -> Unit) {
    var computerSequence by rememberSaveable { mutableStateOf(listOf<Char>()) }
    var userSequence by rememberSaveable { mutableStateOf(listOf<Char>()) }
    var isComputerPlaying by rememberSaveable { mutableStateOf(false) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var gameStarted by rememberSaveable { mutableStateOf(false) }
    var activeColorIndex by rememberSaveable { mutableIntStateOf(-1) }
    var playbackIndex by rememberSaveable { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()
    val orientation = LocalConfiguration.current.orientation
    val colorChars = listOf('R', 'G', 'B', 'M', 'Y', 'C')

    // Persistent ToneGenerator to prevent sound loss during long matches
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }

    // Release sound resources when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            toneGenerator.release()
        }
    }

    // play the tone associated with the color index using the persistent generator
    fun playSound(idx: Int) {
        if (idx < 0) return
        try {
            toneGenerator.startTone(ToneGenerator.TONE_DTMF_0 + idx, 200)
        } catch (e: Exception) {}
    }

    // handle the computer proposal sequence with pause logic
    LaunchedEffect(isComputerPlaying, isPaused) {
        // The computer only acts if it is its turn and if the game is not paused
        if (isComputerPlaying && !isPaused) {
            // Loop that loops through the generated sequence until it reaches the end
            // 'playbackIndex' to see where we left off (useful after a pause)
            while (playbackIndex < computerSequence.size) {
                val char = computerSequence[playbackIndex]
                val idx = colorChars.indexOf(char)
                // Set activeColorIndex to the found value. This makes the button glow
                activeColorIndex = idx
                // Plays the sound corresponding to the key
                playSound(idx)
                delay(800)
                activeColorIndex = -1
                delay(200)
                //  Increment the index to advance to the next color in the sequence in the next loop
                playbackIndex++
            }
            // Once the entire sequence is complete: the computer stops playing and passes the turn to the user
            isComputerPlaying = false
            playbackIndex = 0
        }
    }

    // finalize the game and send data to the activity
    val finalize = {
        if (computerSequence.size <= 1 && userSequence.isEmpty()) {
            onEndGame("", -1)
        } else {
            onEndGame(computerSequence.joinToString(", "), userSequence.size)
        }
    }

    BackHandler { finalize() }

    // logic for handling color rectangle pressure
    fun handleColorClick(char: Char) {
        if (isComputerPlaying || !gameStarted) return
        val idx = colorChars.indexOf(char)
        playSound(idx)

        if (char == computerSequence[userSequence.size]) {
            userSequence = userSequence + char
            if (userSequence.size == computerSequence.size) {
                scope.launch {
                    delay(500)
                    userSequence = emptyList()
                    computerSequence = computerSequence + colorChars.random()
                    isComputerPlaying = true
                }
            }
        } else {
            finalize()
        }
    }

    // Responsive layout based on orientation
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape: grid on the left, controls on the right
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp), // Spacing from phone edge
            verticalAlignment = Alignment.CenterVertically // Center elements vertically
        ) {
            // Grid on the left side
            Box(modifier = Modifier.padding(end = 24.dp)) {
                ColorGrid(activeIndex = activeColorIndex, onColorPressed = { handleColorClick(it) })
            }

            // Command column
            Column(
                modifier = Modifier.width(400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SequenceText(sequence = if (isComputerPlaying) "" else userSequence.joinToString(", "))

                ButtonsArea(
                    started = gameStarted,
                    playing = isComputerPlaying,
                    paused = isPaused,
                    onStart = {
                        gameStarted = true
                        computerSequence = listOf(colorChars.random())
                        isComputerPlaying = true
                    },
                    onPause = { isPaused = !isPaused },
                    onEnd = { finalize() }
                )
            }
        }
    } else {
        // Portrait: elements stacked vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center the grid and other elements
        ) {
            ColorGrid(activeIndex = activeColorIndex, onColorPressed = { handleColorClick(it) })

            SequenceText(sequence = if (isComputerPlaying) "" else userSequence.joinToString(", "))

            ButtonsArea(
                started = gameStarted,
                playing = isComputerPlaying,
                paused = isPaused,
                onStart = {
                    gameStarted = true
                    computerSequence = listOf(colorChars.random())
                    isComputerPlaying = true
                },
                onPause = { isPaused = !isPaused },
                onEnd = { finalize() }
            )
        }
    }
}

@Composable
fun ColorGrid(activeIndex: Int, onColorPressed: (Char) -> Unit) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Yellow, Color.Cyan)
    val chars = listOf('R', 'G', 'B', 'M', 'Y', 'C')

    Column {
        // Iterate through rows to generate the grid
        for (row in 0..2) {
            Row {
                for (col in 0..1) {
                    val index = row * 2 + col
                    // Clickable colored rectangle
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .alpha(if (activeIndex == index) 1f else 0.4f)
                            .background(colors[index])
                            .clickable { onColorPressed(chars[index]) }
                    )
                }
            }
        }
    }
}

@Composable
// Text area displaying the current sequence
fun SequenceText(sequence: String) {
    val scrollState = rememberScrollState()
    Text(
        text = sequence,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp))
            .border(width = 2.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(4.dp))
            .verticalScroll(scrollState)
            .padding(12.dp),
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 30.sp
    )
}

@Composable
// Area with game control buttons
fun ButtonsArea(
    started: Boolean,
    playing: Boolean,
    paused: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onEnd: () -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onStart,
            enabled = !started,
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
        ) {
            Text(text = "Avvia Partita")
        }

        Button(
            onClick = onPause,
            enabled = playing,
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
        ) {
            Text(text = if (paused) "Riprendi" else "Pausa")
        }

        Button(
            onClick = onEnd,
            enabled = started,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Fine Partita")
        }
    }
}