package com.example.simongame

class GameHistory {
    val completedRounds = mutableListOf<Round>()

    fun addRound(round: Round) {
        completedRounds.add(round.copy())
    }
    fun getRounds(): List<Round> {
        return completedRounds
    }
}