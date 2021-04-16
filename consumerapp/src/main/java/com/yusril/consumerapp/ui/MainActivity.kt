package com.yusril.consumerapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusril.consumerapp.R
import com.yusril.consumerapp.adapter.UserAdapter
import com.yusril.consumerapp.databinding.ActivityMainBinding
import com.yusril.consumerapp.model.User
import com.yusril.consumerapp.viewmodel.UserViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Consumer Github App"

        showRecyclerView()

        userViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        showLoading(false)
        binding.tvNotfound.visibility = View.VISIBLE

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.search

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(usernames: String?): Boolean {

                showLoading(true)
                val searchUrl = "https://api.github.com/search/users?q=$usernames"
                searchUrl.let { userViewModel.setUser(it) }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        userViewModel.getUser().observe(this, { listItems ->
            if (listItems.isNullOrEmpty()) {
                binding.tvNotfound.visibility = View.VISIBLE
            } else {
                binding.tvNotfound.visibility = View.GONE
            }

            showLoading(false)
            adapter.setUser(listItems)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
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
            binding.tvNotfound.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}