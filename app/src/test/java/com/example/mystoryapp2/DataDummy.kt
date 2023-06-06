package com.example.mystoryapp2

import com.example.mystoryapp2.data.model.ListStoryItem
import com.example.mystoryapp2.data.model.StoryListResponse

object DataDummy {

    fun generateDummyStories(): StoryListResponse {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20) {
            val story = ListStoryItem(
                photoUrl = "https://akcdn.detik.net.id/visual/2020/02/14/066810fd-b6a9-451d-a7ff-11876abf22e2_169.jpeg?w=650",
                createdAt = "2022-02-22T22:22:22Z",
                name = "Name $i",
                description = "Description $i",
                lon = i.toDouble() * 10,
                id = "id_$i",
                lat = i.toDouble() * 10
            )
            listStory.add(story)
        }

        return StoryListResponse(
            listStory = listStory,
            error = false,
            message = "Stories fetched successfully"
        )
    }
}