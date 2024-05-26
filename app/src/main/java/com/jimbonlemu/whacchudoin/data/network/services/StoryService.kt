package com.jimbonlemu.whacchudoin.data.network.services

import com.jimbonlemu.whacchudoin.data.network.dto.AddStoryResponse
import com.jimbonlemu.whacchudoin.data.network.dto.DetailStoryResponse
import com.jimbonlemu.whacchudoin.data.network.dto.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryService {

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 5,
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): AddStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse
}