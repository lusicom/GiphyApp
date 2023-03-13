package com.example.giphytestapp.uimodel

import com.example.giphytestapp.data.network.Data

data class GifModel(
    var id: String?,
    var url: String?
) {
    companion object {
        fun create(data: Data) = GifModel(
            id = data.id,
            url = data.url
        )
    }
}
