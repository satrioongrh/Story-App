package com.example.mystoryapp2.data.model

import com.google.gson.annotations.SerializedName

data class StoryListResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)


