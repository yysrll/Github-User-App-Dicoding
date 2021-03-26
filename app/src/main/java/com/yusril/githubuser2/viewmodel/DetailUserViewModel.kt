package com.yusril.githubuser2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.yusril.githubuser2.model.DetailUser
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailUserViewModel : ViewModel() {

    private val listDetailUser = MutableLiveData<DetailUser>()

    fun setDetailUser(usernames: String) {

        val githubToken = "ab0793b78484cae33d7f0f53b8ec50713921c256"
        val url = "https://api.github.com/users/$usernames"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $githubToken")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseObject = JSONObject(result)

                    val username = responseObject.getString("login")
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val avatar = responseObject.getString("avatar_url")
                    val repository = responseObject.getInt("public_repos")
                    val followers = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")

                    val userDetail = DetailUser(
                        username,
                        name,
                        location,
                        company,
                        avatar,
                        repository,
                        followers,
                        following
                    )
                    listDetailUser.postValue(userDetail)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getDetailUser(): LiveData<DetailUser> {
        return listDetailUser
    }
}