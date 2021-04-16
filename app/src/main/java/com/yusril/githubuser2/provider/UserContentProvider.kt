package com.yusril.githubuser2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.yusril.githubuser2.database.FavoriteDao
import com.yusril.githubuser2.database.FavoriteDatabase
import com.yusril.githubuser2.model.fromContentValues
import kotlinx.coroutines.InternalCoroutinesApi

class UserContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.yusril.githubuser2"
        private const val SCHEME = "content"
        private const val TABLE_NAME = "favorite"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        private const val USER = 1
        private const val USERNAME = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteDao: FavoriteDao
        private lateinit var favoriteDatabase: FavoriteDatabase

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USERNAME)
        }

    }

    @InternalCoroutinesApi
    override fun onCreate(): Boolean {
        favoriteDatabase = FavoriteDatabase.getInstance(context as Context)
        favoriteDao = favoriteDatabase.favoriteDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> favoriteDao.getFavorites()
            USERNAME -> favoriteDao.getFavoriteByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> favoriteDao.insertFavorite(fromContentValues(values))
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USERNAME) {
            sUriMatcher.match(uri) -> favoriteDao.deleteFavorite(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}