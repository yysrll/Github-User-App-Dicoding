package com.yusril.githubuser2.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite")
data class User(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "avatar")
    var avatar: String?,
    @ColumnInfo(name = "detail_url")
    var detail_url: String?,
    @ColumnInfo(name = "followers_url")
    var followers_url: String?,
    @ColumnInfo(name = "following_url")
    var following_url: String?
) : Parcelable