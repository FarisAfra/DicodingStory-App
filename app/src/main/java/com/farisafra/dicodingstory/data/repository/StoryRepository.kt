package com.farisafra.dicodingstory.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.farisafra.dicodingstory.data.paging.StoryPagingSource
import com.farisafra.dicodingstory.data.paging.StoryRemoteMediator
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.response.login.LoginResponse
import com.farisafra.dicodingstory.data.response.register.RegisterResponse
import com.farisafra.dicodingstory.data.response.story.AddStoryResponse
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.response.story.StoryResponse
import com.farisafra.dicodingstory.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val preference: LoginPreference, private val apiService: ApiService) {

    fun getStory(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(preference, apiService),
            pagingSourceFactory = {
                StoryPagingSource(apiService, preference)
            }
        ).liveData
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

    fun addStory(
        imageFile: MultipartBody.Part,
        desc: RequestBody,
        lat: Double,
        lon: Double
    ): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addStory(
                token = "Bearer ${preference.getLogin().token}",
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

    fun getStories(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories(
                token = "Bearer ${preference.getLogin().token}",
                page = 1,
                size = 100,
                location = 0
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


        companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: LoginPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}