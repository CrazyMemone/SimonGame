package com.example.simongame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simongame.data.DatabaseHelper
import com.example.simongame.ui.theme.SimonGameTheme


class MainActivity : ComponentActivity() {

    private lateinit var soundPool: android.media.SoundPool
    private val soundIds = IntArray(6)
    private var errorSoundId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the SQLite native helper (create or open the simon_database.db file)
        val dbHelper = DatabaseHelper(this)

        val audioAttributes = android.media.AudioAttributes.Builder()
            .setUsage(android.media.AudioAttributes.USAGE_GAME)
            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = android.media.SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttributes)
            .build()

        // Caricamento dei file inseriti nella cartella raw
        soundIds[0] = soundPool.load(this, R.raw.rosso, 1)
        soundIds[1] = soundPool.load(this, R.raw.verde, 1)
        soundIds[2] = soundPool.load(this, R.raw.blu, 1)
        soundIds[3] = soundPool.load(this, R.raw.magenta, 1)
        soundIds[4] = soundPool.load(this, R.raw.giallo, 1)
        soundIds[5] = soundPool.load(this, R.raw.ciano, 1)
        errorSoundId = soundPool.load(this, R.raw.errore, 1)

        setContent {
            SimonGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // Compose's reactive state containing the updated list of matches from the DB
                    var matches by remember { mutableStateOf(dbHelper.getAllMatches()) }

                    NavHost(
                        navController = navController,
                        startDestination = "history"
                    ) {
                        composable(route = "history") {
                            SecondScreen(
                                matches = matches,
                                onPlayClick = { navController.navigate(route = "main") },
                                onMatchClick = { index ->
                                    navController.navigate(route = "detail/$index")
                                }
                            )
                        }

                        // MainScreen
                        composable(route = "main") {
                            MainScreen(
                                soundPool = soundPool,
                                soundIds = soundIds,
                                errorSoundId = errorSoundId,
                                onEndGame = { sequence, score ->
                                    // mantieni la tua logica di salvataggio esistente...
                                    dbHelper.insertMatch(
                                        maxCorrectLength = score,
                                        fullSequence = sequence,
                                        errorIndex = -1
                                    )
                                    // We immediately update the state by reading data from the DB
                                    // This causes the list in SecondScreen to update instantly!
                                    matches = dbHelper.getAllMatches()
                                    navController.popBackStack(route = "history", inclusive = false)
                                }
                            )
                        }

                        // DetailScreen
                        composable(route = "detail/{matchId}") { backStackEntry ->
                            val indexString = backStackEntry.arguments?.getString("matchId")
                            val index = indexString?.toIntOrNull()

                            // Find the correct item in the list based on the clicked position
                            val selectedMatch = index?.let { matches.getOrNull(it) }

                            DetailScreen(match = selectedMatch)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Rilascia le risorse del SoundPool per evitare memory leak quando l'Activity viene distrutta
        soundPool.release()
    }
}