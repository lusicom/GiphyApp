package com.example.giphytestapp.data.network


import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("count")
    val count: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("total_count")
    val totalCount: Int
)