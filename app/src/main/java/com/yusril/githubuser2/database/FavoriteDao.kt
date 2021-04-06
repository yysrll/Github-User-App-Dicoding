package com.yusril.githubuser2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yusril.githubuser2.model.DetailUser
import com.yusril.githubuser2.model.User

@Dao
interface FavoriteDao {
    @Query("Select * from favorite")
    fun getFavorites(): LiveData<List<User>>

    @Query("Select * from favorite where username = :username")
    fun getFavoriteByUsername(username: String): LiveData<List<User>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(user: User)

    @Delete
    suspend fun deleteFavorite(user: User)
}