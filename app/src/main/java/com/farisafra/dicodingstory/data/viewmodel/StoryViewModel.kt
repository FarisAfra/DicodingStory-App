package com.farisafra.dicodingstory.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farisafra.dicodingstory.data.response.story.Story
import retrofit2.http.Query

class StoryViewModel: ViewModel() {
    val listStory = MutableLiveData<ArrayList<Story>>()

    fun setSearchUser(query: String){

    }
}