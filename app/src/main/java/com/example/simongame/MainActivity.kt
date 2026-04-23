package com.example.simongame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simongame.ui.theme.SimonGameTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimonGameTheme {
                val navController = rememberNavController()
                // list of saved sequence stored as strings
                // rememberSaveable ensure the state survives configuration change
                val rounds = rememberSaveable {
                    mutableStateListOf<String>()
                }
                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    // MainScreen
                    composable("main") {
                        MainScreen(
                            onEndGame = { sequence ->
                                rounds.add(sequence); // added the current game sequence to history
                                // navigate to second screen (history)
                                navController.navigate("history")
                            }
                        )
                    }
                    // SecondScreen
                    composable(route = "history") {
                        // transform the list of strings into a list of Round objects
                        val roundList = mutableListOf<Round>()
                        for (sequence in rounds) {
                            val round = Round()
                            round.fromString(sequence)
                            roundList.add(round)
                        }
                        SecondScreen(rounds = roundList)
                    }
                }
            }
        }
    }
    }
