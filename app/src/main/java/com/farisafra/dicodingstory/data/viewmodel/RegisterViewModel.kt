package com.farisafra.dicodingstory.data.viewmodel

import androidx.lifecycle.ViewModel
import com.farisafra.dicodingstory.data.repository.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postRegister(name: String, email: String, password: String) = storyRepository.register(name, email, password)
}