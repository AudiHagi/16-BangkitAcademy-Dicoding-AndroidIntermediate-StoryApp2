package com.dicoding.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class InsertStoryResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)