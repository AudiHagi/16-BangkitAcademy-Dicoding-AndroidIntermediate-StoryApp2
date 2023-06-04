package com.dicoding.storyapp.view.splashscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.remote.response.LoginResult
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.liststory.ListStoryActivity
import com.dicoding.storyapp.view.login.LoginActivity
import com.dicoding.storyapp.view.login.LoginPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashscreenViewModel: SplashscreenViewModel
    private lateinit var loginPreference: LoginPreferences
    private lateinit var loginModel: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        loginPreference = LoginPreferences(this)
        loginModel = loginPreference.getUser()
        splashscreenViewModel = obtainViewModel(this as AppCompatActivity)
        lifecycleScope.launch {
            delay(splashTime)
            withContext(Dispatchers.Main) {
                if (loginModel.name != null && loginModel.userId != null && loginModel.token != null) {
                    val intentToList =
                        Intent(this@SplashScreenActivity, ListStoryActivity::class.java)
                    startActivity(intentToList)
                    finish()
                } else {
                    val intentToLogin = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intentToLogin)
                    finish()
                }
            }
        }
        isDarkMode()
        setupView()
    }

    private fun obtainViewModel(activity: AppCompatActivity): SplashscreenViewModel {
        val loginPreferences = LoginPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[SplashscreenViewModel::class.java]
    }


    private fun isDarkMode() {
        splashscreenViewModel.getThemeSetting().observe(this) { isDark ->
            val mode =
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object {
        const val splashTime: Long = 1500
    }
}