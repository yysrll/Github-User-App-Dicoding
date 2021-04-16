package com.yusril.githubuser2.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.yusril.githubuser2.model.DetailUser
import com.yusril.githubuser2.model.User

@Dao
interface FavoriteDao {
    @Query("Select * from favorite")
    fun getFavorites(): Cursor?

    @Query("Select * from favorite where username = :username")
    fun getFavoriteByUsername(username: String): Cursor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: User): Long

    @Query("Delete from favorite where username = :username")
    fun deleteFavorite(username: String): Int
}