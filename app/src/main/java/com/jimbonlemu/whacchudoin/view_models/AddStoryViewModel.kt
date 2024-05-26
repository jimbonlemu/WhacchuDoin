package com.jimbonlemu.whacchudoin.view_models

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimbonlemu.whacchudoin.data.network.dto.AddStoryResponse
import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import kotlinx.coroutines.launch

class AddStoryViewModel (private val storyRepository: StoryRepository) : ViewModel(){
    private val _addStoryResult = MutableLiveData<ResponseState<AddStoryResponse>>()
    val addStoryResult: LiveData<ResponseState<AddStoryResponse>> by lazy { _addStoryResult }

    fun addStory(imageUri: Uri, description: String, lat: Double = 0.0, lon: Double = 0.0) {
        viewModelScope.launch {
            storyRepository.addStory(imageUri, description, lat, lon).collect {
                _addStoryResult.value = it
            }
        }
    }
}