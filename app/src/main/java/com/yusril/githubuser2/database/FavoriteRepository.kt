package com.yusril.githubuser2.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.yusril.githubuser2.model.DetailUser
import com.yusril.githubuser2.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@InternalCoroutinesApi
class FavoriteRepository(private val application: Application) {

    private val favoriteDao: FavoriteDao?
    private var favorites: LiveData<List<User>>? = null
    private var favoriteUsername: LiveData<List<User>>? = null

    init {
        val db = FavoriteDatabase.getInstance(application.applicationContext)
        favoriteDao = db.favoriteDao()
        favorites = favoriteDao.getFavorites()
    }

    fun getFavorites(): LiveData<List<User>>? {
        return favorites
    }

    fun getFavoriteByUsername(username: String): LiveData<List<User>>? {
        favoriteUsername = favoriteDao?.getFavoriteByUsername(username)
        return favoriteUsername
    }

    fun insert(user: User) = runBlocking {
        this.launch(Dispatchers.IO) {
            favoriteDao?.insertFavorite(user)
        }
    }

    fun delete(user: User) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                favoriteDao?.deleteFavorite(user)
            }
        }
    }
}