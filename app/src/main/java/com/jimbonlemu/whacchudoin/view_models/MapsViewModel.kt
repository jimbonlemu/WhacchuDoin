package com.jimbonlemu.whacchudoin.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimbonlemu.whacchudoin.data.network.dto.StoryResponse
import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import kotlinx.coroutines.launch

class MapsViewModel (private val storyRepository: StoryRepository) : ViewModel() {

    private val _storiesResult = MutableLiveData<ResponseState<StoryResponse>>()
    val storyResult: LiveData<ResponseState<StoryResponse>> by lazy { _storiesResult }

    fun getAllStoriesWithLocation() {
        viewModelScope.launch {
            storyRepository.getAllStoriesWithLocation().collect {
                _storiesResult.value = it
            }
        }
    }
}