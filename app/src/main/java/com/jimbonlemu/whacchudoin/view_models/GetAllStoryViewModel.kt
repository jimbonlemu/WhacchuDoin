    package com.jimbonlemu.whacchudoin.view_models

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import androidx.paging.PagingData
    import androidx.paging.cachedIn
    import com.jimbonlemu.whacchudoin.data.network.dto.Story
    import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository
    import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
    import kotlinx.coroutines.launch

    class GetAllStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

        private val _stories = MutableLiveData<ResponseState<PagingData<Story>>>()
        val stories: LiveData<ResponseState<PagingData<Story>>> get() = _stories

        fun getAllStories() {
            viewModelScope.launch {
                _stories.value = ResponseState.Loading
                try {
                    storyRepository.getAllStories()
                        .cachedIn(viewModelScope)
                        .collect { data ->
                            _stories.value = ResponseState.Success(data)
                        }
                } catch (e: Exception) {
                    _stories.value = ResponseState.Error(e.message ?: "Unknown Error")
                }
            }
        }
    }