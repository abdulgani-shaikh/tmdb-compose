package com.shaikhabdulgani.tmdb.home.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.shaikhabdulgani.tmdb.R
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.global.Screen
import com.shaikhabdulgani.tmdb.ui.theme.DarkBg
import com.shaikhabdulgani.tmdb.ui.theme.GradientStart
import com.shaikhabdulgani.tmdb.ui.theme.TMDBTheme
import com.shaikhabdulgani.tmdb.ui.theme.Transparent
import com.shaikhabdulgani.tmdb.core.presentation.MovieRowWithTitle
import com.shaikhabdulgani.tmdb.core.presentation.util.noRippleClickable
import com.shaikhabdulgani.tmdb.home.domain.model.Movie
import com.shaikhabdulgani.tmdb.home.domain.repository.HomeRepository
import com.shaikhabdulgani.tmdb.home.presentation.components.HomeTopTitle
import com.shaikhabdulgani.tmdb.home.presentation.components.SearchBar
import com.shaikhabdulgani.tmdb.home.presentation.components.TabLayout
import com.shaikhabdulgani.tmdb.home.presentation.util.HomeTab
import com.shaikhabdulgani.tmdb.ui.theme.SearchBarBg
import com.shaikhabdulgani.tmdb.ui.theme.Shapes
import com.shaikhabdulgani.tmdb.ui.theme.White50
import com.shaikhabdulgani.tmdb.ui.theme.spacing

@Composable
fun HomeScreen(
    controller: NavHostController,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(context) {
        viewModel.loadUpcomingMovies()
        viewModel.loadTrendingMovies()
        viewModel.loadPopularSeries()
        viewModel.loadOnTheAirSeries()
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(color = DarkBg)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        GradientStart,
                        Transparent,
                        Transparent,
                    )
                )
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default)
    ) {
        HomeTopTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.default,
                    end = MaterialTheme.spacing.default,
                    top = MaterialTheme.spacing.extraLarge
                )
        )

//        SearchBar(
//            modifier = Modifier
//                .noRippleClickable {
//                    controller.navigate(Screen.Search)
//                }
//                .fillMaxWidth()
//                .padding(horizontal = MaterialTheme.spacing.default),
//            query = "",
//            onQueryChange = {  },
//            onSearch = {  },
//        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.default)
                .clip(Shapes.fullRoundedCorner)
                .noRippleClickable {
                    controller.navigate(Screen.Search)
                }
                .background(SearchBarBg)
                .padding(
                    horizontal = MaterialTheme.spacing.default,
                    vertical = MaterialTheme.spacing.medium
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.placeholder_search),
                color = White50,
            )
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(R.string.placeholder_search),
                tint = White50,
            )
        }


        TabLayout(
            modifier = Modifier.fillMaxWidth(),
            tabs = stringArrayResource(R.array.types).toList(),
            selectedItem = viewModel.currentActiveTab.ordinal,
            onSelect = {
                viewModel.onEvent(
                    HomeEvent.TabClicked(
                        HomeTab.fromOrdinal(it)
                    )
                )
            }
        )

        when (viewModel.currentActiveTab) {
            HomeTab.MOVIES -> {
                MovieRowWithTitle(
                    title = stringResource(R.string.trending),
                    movies = viewModel.trendingMovies.list,
                    onClick = { controller.navigate(Screen.MovieDetail(it)) },
                    onLastReached = {
                        Log.d("onLastReached","Last Reached")
                        viewModel.loadTrendingMovies()
                    }
                )

                MovieRowWithTitle(
                    title = stringResource(R.string.upcoming),
                    movies = viewModel.upcomingMovies.list,
                    onClick = { controller.navigate(Screen.MovieDetail(it)) },
                    onLastReached = {
                        viewModel.loadUpcomingMovies()
                    }
                )
            }

            else -> {
                MovieRowWithTitle(
                    title = stringResource(R.string.trending),
                    movies = viewModel.popularSeries.list,
                    onClick = { controller.navigate(Screen.MovieDetail(it)) },
                    onLastReached = {
                        viewModel.loadPopularSeries()
                    }
                )

                MovieRowWithTitle(
                    title = stringResource(R.string.on_the_air),
                    movies = viewModel.onTheAirSeries.list,
                    onClick = { controller.navigate(Screen.MovieDetail(it)) }
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(MaterialTheme.spacing.default)
                .fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    TMDBTheme {
        HomeScreen(
            NavHostController(LocalContext.current),
            viewModel = HomeViewModel(object : HomeRepository {
                override suspend fun getTrendingMovies(page: Int): Result<List<Movie>> {
                    return Result.failure()
                }

                override suspend fun getUpcomingMovies(page: Int): Result<List<Movie>> {
                    return Result.failure()
                }

                override suspend fun getPopularSeries(page: Int): Result<List<Movie>> {
                    return Result.failure()
                }

                override suspend fun getOnTheAirSeries(page: Int): Result<List<Movie>> {
                    return Result.failure()
                }
            })
        )
    }
}