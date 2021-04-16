package com.yusril.consumerapp.viewmodel

import android.app.Application
import android.content.ContentValues
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import com.yusril.consumerapp.database.FavoriteRepository
import com.yusril.consumerapp.model.User
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

    fun insertFavorite(values: ContentValues) {
        repository.insert(values)
    }

    fun deleteFavorite(username: String) {
        repository.delete(username)
    }
}
