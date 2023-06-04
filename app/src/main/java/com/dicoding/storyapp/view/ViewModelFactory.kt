package com.dicoding.storyapp.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.view.insertstory.InsertStoryViewModel
import com.dicoding.storyapp.view.liststory.ListStoryViewModel
import com.dicoding.storyapp.view.login.LoginPreferences
import com.dicoding.storyapp.view.login.LoginViewModel
import com.dicoding.storyapp.view.map.MapViewModel
import com.dicoding.storyapp.view.register.RegisterViewModel
import com.dicoding.storyapp.view.setting.AccountViewModel
import com.dicoding.storyapp.view.splashscreen.SplashscreenViewModel

class ViewModelFactory constructor(
    private val application: Application,
    private val loginPreferences: LoginPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> AccountViewModel(
                application,
                loginPreferences
            ) as T

            modelClass.isAssignableFrom(SplashscreenViewModel::class.java) -> SplashscreenViewModel(
                application,
                loginPreferences
            ) as T

            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> ListStoryViewModel(
                application,
                loginPreferences
            ) as T

            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(
                application,
                loginPreferences
            ) as T

            modelClass.isAssignableFrom(InsertStoryViewModel::class.java) -> InsertStoryViewModel(
                application,
                loginPreferences
            ) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                application,
                loginPreferences
            ) as T

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                application,
                loginPreferences
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application,
            loginPreferences: LoginPreferences
        ): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(application, loginPreferences)
            }
    }
}