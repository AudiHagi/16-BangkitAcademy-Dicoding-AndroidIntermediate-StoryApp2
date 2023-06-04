package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.remote.response.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "Postingan $i",
                "Sender $i",
                "Ini Description Untuk Postingan $i",
                "https://www.pexels.com/photo/grayscale-photo-of-cliff-and-pine-trees-573863/",
                "2022-02-22T22:22:22Z",
                0.0,
                0.0
            )
            items.add(story)
        }
        return items
    }

}