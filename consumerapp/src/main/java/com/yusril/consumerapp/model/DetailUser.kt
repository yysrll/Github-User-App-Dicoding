package com.yusril.consumerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailUser(
    var username: String?,
    var name: String?,
    var location: String?,
    var company: String?,
    var avatar: String?,
    var repository: Int?,
    var followers: Int?,
    var following: Int?,
) : Parcelable