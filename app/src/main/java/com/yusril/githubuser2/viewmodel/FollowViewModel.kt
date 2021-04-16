package com.yusril.githubuser2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.yusril.githubuser2.BuildConfig
import com.yusril.githubuser2.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowViewModel : ViewModel() {
    private val listFollowers = MutableLiveData<ArrayList<User>>()

    companion object {
        private const val GITHUB_API = BuildConfig.GITHUB_API_KEY
    }

    fun setUser(followUrl: String) {
        val listItems = ArrayList<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $GITHUB_API")
        client.addHeader("User-Agent", "request")
        client.get(followUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
            ) {
                try {
                    //parsing json
                    val result = responseBody?.let { String(it) }
                    val list = JSONArray(result)

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val username = user.getString("login")
                        val avatar = user.getString("avatar_url")
                        val detailUrl = user.getString("url")
                        val followersUrl = user.getString("followers_url")
                        val followingUrl = "https://api.github.com/users/${username}/following"
                        val followerItems =
                            User(username, avatar, detailUrl, followersUrl, followingUrl)
                        listItems.add(followerItems)
                    }
                    listFollowers.postValue(listItems)
                    Log.d("TAG", listFollowers.toString())
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listFollowers
    }
}