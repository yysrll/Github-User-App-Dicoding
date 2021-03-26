package com.yusril.githubuser2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.yusril.githubuser2.R
import com.yusril.githubuser2.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.github)
            .into(binding.logoGithub)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}