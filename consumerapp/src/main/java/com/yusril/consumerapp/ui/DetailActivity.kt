package com.yusril.consumerapp.ui

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yusril.consumerapp.R
import com.yusril.consumerapp.adapter.SectionPagerAdapter
import com.yusril.consumerapp.database.DatabaseContract.UserColumns.Companion.avatar
import com.yusril.consumerapp.database.DatabaseContract.UserColumns.Companion.detail_url
import com.yusril.consumerapp.database.DatabaseContract.UserColumns.Companion.followers_url
import com.yusril.consumerapp.database.DatabaseContract.UserColumns.Companion.following_url
import com.yusril.consumerapp.databinding.ActivityDetailBinding
import com.yusril.consumerapp.model.DetailUser
import com.yusril.consumerapp.model.User
import com.yusril.consumerapp.util.MappingHelper.mapCursorToObject
import com.yusril.consumerapp.viewmodel.DetailUserViewModel
import com.yusril.consumerapp.viewmodel.FavoriteViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    private var isFavorite: Boolean = false
    private var username = ""

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val user = intent.getParcelableExtra(EXTRA_USER) as User
        username = user.username

        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()


        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        user.detail_url?.let { detailUserViewModel.setDetailUser(it) }

        detailUserViewModel.getDetailUser().observe(this, { userDetail ->
            if (userDetail != null) {
                showDetailUser(userDetail)
                showLoading(false)
            }
        })

        btnFavoriteChecked(username)
        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            when (isFavorite) {
                true -> insertFavorite(user)
                false -> deleteFavorite(user)
            }
        }
    }

    private fun btnFavoriteChecked(username: String) {
        isFavorite =
            (favoriteViewModel.getFavoriteByUsername(username)?.mapCursorToObject() != null)
        if (isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun insertFavorite(user: User) {
        val values = ContentValues()
        values.apply {
            put(username, user.username)
            put(avatar, user.avatar)
            put(detail_url, user.detail_url)
            put(followers_url, user.followers_url)
            put(following_url, user.following_url)
        }
        favoriteViewModel.insertFavorite(values)
        binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        Toast.makeText(this, getString(R.string.insert_success), Toast.LENGTH_SHORT).show()
    }

    private fun deleteFavorite(user: User) {
        favoriteViewModel.deleteFavorite(user.username)
        binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
    }

    private fun showDetailUser(userDetail: DetailUser) {
        Glide.with(this)
            .load(userDetail.avatar)
            .into(binding.includeHeader.detailAvatar)
        binding.includeHeader.detailRepo.text = userDetail.repository.toString()
        binding.includeHeader.detailFollowers.text = userDetail.followers.toString()
        binding.includeHeader.detailFollowing.text = userDetail.following.toString()

        if (userDetail.name == "null") {
            binding.detailName.visibility = View.GONE
        } else {
            binding.detailName.text = userDetail.name
        }
        if (userDetail.company == "null") {
            binding.detailCompany.visibility = View.GONE
        } else {
            binding.detailCompany.text = userDetail.company
        }
        if (userDetail.location == "null") {
            binding.detailLocation.visibility = View.GONE
        } else {
            binding.detailLocation.text = userDetail.location
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.includeHeader.detailAvatar.visibility = View.GONE
            binding.includeHeader.detailRepo.visibility = View.GONE
            binding.includeHeader.detailFollowers.visibility = View.GONE
            binding.includeHeader.detailFollowing.visibility = View.GONE
            binding.includeHeader.repo.visibility = View.GONE
            binding.includeHeader.followers.visibility = View.GONE
            binding.includeHeader.following.visibility = View.GONE
            binding.detailName.visibility = View.GONE
            binding.detailCompany.visibility = View.GONE
            binding.detailLocation.visibility = View.GONE
            binding.tabs.visibility = View.GONE
            binding.viewPager.visibility = View.GONE

            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.includeHeader.detailAvatar.visibility = View.VISIBLE
            binding.includeHeader.detailRepo.visibility = View.VISIBLE
            binding.includeHeader.detailFollowers.visibility = View.VISIBLE
            binding.includeHeader.detailFollowing.visibility = View.VISIBLE
            binding.includeHeader.repo.visibility = View.VISIBLE
            binding.includeHeader.followers.visibility = View.VISIBLE
            binding.includeHeader.following.visibility = View.VISIBLE
            binding.detailName.visibility = View.VISIBLE
            binding.detailCompany.visibility = View.VISIBLE
            binding.detailLocation.visibility = View.VISIBLE
            binding.tabs.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE

            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}