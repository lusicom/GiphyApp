package com.example.giphytestapp.paginator

import com.example.giphytestapp.data.network.Data

class GiphyPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Data>>,
    private inline val getNextKey: suspend (List<Data>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Data>, newKey: Key) -> Unit

): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.getOrElse{
            onError(it)
            onLoadUpdated(false)
            return
        }

        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}