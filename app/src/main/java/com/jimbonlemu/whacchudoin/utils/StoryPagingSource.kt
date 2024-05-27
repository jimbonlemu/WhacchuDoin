package com.jimbonlemu.whacchudoin.utils

import androidx.paging.PagingSource
import com.jimbonlemu.whacchudoin.data.network.dto.Story
import com.jimbonlemu.whacchudoin.data.network.services.StoryService
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class StoryPagingSource(
    private val storyService: StoryService,
) : PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val position = params.key ?: 1
        return try {
            val response = storyService.getAllStories(page = position, size = params.loadSize)
            val stories = response.listStory

            LoadResult.Page(
                data = stories,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}