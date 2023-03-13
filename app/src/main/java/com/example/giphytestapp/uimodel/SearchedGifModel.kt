package com.example.giphytestapp.uimodel

import com.example.giphytestapp.data.network.Data

data class SearchedGifModel(
    var data: List<Data>,
) {
    companion object {
        fun create(searchedGifModel: SearchedGifModel) = SearchedGifModel(
            data = searchedGifModel.data,
        )
    }
}