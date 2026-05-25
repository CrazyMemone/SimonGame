package com.example.simongame.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "SimonGame.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create the initial table of matches
        db.execSQL("""
            CREATE TABLE matches (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                max_correct_length INTEGER,
                full_sequence TEXT,
                error_index INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS matches")
        onCreate(db)
    }
}