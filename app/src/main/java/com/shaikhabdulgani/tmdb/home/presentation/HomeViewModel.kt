package com.shaikhabdulgani.tmdb.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.core.presentation.util.PaginatedListState
import com.shaikhabdulgani.tmdb.core.presentation.util.Paginator
import com.shaikhabdulgani.tmdb.home.domain.model.Media
import com.shaikhabdulgani.tmdb.home.domain.repository.HomeRepository
import com.shaikhabdulgani.tmdb.home.presentation.util.HomeTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel (
    private val homeRepository: HomeRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    var trendingMovies by
    mutableStateOf<PaginatedListState<Media>>(PaginatedListState(page = 1))
        private set

    var upcomingMovies by
    mutableStateOf<PaginatedListState<Media>>(PaginatedListState(page = 1))
        private set

    var onTheAirSeries by
    mutableStateOf<PaginatedListState<Media>>(PaginatedListState(page = 1))
        private set

    var popularSeries by
    mutableStateOf<PaginatedListState<Media>>(PaginatedListState(page = 1))
        private set

    var currentActiveTab by mutableStateOf(HomeTab.MOVIES)

    var username by mutableStateOf("")
        private set

    fun fetchUsername() = viewModelScope.launch(Dispatchers.IO) {
        val user = authRepository.getLoggedInUser()
        username = user?.username ?: "NA"
    }

    private val trendingMoviePaginator = Paginator(
        initialPage = 1,
        onLoading = { trendingMovies = trendingMovies.copy(isLoading = it) },
        onRequest = {
            viewModelScope.async(Dispatchers.IO) {
                homeRepository.getTrendingMovies(it)
            }.await()
        },
        getNextPage = { _, _ -> trendingMovies.page + 1 },
        onError = { },
        onSuccess = { items, newPage ->
            trendingMovies = trendingMovies.copy(
                list = trendingMovies.list + items,
                page = newPage,
                endReached = items.isEmpty()
            )
        },
        onClear = {}
    )

    private val upcomingMoviePaginator = Paginator(
        initialPage = 1,
        onLoading = { upcomingMovies = upcomingMovies.copy(isLoading = it) },
        onRequest = {
            viewModelScope.async(Dispatchers.IO) {
                homeRepository.getUpcomingMovies(it)
            }.await()
        },
        getNextPage = { _, _ -> upcomingMovies.page + 1 },
        onError = { },
        onSuccess = { items, newPage ->
            upcomingMovies = upcomingMovies.copy(
                list = upcomingMovies.list + items,
                page = newPage,
                endReached = items.isEmpty()
            )
        },
        onClear = {}
    )

    private val onTheAirSeriesPaginator = Paginator(
        initialPage = 1,
        onLoading = { onTheAirSeries = onTheAirSeries.copy(isLoading = it) },
        onRequest = {
            viewModelScope.async(Dispatchers.IO) {
                homeRepository.getOnTheAirSeries(it)
            }.await()
        },
        getNextPage = { _, _ -> onTheAirSeries.page + 1 },
        onError = { },
        onSuccess = { items, newPage ->
            onTheAirSeries = onTheAirSeries.copy(
                list = onTheAirSeries.list + items,
                page = newPage,
                endReached = items.isEmpty()
            )
        },
        onClear = {}
    )

    private val popularSeriesPaginator = Paginator(
        initialPage = 1,
        onLoading = { popularSeries = popularSeries.copy(isLoading = it) },
        onRequest = {
            viewModelScope.async(Dispatchers.IO) {
                homeRepository.getPopularSeries(it)
            }.await()
        },
        getNextPage = { _, _ -> popularSeries.page + 1 },
        onError = { },
        onSuccess = { items, newPage ->
            popularSeries = popularSeries.copy(
                list = popularSeries.list + items,
                page = newPage,
                endReached = items.isEmpty()
            )
        },
        onClear = {}
    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.TabClicked -> onTabClicked(event.selectedTab)
        }
    }

    private fun onTabClicked(selectedTab: HomeTab) {
        currentActiveTab = selectedTab
    }

    fun loadTrendingMovies() = viewModelScope.launch(Dispatchers.IO) {
        if (!trendingMovies.endReached && !trendingMovies.isLoading) {
            trendingMoviePaginator.loadNext()
        }
    }

    fun loadUpcomingMovies() = viewModelScope.launch(Dispatchers.IO) {
        if (!upcomingMovies.endReached && !upcomingMovies.isLoading) {
            upcomingMoviePaginator.loadNext()
        }
    }


    fun loadPopularSeries() = viewModelScope.launch(Dispatchers.IO) {
        if (!popularSeries.endReached && !popularSeries.isLoading) {
            popularSeriesPaginator.loadNext()
        }
    }
    fun loadOnTheAirSeries() = viewModelScope.launch(Dispatchers.IO) {
        if (!onTheAirSeries.endReached && !onTheAirSeries.isLoading) {
            onTheAirSeriesPaginator.loadNext()
        }
    }
}


