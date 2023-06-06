package com.example.mystoryapp2.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp2.MainActivity
import com.example.mystoryapp2.data.api.ApiService
import com.example.mystoryapp2.data.model.ListStoryItem
import com.example.mystoryapp2.view.utils.SessionPref

class StoryPagingSource(private val apiService: ApiService, private val pref: SessionPref): PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "bearer ${pref.getToken}"
            val responseData = apiService.getStories(token ,page, params.loadSize)


            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}

