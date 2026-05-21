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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the SQLite native helper (create or open the simon_database.db file)
        val dbHelper = DatabaseHelper(this)
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
                                onPlayClick = { navController.navigate("main") },
                                onMatchClick = { index ->
                                    navController.navigate("detail/$index")
                                }
                            )
                        }

                        // MainScreen
                        composable("main") {
                            MainScreen(onEndGame = { sequence, score ->
                                if (sequence.isNotEmpty() && score != -1) {
                                    // Save the original computer string and the score separated by "|"
                                    dbHelper.insertMatch(
                                        maxCorrectLength = score, // Maximum score guessed (n)
                                        fullSequence = sequence,   // Error sequence (n+1)
                                        errorIndex = score         // Index where the error occurred
                                    )
                                    // We immediately update the state by reading data from the DB
                                    // This causes the list in SecondScreen to update instantly!
                                    matches = dbHelper.getAllMatches()
                                }
                                navController.popBackStack("history", inclusive = false)
                            })
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
}