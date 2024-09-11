package com.shaikhabdulgani.tmdb.home.presentation

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.shaikhabdulgani.tmdb.R
import com.shaikhabdulgani.tmdb.global.Screen
import com.shaikhabdulgani.tmdb.ui.theme.DarkBg
import com.shaikhabdulgani.tmdb.ui.theme.GradientStart
import com.shaikhabdulgani.tmdb.ui.theme.TMDBTheme
import com.shaikhabdulgani.tmdb.ui.theme.Transparent
import com.shaikhabdulgani.tmdb.core.presentation.MediaRow
import com.shaikhabdulgani.tmdb.core.presentation.dummy.DummyAuthRepo
import com.shaikhabdulgani.tmdb.core.presentation.dummy.DummyHomeRepo
import com.shaikhabdulgani.tmdb.core.presentation.util.toContentDetail
import com.shaikhabdulgani.tmdb.core.presentation.util.noRippleClickable
import com.shaikhabdulgani.tmdb.core.presentation.util.toProfile
import com.shaikhabdulgani.tmdb.home.presentation.components.HomeTopTitle
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

    LaunchedEffect(true) {
        viewModel.loadUpcomingMovies()
        viewModel.loadTrendingMovies()
        viewModel.loadPopularSeries()
        viewModel.loadOnTheAirSeries()
        viewModel.fetchUsername()
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
                ),
            username = viewModel.username,
            onProfileClick = controller::toProfile
        )

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
                MediaRow(
                    title = stringResource(R.string.trending),
                    movies = viewModel.trendingMovies.list,
                    onClick = controller::toContentDetail,
                    onLastReached = viewModel::loadTrendingMovies
                )

                MediaRow(
                    title = stringResource(R.string.upcoming),
                    movies = viewModel.upcomingMovies.list,
                    onClick = controller::toContentDetail,
                    onLastReached = viewModel::loadUpcomingMovies
                )
            }

            else -> {
                MediaRow(
                    title = stringResource(R.string.trending),
                    movies = viewModel.popularSeries.list,
                    onClick = controller::toContentDetail,
                    onLastReached = viewModel::loadPopularSeries
                )

                MediaRow(
                    title = stringResource(R.string.on_the_air),
                    movies = viewModel.onTheAirSeries.list,
                    onClick = controller::toContentDetail,
                    onLastReached = viewModel::loadOnTheAirSeries
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
            viewModel = HomeViewModel(
                DummyHomeRepo,
                DummyAuthRepo,
            )
        )
    }
}