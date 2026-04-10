package com.example.simongame

//gestisce lista partite concluse
class GameHistory {
    //lista partite concluse
    val completedRounds = mutableListOf<Round>()
    //aggiunge copia partita alla lista

    fun addRound(round: Round) {
        completedRounds.add(round.copy())
    }
    //restituisce lista parite concluse
    fun getRounds(): List<Round> {
        return completedRounds
    }
}