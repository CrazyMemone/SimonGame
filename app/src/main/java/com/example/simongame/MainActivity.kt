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
                            // Transform the list of strings into a list of Round objects
                            val roundList = mutableListOf<Round>()
                            for (sequence in rounds) {
                                val round = Round()
                                round.fromString(sequence)
                                roundList.add(round)
                            }
                            SecondScreen(
                                rounds = roundList,
                                onPlayClick = { navController.navigate("main") },
                                onMatchClick = { index ->
                                    navController.navigate("detail/$index")
                                }
                            )
                        }

                        // MainScreen
                        composable("main") {
                            MainScreen(
                                onEndGame = { sequence, errorIdx ->
                                    if (errorIdx != -1) {
                                        rounds.add(sequence) // Add sequence to the list
                                    }
                                    navController.popBackStack()
                                }
                            )
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