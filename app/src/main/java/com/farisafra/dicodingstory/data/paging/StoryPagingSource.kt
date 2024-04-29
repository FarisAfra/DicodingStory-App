package com.farisafra.dicodingstory.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.response.story.StoryResponse
import com.farisafra.dicodingstory.data.retrofit.ApiService

class StoryPagingSource(
    private val apiService: ApiService,
    private val pref: LoginPreference
) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        val token = pref.getLogin().token

        if (token.isNotEmpty()) {
            val responseData = token.let { apiService.getStories("Bearer $it", page, params.loadSize, 0) }
            if (responseData.isSuccessful) {
                return LoadResult.Page(
                    data = responseData.body()?.listStory ?: emptyList(),
                    prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                    nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else page + 1
                )
            } else {
                return LoadResult.Error(Exception("Failed to load story"))
            }
        } else {
            return LoadResult.Error(Exception("Token empty"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}