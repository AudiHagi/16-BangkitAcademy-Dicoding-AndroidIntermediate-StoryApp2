package com.dicoding.storyapp.view.map

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.view.login.LoginPreferences

class MapViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) : ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    fun getStories() = repository.listStoryLoc()
}