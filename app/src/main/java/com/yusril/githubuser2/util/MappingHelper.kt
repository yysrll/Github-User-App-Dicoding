package com.yusril.githubuser2.util

import android.database.Cursor
import com.yusril.githubuser2.model.User

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<User> {
        val favList = ArrayList<User>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow("username"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                val detail_url = getString(getColumnIndexOrThrow("detail_url"))
                val followers_url = getString(getColumnIndexOrThrow("followers_url"))
                val following_url = getString(getColumnIndexOrThrow("following_url"))
                favList.add(User(username, avatar, detail_url, followers_url, following_url))
            }
        }
        return favList
    }

    fun Cursor.mapCursorToObject(): User? {
        this.apply {
            return if (moveToFirst()) {
                val username = getString(getColumnIndexOrThrow("username"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                val detail_url = getString(getColumnIndexOrThrow("detail_url"))
                val followers_url = getString(getColumnIndexOrThrow("followers_url"))
                val following_url = getString(getColumnIndexOrThrow("following_url"))
                User(username, avatar, detail_url, followers_url, following_url)
            } else {
                null
            }
        }
    }
}