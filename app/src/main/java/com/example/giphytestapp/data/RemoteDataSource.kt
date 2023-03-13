package com.example.giphytestapp.data

import com.example.giphytestapp.data.network.SearchedGif
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val giphyApi: GiphyApi
) {

    suspend fun searchGifs(
        query: String,
        page: Int,
        limit: Int
    ): SearchedGif {
        return giphyApi.searchGifs(query, page, limit)
    }
}