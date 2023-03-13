package com.example.giphytestapp.paginator

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}