package com.dicoding.storyapp.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.remote.response.InsertStoryResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.response.Story
import com.dicoding.storyapp.data.remote.response.StoryResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.dicoding.storyapp.view.login.LoginPreferences
import com.dicoding.storyapp.view.setting.SettingPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    application: Application,
    private val loginPreferences: LoginPreferences
) {
    private val pref: SettingPreferences
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val apiService: ApiService = ApiConfig.getApiService()

    init {
        pref = SettingPreferences.getInstance(application.dataStore)
    }

    suspend fun saveTheme(darkMode: Boolean) = pref.saveThemeSetting(darkMode)

    fun getTheme() = pref.getThemeSetting()

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(
                name,
                email,
                password
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(
                email,
                password
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun listStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(loginPreferences, apiService)
            }
        ).liveData
    }

    fun listStoryLoc(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryLoc(
                token = "Bearer ${loginPreferences.getUser().token}",
                page = 1,
                size = 100,
                location = 1
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun insertStory(
        imageFile: MultipartBody.Part,
        desc: RequestBody,
        lat: Double,
        lon: Double
    ): LiveData<Result<InsertStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.insertStory(
                token = "Bearer ${loginPreferences.getUser().token}",
                file = imageFile,
                description = desc,
                lat = lat,
                lon = lon
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}