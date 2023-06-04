package com.dicoding.storyapp.view.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository

class LoginViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) : ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    fun userLogin(email: String, password: String) = repository.login(email, password)
}