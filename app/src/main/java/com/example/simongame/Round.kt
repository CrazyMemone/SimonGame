package com.example.simongame
// represents a single game round
class Round() {
    // list of selected colors in the sequence
    private val sequence: MutableList<GameColor> = mutableListOf()
    // adds new color to the sequence
    fun addColor(gameColor: GameColor) {
        sequence.add(gameColor)
    }
    fun printSequence(): String = sequence.joinToString(", ") { it.printLetter() }
    // clear the current sequence
    fun clear(){
        sequence.clear()
    }
    // reconstructs the round sequence from a string.
    fun fromString(sequence: String) {
        clear()
        if (sequence.isEmpty()) return
        for (lecter in sequence.split(", ")) {
            when (lecter) {
                "R" -> addColor(GameColor('R'))
                "G" -> addColor(GameColor('G'))
                "B" -> addColor(GameColor('B'))
                "M" -> addColor(GameColor('M'))
                "Y" -> addColor(GameColor('Y'))
                "C" -> addColor(GameColor('C'))
            }
        }
    }
    // returns the number of colors in the sequence
    fun getCount(): Int = sequence.size

}
