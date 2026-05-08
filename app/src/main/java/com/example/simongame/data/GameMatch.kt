package com.example.simongame.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match")
data class GameMatch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "max_correct_length")
    val maxCorrectLength: Int, // 'n'

    @ColumnInfo(name = "full_sequence")
    val fullSequence: String,  // sequence 'n+1'
    @ColumnInfo(name = "error_index")
    val errorIndex: Int,       // the index where the error occurred

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis() // to sort your history
)