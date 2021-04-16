package com.yusril.githubuser2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusril.githubuser2.R
import com.yusril.githubuser2.adapter.UserAdapter
import com.yusril.githubuser2.database.FavoriteDao
import com.yusril.githubuser2.database.FavoriteDatabase
import com.yusril.githubuser2.databinding.ActivityFavoriteBinding
import com.yusril.githubuser2.model.User
import com.yusril.githubuser2.util.MappingHelper
import com.yusril.githubuser2.viewmodel.FavoriteViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var database: FavoriteDatabase
    private lateinit var dao: FavoriteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        database = FavoriteDatabase.getInstance(applicationContext)
        dao = database.favoriteDao()

        showLoading(true)
        showRecyclerView()
//        favoriteViewModel.getFavorites()?.observe(this, { user ->
//            if (user.isNullOrEmpty()) {
//                binding.tvNotFound.visibility = View.VISIBLE
//            } else {
//                binding.tvNotFound.visibility = View.GONE
//            }
//
//            showLoading(false)
//            adapter.setUser(user as ArrayList<User>)
//        })
    }

    override fun onResume() {
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        val getFavorite = favoriteViewModel.getFavorites()
        val userFav = MappingHelper.mapCursorToArrayList(getFavorite)
        if (userFav.isNullOrEmpty()) {
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.tvNotFound.visibility = View.GONE
        }

        showLoading(false)
        adapter.setUser(userFav)
        super.onResume()
    }

    private fun showRecyclerView() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        val rv = binding.recycleView
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        rv.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                showSelectedUser(user)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}