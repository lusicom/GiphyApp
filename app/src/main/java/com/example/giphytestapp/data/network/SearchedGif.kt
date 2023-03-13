package com.example.giphytestapp.data.network


import com.google.gson.annotations.SerializedName

data class SearchedGif(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("pagination")
    val pagination: Pagination
)