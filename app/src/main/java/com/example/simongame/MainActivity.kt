package com.example.simongame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simongame.ui.theme.SimonGameTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimonGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // List of saved sequence stored as strings
                    val rounds = rememberSaveable {
                        mutableStateListOf<String>()
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "history"
                    ) {
                        composable(route = "history") {
                            SecondScreen(
                                rawRounds = rounds,
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
                                    rounds.add("$sequence|$score")
                                }
                                navController.popBackStack("history", inclusive = false)
                            })
                        }

                        // DetailScreen
                        composable(route = "detail/{matchId}") { backStackEntry ->
                            val matchIdString = backStackEntry.arguments?.getString("matchId")
                            val matchId = matchIdString?.toIntOrNull()

                            DetailScreen(matchId = matchId, allRounds = rounds)
                        }
                    }
                }
            }
        }
    }
}