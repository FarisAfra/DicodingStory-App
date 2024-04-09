package com.farisafra.dicodingstory.data.viewmodel

import androidx.lifecycle.ViewModel
import com.farisafra.dicodingstory.data.repository.StoryRepository

class StoriesViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getStories()
}