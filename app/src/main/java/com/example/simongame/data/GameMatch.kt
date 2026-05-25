package com.example.simongame.data

/**
 * Data class representing a single Simon game match history entry.
 */
data class GameMatch(
    val id: Int = 0,
    val maxCorrectLength: Int, // The 'n' score (maximum sequence correctly reproduced by the player)
    val fullSequence: String,  // The complete 'n+1' sequence where the first mistake occurred
    val errorIndex: Int,       // The exact index position where the player made the mistake
)