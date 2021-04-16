package com.yusril.githubuser2.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yusril.githubuser2.database.FavoriteRepository
import com.yusril.githubuser2.model.DetailUser
import com.yusril.githubuser2.model.User
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.IllegalArgumentException

@InternalCoroutinesApi
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = FavoriteRepository(application)
    private var favorites: Cursor? = repository.getFavorites()
    private var favoriteUsername: Cursor? = null

    fun getFavorites(): Cursor? {
        return favorites
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


//@Suppress("UNCHECKED_CAST")
//@InternalCoroutinesApi
//class FavoriteViewModelFactory(private val repository: FavoriteRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) return FavoriteViewModel(repository) as T
//        throw IllegalArgumentException("Unknown ViewModel Class")
//    }
//}
