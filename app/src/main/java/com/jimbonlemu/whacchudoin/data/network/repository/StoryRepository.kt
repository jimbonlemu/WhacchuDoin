package com.jimbonlemu.whacchudoin.data.network.repository

import android.net.Uri
import androidx.core.net.toFile
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jimbonlemu.whacchudoin.data.network.dto.AddStoryResponse
import com.jimbonlemu.whacchudoin.data.network.dto.DetailStoryResponse
import com.jimbonlemu.whacchudoin.data.network.dto.Story
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.data.network.services.StoryService
import com.jimbonlemu.whacchudoin.utils.StoryPagingSource
import com.jimbonlemu.whacchudoin.utils.extension.sizeReducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryRepository(private val storyService: StoryService) {
    fun getAllStories(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(storyService) }
        ).flow
    }

    fun addStory(
        imageUri: Uri,
        description: String,
        lat: Double,
        lon: Double,
    ): Flow<ResponseState<AddStoryResponse>> =
        flow {
            try {
                emit(ResponseState.Loading)
                val photo = imageUri.toFile().sizeReducer()
                val requestImageFile = photo.asRequestBody("image/*".toMediaType())

                val response = storyService.addNewStory(
                    MultipartBody.Part.createFormData("photo", photo.name, requestImageFile),
                    description.toRequestBody("text/plain".toMediaType()),
                    lat.toFloat(),
                    lon.toFloat()
                )

                if (response.error) {
                    emit(ResponseState.Error(response.message))
                } else {
                    emit(ResponseState.Success(response))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ResponseState.Error(e.message.toString()))
            }
        }

    fun getDetailStory(id: String): Flow<ResponseState<DetailStoryResponse>> = flow {
        try {
            emit(ResponseState.Loading)
            val response = storyService.getDetailStory(id)
            if (response.error) {
                emit(ResponseState.Error(response.message))
            } else {
                emit(ResponseState.Success(response))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResponseState.Error(e.message.toString()))
        }
    }
}
