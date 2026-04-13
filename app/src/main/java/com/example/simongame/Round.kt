package com.example.simongame
//rappresenta singola partita
class Round() {
    //lista colori selezionati
    private val sequence: MutableList<GameColor> = mutableListOf()
    //aggiunge colore alla lista

    fun addColor(gameColor: GameColor) {
        sequence.add(gameColor)
    }

    fun getSequence(): List<GameColor> = sequence
    //restituisce la lista come stringa
    fun printSequence(): String = sequence.joinToString(", ") { it.printLetter() }
    //svuota la lista corrente
    fun clear(){
        sequence.clear()
    }
    //restituisce una copia della partita corrente
    fun copy(): Round {
        val newRound = Round()
        for (color in this.sequence) newRound.addColor(color)
        return newRound
    }
    //ricostruisce partendo da una stringa
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
    // Restituisce il numero di colori premuti nella sequenza
    fun getCount(): Int = sequence.size

}
