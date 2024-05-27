package com.jimbonlemu.whacchudoin.views

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.jimbonlemu.whacchudoin.FakeData
import com.jimbonlemu.whacchudoin.MainDispatcherRule
import com.jimbonlemu.whacchudoin.PaginatingData
import com.jimbonlemu.whacchudoin.data.network.dto.Story
import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository
import com.jimbonlemu.whacchudoin.getOrAwaitValue
import com.jimbonlemu.whacchudoin.view_models.GetAllStoryViewModel
import com.jimbonlemu.whacchudoin.views.adapters.StoryPagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : KoinTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `When fetching stories, the data received should be non-null and valid`() =
        runTest {
            // Prepare
            val dummyStory = FakeData.autoGenerateFakeData()
            val data: PagingData<Story> = PaginatingData.snapshot(dummyStory)
            val expectedStory = MutableLiveData<PagingData<Story>>()
            Mockito.`when`(storyRepository.getAllStories(5)).thenReturn(expectedStory.asFlow())

            // Action
            val storyViewModel = GetAllStoryViewModel(storyRepository)
            storyViewModel.getAllStories()
            expectedStory.value = data

            // Assertion
            val actualStory: PagingData<Story> = storyViewModel.storyResult.getOrAwaitValue()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(actualStory)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dummyStory.size, differ.snapshot().size)
            Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
        }

    @Test
    fun `When fetching stories with an empty result, it should return no data`() = runTest {
        // Prepare
        val emptyData: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<Story>>()
        Mockito.`when`(storyRepository.getAllStories(5)).thenReturn(expectedStory.asFlow())

        // Action
        val storyViewModel = GetAllStoryViewModel(storyRepository)
        storyViewModel.getAllStories()
        expectedStory.value = emptyData

        // Assertion
        val actualStory: PagingData<Story> = storyViewModel.storyResult.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}