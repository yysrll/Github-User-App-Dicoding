package com.yusril.githubuser2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String?,
    var avatar: String?,
    var detail_url: String?,
    var followers_url: String?,
    var following_url: String?
) : Parcelable