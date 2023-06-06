package com.example.mystoryapp2.view.utils

import android.content.Context
import com.example.mystoryapp2.data.api.ApiConfig
import com.example.mystoryapp2.data.paging.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val pref = SessionPref(context)
        return StoryRepository(apiService, pref)
    }
}