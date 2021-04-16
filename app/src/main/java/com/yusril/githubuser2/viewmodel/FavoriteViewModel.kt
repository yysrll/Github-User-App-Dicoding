package com.yusril.githubuser2.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import com.yusril.githubuser2.database.FavoriteRepository
import com.yusril.githubuser2.model.User
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = FavoriteRepository(application)
    private var favoriteUsername: Cursor? = null

    fun getFavorites(): Cursor? {
        return repository.getFavorites()
    }

    fun getFavoriteByUsername(username: String): Cursor? {
        favoriteUsername = repository.getFavoriteByUsername(username)
        return favoriteUsername
    }

    fun insertFavorite(user: User) {
        repository.insert(user)
    }

    fun deleteFavorite(username: String) {
        repository.delete(username)
    }
}
