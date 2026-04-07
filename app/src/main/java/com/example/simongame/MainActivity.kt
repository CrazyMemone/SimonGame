package com.example.simongame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simongame.ui.theme.SimonGameTheme

class MainActivity : ComponentActivity() {

    val gameHistory = GameHistory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimonGameTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        MainScreen(
                            onEndGame = { sequenza ->
                                val round = Round()
                                round.fromString(sequenza)
                                gameHistory.addRound(round)
                                navController.navigate("history")
                            }
                        )
                    }
                    composable("history") {
                        SeconScreen(rounds = gameHistory.getRounds())
                    }
                }
            }
        }
    }
}