package com.example.simongame

import android.content.ContentValues
import android.database.Cursor
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simongame.data.GameMatch
import com.example.simongame.data.MatchProvider
import com.example.simongame.ui.theme.SimonGameTheme


class MainActivity : ComponentActivity() {

    // Function that queries the Content Provider and scrolls the cursor
    private fun loadMatchesFromProvider(): List<GameMatch> {
        val matchList = mutableListOf<GameMatch>()

        // Query the Content Provider returns a cursor
        val cursor: Cursor? = contentResolver.query(
            MatchProvider.CONTENT_URI,
            null, null, null, null
        )

        // The .use block ensure that the Cursor is always safely closed at the end
        cursor?.use { c ->
            // Move the cursor to the first row of the query result
            if (c.moveToFirst()) {
                // Indexes of all four columns
                val idIdx = c.getColumnIndexOrThrow("_id")
                val scoreIdx = c.getColumnIndexOrThrow("max_correct_length")
                val sequenceIdx = c.getColumnIndexOrThrow("full_sequence")
                val errorIndexIdx = c.getColumnIndexOrThrow("error_index")

                do {
                    // Extract values from the cursor
                    val matchId = c.getInt(idIdx)
                    val score = c.getInt(scoreIdx)
                    val sequence = c.getString(sequenceIdx)
                    val errorIdx = c.getInt(errorIndexIdx)

                    // Map the data by associating it with the GameMatch parameters
                    matchList.add(
                        GameMatch(
                            id = matchId,
                            maxCorrectLength = score,
                            fullSequence = sequence,
                            errorIndex = errorIdx
                        )
                    )
                } while (c.moveToNext())
            }
        }
        return matchList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

        // Loading 6 color audio files + 1 error file from raw resources
        val soundIds = intArrayOf(
            soundPool.load(this, R.raw.rosso, 1),
            soundPool.load(this, R.raw.verde, 1),
            soundPool.load(this, R.raw.blu, 1),
            soundPool.load(this, R.raw.magenta, 1),
            soundPool.load(this, R.raw.giallo, 1),
            soundPool.load(this, R.raw.ciano, 1)
        )
        val errorSoundId = soundPool.load(this, R.raw.errore, 1)

        setContent {
            SimonGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var matches by remember { mutableStateOf(loadMatchesFromProvider()) }

                    NavHost(
                        navController = navController,
                        startDestination = "history"
                    ) {
                        // SecondScreen
                        composable(route = "history") {
                            SecondScreen(
                                matches = matches,
                                onPlayClick = { navController.navigate("main") },
                                onMatchClick = { index ->
                                    navController.navigate("detail/$index")
                                }
                            )
                        }

                        // MainScreen
                        composable(route = "main") {
                            MainScreen(
                                soundPool = soundPool,
                                soundIds = soundIds,
                                errorSoundId = errorSoundId,
                                onEndGame = { finalSequence, finalScore ->
                                    if (finalSequence.isNotEmpty() && finalScore != -1) {
                                        // Prepare the records by mapping them into a ContentValues object
                                        val values = ContentValues().apply {
                                            put("max_correct_length", finalScore)
                                            put("full_sequence", finalSequence)
                                            put("error_index", finalScore)
                                        }

                                        contentResolver.insert(MatchProvider.CONTENT_URI, values)

                                        // Update the state by reading data from the DB
                                        matches = loadMatchesFromProvider()
                                    }
                                    navController.popBackStack(route = "history", inclusive = false)
                                }
                            )
                        }

                        // DetailScreen
                        composable(
                            route = "detail/{matchId}",
                            // Zoom in
                            enterTransition = {
                                scaleIn(animationSpec = tween(400), initialScale = 0.5f) + fadeIn(animationSpec = tween(400))
                            },
                            // Zoom out
                            exitTransition = {
                                scaleOut(animationSpec = tween(400), targetScale = 0.5f) + fadeOut(animationSpec = tween(400))
                            }
                        ) { backStackEntry ->
                            val indexString = backStackEntry.arguments?.getString("matchId")
                            val index = indexString?.toIntOrNull()

                            val selectedMatch = index?.let { matches.getOrNull(it) }

                            DetailScreen(match = selectedMatch)
                        }
                    }
                }
            }
        }
    }
}