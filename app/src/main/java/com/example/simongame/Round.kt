package com.example.simongame

class Round() {
    private val sequence: MutableList<Color> = mutableListOf()

    fun addColor(color: Color) {
        sequence.add(color)
    }

    fun getSequence(): List<Color> = sequence
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
                "R" -> addColor(Color('R'))
                "G" -> addColor(Color('G'))
                "B" -> addColor(Color('B'))
                "M" -> addColor(Color('M'))
                "Y" -> addColor(Color('Y'))
                "C" -> addColor(Color('C'))
            }
        }
    }

}
