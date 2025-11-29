package com.example.jsonplaceholderapp.model

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    val title: String,
    val body: String
)