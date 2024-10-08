package com.shaikhabdulgani.tmdb.search.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeviceUnknown
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.shaikhabdulgani.tmdb.R
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.presentation.InputText
import com.shaikhabdulgani.tmdb.core.presentation.util.shimmer
import com.shaikhabdulgani.tmdb.core.presentation.util.toast
import com.shaikhabdulgani.tmdb.global.Constants
import com.shaikhabdulgani.tmdb.global.Screen
import com.shaikhabdulgani.tmdb.home.presentation.components.SearchBar
import com.shaikhabdulgani.tmdb.home.presentation.components.TabLayout
import com.shaikhabdulgani.tmdb.search.domain.model.ContentType
import com.shaikhabdulgani.tmdb.search.domain.model.SearchResult
import com.shaikhabdulgani.tmdb.search.domain.model.SearchType
import com.shaikhabdulgani.tmdb.search.domain.repository.SearchRepository
import com.shaikhabdulgani.tmdb.search.presentation.util.SearchFilter
import com.shaikhabdulgani.tmdb.ui.theme.Black
import com.shaikhabdulgani.tmdb.ui.theme.Black50
import com.shaikhabdulgani.tmdb.ui.theme.DarkBg
import com.shaikhabdulgani.tmdb.ui.theme.GradientStart
import com.shaikhabdulgani.tmdb.ui.theme.Transparent
import com.shaikhabdulgani.tmdb.ui.theme.White50
import com.shaikhabdulgani.tmdb.ui.theme.spacing

@Composable
fun SearchScreen(
    controller: NavController,
    viewModel: SearchViewModel
) {
    val query = viewModel.query.collectAsState()
    val result = viewModel.result.collectAsState()
    val context: Context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkBg)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, Transparent, Transparent)
                )
            )
            .padding(MaterialTheme.spacing.default),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = query.value.query
        ) {
            viewModel.onEvent(SearchEvent.QueryChange(it))
        }

        if (query.value.query.isNotBlank()){
            Text(
                text = "Showing Results for term \"${query.value.query}\"",
                color = White50
            )
        }

        TabLayout(
            modifier = Modifier.fillMaxWidth(),
            tabs = stringArrayResource(R.array.filters).toList(),
            selectedItem = query.value.filter.ordinal,
            onSelect = {
                viewModel.onEvent(SearchEvent.FilerChange(SearchFilter.entries[it]))
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            itemsIndexed(items = result.value.list, key = { _, item -> item.id }) { i, item ->
                if (i == result.value.list.size - 1 && !result.value.endReached) {
                    viewModel.onEvent(SearchEvent.LoadMore)
                }
                SearchItem(
                    searchResult = item,
                    parentContext = context
                ){
                    when(it.type){
                        ContentType.MOVIE,ContentType.SERIES -> {
                            controller.navigate(Screen.MovieDetail(it.id,item.type.getValue()))
                        }
                        ContentType.PERSON -> {
                            controller.navigate(Screen.MovieDetail(it.id,item.type.getValue()))
                        }
                        ContentType.UNKNOWN -> {
                            context.toast(context.getString(R.string.unknown_media_navigation_error))
                        }
                    }
                }
            }
            if (viewModel.isLoading) {
                val totalItems = ((3 - result.value.list.size % 3)) + 6
                Log.d("result","showing shimmer")
                items(totalItems) {
                    SearchItemShimmer()
                }
            }
        }
        if (query.value.query.isEmpty()){
            SearchSomething(
                message = stringResource(R.string.search_something_message)
            )
        }else if(result.value.list.isEmpty()){
            NoDataLayout(message = stringResource(R.string.search_no_data_message))
        }
    }
}

@Composable
fun NoDataLayout(modifier: Modifier = Modifier,message: String) {
    Column(
        modifier = Modifier
            .then(modifier)
            .padding(MaterialTheme.spacing.default),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default)
    ) {
        Image(
            modifier = Modifier
                .width(200.dp),
            painter = painterResource(R.drawable.img_no_data),
            contentDescription = "no data",
        )
        Text(
            text = message,
            color = White50,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchSomething(modifier: Modifier = Modifier, message: String) {
    Column(
        modifier = Modifier
            .then(modifier)
            .padding(MaterialTheme.spacing.default),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default)
    ) {
        Image(
            modifier = Modifier
                .width(200.dp),
            painter = painterResource(R.drawable.img_search_illus),
            contentDescription = "no data",
        )
        Text(
            text = message,
            color = White50,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun NoDatLayoutPrev() {
    NoDataLayout(message = stringResource(R.string.search_no_data_message))
}

@Composable
fun SearchItemShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .then(modifier)
            .height(180.dp)
            .clip(RoundedCornerShape(MaterialTheme.spacing.defaultSmall))
            .background(Color.Gray)
            .shimmer()
    )
}

@Composable
fun SearchItem(
    modifier: Modifier = Modifier,
    searchResult: SearchResult,
    parentContext: Context,
    onClick: (SearchResult)->Unit
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .height(180.dp)
            .clip(RoundedCornerShape(MaterialTheme.spacing.defaultSmall))
            .background(Color.Gray)
            .clickable {
                onClick(searchResult)
            },
        contentAlignment = Alignment.Center
    ) {
        if (searchResult.imageId.isBlank()) {
            Image(
                modifier =
                Modifier
                    .size(50.dp),
                imageVector = Icons.Outlined.ImageNotSupported,
                contentDescription = searchResult.title,
                contentScale = ContentScale.Crop,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Transparent,
                                Black50
                            )
                        )
                    )
                    .padding(10.dp)
                    .align(Alignment.BottomCenter)
                ,
                text = searchResult.title,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                color = Black
            )
        } else {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = "${Constants.IMAGE_BASE_URL}${searchResult.imageId}",
                contentDescription = searchResult.title,
                imageLoader = ImageLoader(parentContext),
                contentScale = ContentScale.Crop,
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Success) {
                    SubcomposeAsyncImageContent()
                    LaunchedEffect(Unit) { println(state.result.dataSource) }
                }
            }
        }

        val image = when(searchResult.type){
            ContentType.PERSON -> Icons.Outlined.PersonOutline
            ContentType.MOVIE -> Icons.Outlined.Movie
            ContentType.SERIES -> Icons.Outlined.Tv
            ContentType.UNKNOWN -> Icons.Outlined.DeviceUnknown
        }

        Image(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .size(MaterialTheme.spacing.default)
                .align(Alignment.TopEnd)
            ,
            imageVector = image,
            contentDescription = searchResult.type.getValue()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            TextField(
                value = "",
                onValueChange = {}
            )
        }
    )
}

@Preview
@Composable
private fun SearchScreenPrev() {
    SearchScreen(NavController(LocalContext.current),viewModel = SearchViewModel(
        object : SearchRepository{
            override suspend fun search(
                query: String,
                page: Int,
                searchType: SearchType
            ): Result<List<SearchResult>> {
                return Result.failure()
            }

        }))
//    val context = LocalContext.current
//    SearchItem(title = "Hello", imageId = "", type = MediaType.PERSON, parentContext = context)
}

