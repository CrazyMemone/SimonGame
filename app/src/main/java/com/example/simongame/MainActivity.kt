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
                // Lista delle sequenze salvate come stringhe
                // rememberSaveable sopravvive alla rotazione
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
                            onEndGame = { sequenza ->
                                rounds.add(sequenza);
                                // navigazione schermata 2
                                navController.navigate("history")
                            }
                        )
                    }
                    // SecondScreen
                    composable(route = "history") {
                        // converte le stringhe in Round usando un ciclo for
                        val roundList = mutableListOf<Round>()
                        for (sequenza in rounds) {
                            val round = Round()
                            round.fromString(sequenza)
                            roundList.add(round)
                        }
                        SeconScreen(rounds = roundList)
                    }
                }
            }
        }
    }
    }
