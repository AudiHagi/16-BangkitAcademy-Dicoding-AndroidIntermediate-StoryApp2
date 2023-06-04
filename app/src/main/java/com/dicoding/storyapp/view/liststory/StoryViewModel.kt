package com.dicoding.storyapp.view.liststory

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStory() = storyRepository.listStory()
}