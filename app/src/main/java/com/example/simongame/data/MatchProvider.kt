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

        // The UriMatcher is used to intercept incoming URIs and verify if they match supported patterns
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "matches", MATCHES)
        }
    }

    override fun onCreate(): Boolean {
        val ctx = context ?: return false
        // Initialize the DatabaseHelper
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

        // Verify if the received URI matches an address pattern that this provider can handle
        return when (uriMatcher.match(uri)) {
            MATCHES -> {
                // Open the database in read-only mode and execute the SQL query, returning the Cursor
                db.query("matches", projection, selection, selectionArgs, null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("URI Sconosciuta: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper?.writableDatabase ?: throw IllegalStateException("Database non inizializzato")

        return when (uriMatcher.match(uri)) {
            MATCHES -> {
                // Open the database in write mode to insert the row sent by the ContentResolver
                val id = db.insert("matches", null, values)
                if (id > 0) {
                    //  Notify any active observers that the data backing this URI has changed
                    context?.contentResolver?.notifyChange(uri, null)
                    // Return the specific URI of the newly created record by appending its generated unique ID
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
            // Return the MIME type for a directory/list of rows matching the authority schema
            MATCHES -> "vnd.android.cursor.dir/$AUTHORITY.matches"
            else -> throw IllegalArgumentException("URI Sconosciuta: $uri")
        }
    }
}