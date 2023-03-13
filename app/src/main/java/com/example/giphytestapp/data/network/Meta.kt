package com.example.giphytestapp.data.network


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("response_id")
    val responseId: String,
    @SerializedName("status")
    val status: Int
)