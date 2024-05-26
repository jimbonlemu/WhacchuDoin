package com.jimbonlemu.whacchudoin.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimbonlemu.whacchudoin.data.network.dto.DetailStoryResponse
import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import kotlinx.coroutines.launch

class GetDetailStoryViewModel (private val storyRepository: StoryRepository) : ViewModel() {

    private val _detailStory = MutableLiveData<ResponseState<DetailStoryResponse>>()
    val detailStory: LiveData<ResponseState<DetailStoryResponse>> get() = _detailStory

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            _detailStory.value = ResponseState.Loading
            try {
                storyRepository.getDetailStory(id).collect { response ->
                    _detailStory.value = response
                }
            } catch (e: Exception) {
                _detailStory.value = ResponseState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}