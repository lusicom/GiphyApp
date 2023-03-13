package com.example.giphytestapp.data

import com.example.giphytestapp.data.network.Data
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun searchGifs(
        query: String,
        page: Int,
        limit: Int
    ): Result<List<Data>> {
        return Result.success(
            remoteDataSource.searchGifs(query, page, limit).data
        )
    }
}