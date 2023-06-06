package com.example.mystoryapp2.data.model

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)
