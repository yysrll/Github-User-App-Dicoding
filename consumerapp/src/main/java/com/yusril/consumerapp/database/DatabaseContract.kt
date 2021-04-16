package com.yusril.consumerapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.yusril.githubuser2"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "favorite"
            const val username = "username"
            const val avatar = "avatar"
            const val detail_url = "detail_url"
            const val followers_url = "followers_url"
            const val following_url = "following_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }

    }
}