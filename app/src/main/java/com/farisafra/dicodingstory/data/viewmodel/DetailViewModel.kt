package com.farisafra.dicodingstory.data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val story = MutableLiveData<Story>()

    fun getStoryDetail(): LiveData<Story> {
        return story
    }
}