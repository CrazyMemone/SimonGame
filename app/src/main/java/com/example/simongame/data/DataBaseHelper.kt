package com.example.simongame.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "simon_database.db", null, 1) {

    // It runs only once on installation to create the SQL table
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE match_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                max_correct_length INTEGER,
                full_sequence TEXT,
                error_index INTEGER,
                timestamp INTEGER
            )
        """.trimIndent())
    }
    // Handles database upgrades by dropping the old table and recreating it
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS match_history")
        onCreate(db)
    }

    // Inserts a new completed game into the database
    fun insertMatch(maxCorrectLength: Int, fullSequence: String, errorIndex: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("max_correct_length", maxCorrectLength)
            put("full_sequence", fullSequence)
            put("error_index", errorIndex)
            put("timestamp", System.currentTimeMillis())
            // Saves current time in milliseconds
        }
        db.insert("match_history", null, values)
        db.close() // Closes the database connection to prevent memory leaks
    }

    // Retrieves the list of all matches ordered from the most recent to the oldest
    fun getAllMatches(): List<GameMatch> {
        val list = mutableListOf<GameMatch>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM match_history ORDER BY timestamp DESC", null)
   // Iterates through the database rows and converts them into GameMatch objects
        if (cursor.moveToFirst()) {
            do {
                val match = GameMatch(
                    id = cursor.getInt(0),
                    maxCorrectLength = cursor.getInt(1),
                    fullSequence = cursor.getString(2),
                    errorIndex = cursor.getInt(3),
                    timestamp = cursor.getLong(4)
                )
                list.add(match)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}