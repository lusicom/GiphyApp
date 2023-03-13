package com.example.giphytestapp.data.network


import com.google.gson.annotations.SerializedName

data class Onclick(
    @SerializedName("url")
    val url: String
)