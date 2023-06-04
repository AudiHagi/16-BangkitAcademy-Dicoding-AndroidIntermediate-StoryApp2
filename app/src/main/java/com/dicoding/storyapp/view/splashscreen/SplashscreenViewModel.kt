package com.dicoding.storyapp.view.splashscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.view.login.LoginPreferences

class SplashscreenViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) :
    ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    fun getThemeSetting() = repository.getTheme().asLiveData()
}