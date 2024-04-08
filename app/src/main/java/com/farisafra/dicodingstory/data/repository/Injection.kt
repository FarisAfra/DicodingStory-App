package com.farisafra.dicodingstory.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.retrofit.ApiClient
import kotlinx.coroutines.runBlocking
import java.util.prefs.Preferences

object Injection {
//    fun provideRepository(context: Context): StoryRepository {
//        val preferences = LoginPreference(context)
//        val token = preferences.getToken() ?: ""
//        val apiService = ApiClient.getApiService(token)
//        return StoryRepository.getInstance(preferences, apiService)
//    }
}
