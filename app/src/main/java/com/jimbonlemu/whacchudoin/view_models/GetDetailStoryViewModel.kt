package com.jimbonlemu.whacchudoin.view_models

import androidx.lifecycle.ViewModel
import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository

class GetDetailStoryViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun getDetailStory(id: String) = storyRepository.detailStory(id)
}