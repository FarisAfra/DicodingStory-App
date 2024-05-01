package com.farisafra.dicodingstory.data.viewmodel

import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.farisafra.dicodingstory.data.DataDummy
import com.farisafra.dicodingstory.data.MainDispatcherRule
import com.farisafra.dicodingstory.data.getOrAwaitValue
import com.farisafra.dicodingstory.data.repository.StoryRepository
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.ui.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository;
    private var dummyStory = DataDummy.dummyStory()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Get Story Success`() = runBlocking {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory.listStory)
        val expectedResponse = MutableLiveData<PagingData<Story>>()

        expectedResponse.value = data
        `when`(storyRepository.getStory()).thenReturn(expectedResponse)

        val storiesViewModel = StoriesViewModel(storyRepository)
        val actualStory: PagingData<Story> = storiesViewModel.getstory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.listStory, differ.snapshot())
        assertEquals(dummyStory.listStory.size, differ.snapshot().size)
        assertEquals(dummyStory.listStory[0].name, differ.snapshot()[0]?.name)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}