package com.jimbonlemu.whacchudoin.data.network.dto

import com.google.gson.annotations.SerializedName
import com.jimbonlemu.whacchudoin.data.network.response.ApiResponse

data class StoryResponse(
    @field:SerializedName("listStory")
    val listStory: List<Story> = emptyList(),
): ApiResponse()

class AddStoryResponse: ApiResponse()

data class DetailStoryResponse(
    @field:SerializedName("story")
    val story: Story,
): ApiResponse()

data class Story(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Double = 0.0,

    @field:SerializedName("lat")
    val lat: Double = 0.0
)