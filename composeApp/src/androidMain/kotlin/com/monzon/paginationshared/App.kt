package com.monzon.paginationshared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.monzon.paginationshared.data.Movie
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        PopularsMoviesList()
    }
}

@Composable
fun PopularsMoviesList(viewModel: PopularsViewModel = koinViewModel()) {
    val lazyPagingItems = viewModel.pagerFlow.collectAsLazyPagingItems()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Popular Movies") }) }
    ) { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues)) {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { MovieItem(movie = it) }
            }
            when (val state = lazyPagingItems.loadState.refresh) {
                is LoadState.Error -> {
                    item {
                        Button(onClick = {
                            lazyPagingItems.retry()
                        }) {
                            Text("Retry")
                        }
                    }
                }

                is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = "Loading"
                            )
                            CircularProgressIndicator()
                        }
                    }
                }

                else -> {}
            }
        }
    }
}


@Composable
fun MovieItem(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(Modifier.padding(16.dp)) {
            AsyncImage(
                model = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = movie.overview, style = MaterialTheme.typography.body2)
        }
    }
}