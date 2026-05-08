package com.example.simongame.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDAO {
    // retrieve all saved games, from newest to oldest
    @Query("SELECT * FROM `match` ORDER BY timestamp DESC")
    suspend fun getAllMatches(): List<GameMatch>

    // save a game to the database
    @Insert
    suspend fun insertMatch(match: GameMatch)
}