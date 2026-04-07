package com.example.simongame

class Round() {
    private val sequence: MutableList<GameColor> = mutableListOf()

    fun addColor(gameColor: GameColor) {
        sequence.add(gameColor)
    }

    fun getSequence(): List<GameColor> = sequence
    fun printSequence(): String = sequence.joinToString(", ") { it.printLetter() }
    fun clear(){
        sequence.clear()
    }
    fun copy(): Round {
        val newRound = Round()
        for (color in this.sequence) newRound.addColor(color)
        return newRound
    }
    fun fromString(sequenza: String) {
        clear()
        if (sequenza.isEmpty()) return
        for (lettera in sequenza.split(", ")) {
            when (lettera) {
                "R" -> addColor(GameColor('R'))
                "G" -> addColor(GameColor('G'))
                "B" -> addColor(GameColor('B'))
                "M" -> addColor(GameColor('M'))
                "Y" -> addColor(GameColor('Y'))
                "C" -> addColor(GameColor('C'))
            }
        }
    }

}
