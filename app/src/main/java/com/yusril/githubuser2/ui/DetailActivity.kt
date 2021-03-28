package com.yusril.githubuser2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yusril.githubuser2.R
import com.yusril.githubuser2.adapter.SectionPagerAdapter
import com.yusril.githubuser2.databinding.ActivityDetailBinding
import com.yusril.githubuser2.model.DetailUser
import com.yusril.githubuser2.model.User
import com.yusril.githubuser2.viewmodel.DetailUserViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

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
        supportActionBar?.title = user.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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