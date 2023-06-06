package com.example.mystoryapp2.data.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp2.data.api.ApiService
import com.example.mystoryapp2.data.model.ListStoryItem
import com.example.mystoryapp2.view.utils.SessionPref

class StoryRepository(private val apiService: ApiService, private val pref: SessionPref) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref)
            }
        ).liveData
    }
}