package com.jimbonlemu.whacchudoin.data.network.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
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

@Entity(tableName = "tb_stories")
data class Story(
    @PrimaryKey
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