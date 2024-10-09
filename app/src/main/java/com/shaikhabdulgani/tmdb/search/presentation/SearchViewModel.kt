package com.shaikhabdulgani.tmdb.search.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.presentation.util.Paginator
import com.shaikhabdulgani.tmdb.search.domain.model.SearchResult
import com.shaikhabdulgani.tmdb.search.domain.model.SearchType
import com.shaikhabdulgani.tmdb.search.domain.repository.SearchRepository
import com.shaikhabdulgani.tmdb.search.presentation.util.SearchFilter
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _query: MutableStateFlow<QueryState> =
        MutableStateFlow(QueryState())
    val query: StateFlow<QueryState> = _query

    private var resultState: SearchState<SearchResult> = SearchState()

    var isLoading by mutableStateOf(false)
        private set

    val result: StateFlow<SearchState<SearchResult>> = _query
        .onEach {
            Log.d("SearchViewModel::result::onEach","setting loading to true")
            isLoading = true
        }
        .debounce(1000)
        .map {
            if (it.query.isEmpty()){
                return@map SearchState()
            }
            val currentState = resultState
            when(it.event){
                QueryEvent.Page -> {
                    when(val res = paginatedResult.getNext()){
                        is Result.Failure -> {
                            Log.d("SearchViewModel::result",res.error ?: "cannot get data")
                        }
                        is Result.Success -> {
                            resultState = currentState.copy(
                                list = currentState.list + res.data!!,
                                endReached = res.data.isEmpty(),
                            )
                        }
                    }
                }
                QueryEvent.Query -> {
                    paginatedResult.reset()
                    when(val res = paginatedResult.getNext()){
                        is Result.Failure -> {
                            Log.d("SearchViewModel::result",res.error ?: "cannot get data")
                        }
                        is Result.Success -> {
                            resultState = currentState.copy(
                                list = res.data!!,
                                endReached = res.data.isEmpty(),
                            )
                        }
                    }
                }
            }
            resultState
        }
        .onEach {
            Log.d("SearchViewModel::result::onEach","setting loading to false")
            isLoading = false
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SearchState()
        )

    private val paginatedResult: Paginator<SearchResult> = Paginator(
        initialPage = 1,
        onLoading = {},
        onRequest = {
            search(queryState = query.value, it)
        },
        onClear = {
            resultState = SearchState()
        }
    )

    private suspend fun search(
        queryState: QueryState,
        page: Int,
    ): Result<List<SearchResult>> {
        val result = viewModelScope.async {
            val mediaType = when (queryState.filter) {
                SearchFilter.ALL -> SearchType.ALL
                SearchFilter.MOVIE -> SearchType.MOVIE
                SearchFilter.SERIES -> SearchType.SERIES
                SearchFilter.PERSON -> SearchType.PERSON
            }
            searchRepository.search(queryState.query, page, mediaType)
        }.await()
        return result
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChange -> onQueryChange(event.query)
            is SearchEvent.FilerChange -> onFilerChange(event.searchFilter)
            is SearchEvent.LoadMore -> onLoadMore()
        }
    }

    private fun onLoadMore() {
        if (!resultState.endReached) {
            _query.value = _query.value.copy(
                event = QueryEvent.Page,
                flip = !_query.value.flip
            )
        }else{
            Log.d("onLoadMore","End reached")
        }
    }

    private fun onQueryChange(query: String) {
        _query.value = _query.value.copy(query = query, event = QueryEvent.Query)
    }

    private fun onFilerChange(filter: SearchFilter) {
        _query.value = _query.value.copy(filter = filter, event = QueryEvent.Query)
    }
}

