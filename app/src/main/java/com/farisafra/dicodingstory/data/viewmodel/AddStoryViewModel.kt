package com.farisafra.dicodingstory.data.viewmodel

import androidx.lifecycle.ViewModel
import com.farisafra.dicodingstory.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postStory(imageFile: MultipartBody.Part, desc: RequestBody, lat: Double, lon: Double) = storyRepository.addStory(imageFile, desc, lat, lon)
}