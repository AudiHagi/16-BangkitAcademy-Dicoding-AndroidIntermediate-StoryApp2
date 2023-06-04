package com.dicoding.storyapp.view.liststory

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.Story
import com.dicoding.storyapp.view.login.LoginPreferences

class ListStoryViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) : ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    val getListStory: LiveData<PagingData<Story>> = repository.listStory().cachedIn(viewModelScope)
}