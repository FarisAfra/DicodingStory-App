package com.farisafra.dicodingstory.data.repository

import android.content.Context
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.retrofit.ApiClient

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = LoginPreference(context)
        val token = preferences.getLogin().token
        val apiService = ApiClient.getApiService(token)
        return StoryRepository.getInstance(preferences, apiService)
    }
}
