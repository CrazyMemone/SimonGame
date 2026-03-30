package com.example.simongame

class Round() {
    private val sequence: MutableList<Color> = mutableListOf()

    fun addColor(color: Color) {
        sequence.add(color)
    }

    fun getSequence(): List<Color> = sequence
    fun printSequence(): String = sequence.joinToString(", ") { it.printLetter() }
}
