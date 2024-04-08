package com.farisafra.dicodingstory.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.response.login.LoginResponse
import com.farisafra.dicodingstory.data.retrofit.ApiService

class StoryRepository(private val preference: LoginPreference, private val apiService: ApiService) {

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