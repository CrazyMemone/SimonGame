package com.example.simongame.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class MatchProvider : ContentProvider() {

    private var dbHelper: DatabaseHelper? = null

    companion object {
        const val AUTHORITY = "com.example.simongame.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/matches")

        private const val MATCHES = 1
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "matches", MATCHES)
        }
    }

    override fun onCreate(): Boolean {
        val ctx = context ?: return false
        dbHelper = DatabaseHelper(ctx)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper?.readableDatabase ?: throw IllegalStateException("Database non inizializzato")
        return when (uriMatcher.match(uri)) {
            MATCHES -> {
                db.query("matches", projection, selection, selectionArgs, null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("URI Sconosciuta: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper?.writableDatabase ?: throw IllegalStateException("Database non inizializzato")
        return when (uriMatcher.match(uri)) {
            MATCHES -> {
                val id = db.insert("matches", null, values)
                if (id > 0) {
                    context?.contentResolver?.notifyChange(uri, null)
                    Uri.withAppendedPath(CONTENT_URI, id.toString())
                } else {
                    null
                }
            }
            else -> throw IllegalArgumentException("URI Sconosciuta: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            MATCHES -> "vnd.android.cursor.dir/$AUTHORITY.matches"
            else -> throw IllegalArgumentException("URI Sconosciuta: $uri")
        }
    }
}