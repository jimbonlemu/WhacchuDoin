package com.jimbonlemu.whacchudoin.data.network.repository

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jimbonlemu.whacchudoin.data.local.story.StoryDatabase
import com.jimbonlemu.whacchudoin.data.local.story.StoryRemoteMediator
import com.jimbonlemu.whacchudoin.data.network.dto.AddStoryResponse
import com.jimbonlemu.whacchudoin.data.network.dto.Story
import com.jimbonlemu.whacchudoin.data.network.dto.StoryResponse
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.data.network.services.StoryService
import com.jimbonlemu.whacchudoin.utils.extension.sizeReducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryRepository(private val storyService: StoryService, private val database: StoryDatabase) {
    fun getAllStories(pageSize:Int): Flow<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
            ),
            remoteMediator = StoryRemoteMediator(database, storyService),
            pagingSourceFactory = { database.storyDao().getAllStories()}
        ).flow
    }

    fun getAllStoriesWithLocation(): Flow<ResponseState<StoryResponse>> = flow {
        try {
            emit(ResponseState.Loading)
            val response = storyService.getAllStoriesWithLocation()
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

    fun detailStory(id: String): LiveData<Story> {
        return database.storyDao().getDetailStory(id)
    }
}
