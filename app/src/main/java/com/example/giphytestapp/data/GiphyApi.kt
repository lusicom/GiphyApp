package com.example.giphytestapp.data

import com.example.giphytestapp.BuildConfig
import com.example.giphytestapp.data.network.SearchedGif
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("/v1/gifs/search?api_key=${BuildConfig.API_KEY}")
    suspend fun searchGifs(
        @Query("q") value: String?,
        @Query("offset") page : Int?,
        @Query("limit") limit: Int?
    ): SearchedGif
}