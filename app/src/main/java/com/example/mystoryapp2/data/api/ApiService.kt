package com.example.mystoryapp2.data.api

import com.example.mystoryapp2.data.model.AddStoryResponse
import com.example.mystoryapp2.data.model.LoginResponse
import com.example.mystoryapp2.data.model.RegisterResponse
import com.example.mystoryapp2.data.model.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("register")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    fun getStoryList(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): Call<StoryListResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : StoryListResponse

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>

    @GET("stories?location=1")
    fun getStoryListLocation(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): Call<StoryListResponse>
}