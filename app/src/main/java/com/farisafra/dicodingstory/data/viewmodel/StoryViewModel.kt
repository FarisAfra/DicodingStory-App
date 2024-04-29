package com.farisafra.dicodingstory.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.farisafra.dicodingstory.data.repository.StoryRepository
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.response.story.StoryResponse

class StoriesViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getStories()
    val getstory: LiveData<PagingData<Story>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}