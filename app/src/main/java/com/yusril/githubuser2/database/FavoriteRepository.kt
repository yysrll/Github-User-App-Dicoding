package com.yusril.githubuser2.database

import android.app.Application
import android.database.Cursor
import com.yusril.githubuser2.model.User
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteRepository(private val application: Application) {

    private val favoriteDao: FavoriteDao?
    private var favorites: Cursor? = null
    private var favoriteUsername: Cursor? = null

    init {
        val db = FavoriteDatabase.getInstance(application.applicationContext)
        favoriteDao = db.favoriteDao()
    }

    fun getFavorites(): Cursor? {
        favorites = favoriteDao?.getFavorites()
        return favorites
    }

    fun getFavoriteByUsername(username: String): Cursor? {
        favoriteUsername = favoriteDao?.getFavoriteByUsername(username)
        return favoriteUsername
    }

    fun insert(user: User) {
        favoriteDao?.insertFavorite(user)
    }

    fun delete(username: String) {
        favoriteDao?.deleteFavorite(username)
    }
}