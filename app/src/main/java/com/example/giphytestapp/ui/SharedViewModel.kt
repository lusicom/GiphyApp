package com.example.giphytestapp.ui

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphytestapp.data.Repository
import com.example.giphytestapp.data.network.Data
import com.example.giphytestapp.paginator.GiphyPaginator
import com.example.giphytestapp.ui.screens.master.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    var state by mutableStateOf(ScreenState())


    private val paginator = GiphyPaginator<Int, List<Data>>(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            repository.searchGifs(_searchTextState.value, nextPage, 10)
        },
        getNextKey = {
            state.page + 10
        },
        onError = {
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = { items, newKey ->
            state = state.copy(
                items = state.items + items,
                page = newKey,
                endReached = items.isEmpty()
            )
        }
    )

    init {
        loadNextItems()
    }

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    fun searchGifs(query: String) {
        if (searchTextState.value.isNotEmpty()) {
            viewModelScope.launch {
                val response = repository.searchGifs(
                    query = query,
                    page = 0,
                    limit = 10
                )

                state = if (response.isSuccess) {
                    state.copy(items = response.getOrDefault(emptyList()))
                } else {
                    state.copy(items = emptyList())
                }
            }
        }
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<Data> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    var page: Int = 0
)