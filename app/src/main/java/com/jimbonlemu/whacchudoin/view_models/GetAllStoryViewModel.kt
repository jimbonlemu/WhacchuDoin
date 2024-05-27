package com.jimbonlemu.whacchudoin.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jimbonlemu.whacchudoin.data.network.dto.Story
import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository
import kotlinx.coroutines.launch

class GetAllStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private var _storyResult = MutableLiveData<PagingData<Story>>()
    val storyResult: LiveData<PagingData<Story>> = _storyResult

    fun getAllStories(size: Int = 5) {
        viewModelScope.launch {
            storyRepository.getAllStories(size).cachedIn(viewModelScope).collect {
                _storyResult.value = it
            }
        }
    }
}