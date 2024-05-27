package com.jimbonlemu.whacchudoin.data.local.story

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jimbonlemu.whacchudoin.data.network.dto.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<Story>)

    @Query("SELECT * FROM tb_stories ORDER BY createdAt DESC")
    fun getAllStories(): PagingSource<Int, Story>

    @Query("SELECT * FROM tb_stories WHERE id = :id")
    fun getDetailStory(id: String): LiveData<Story>

    @Query("DELETE FROM tb_stories")
    suspend fun deleteAll()
}