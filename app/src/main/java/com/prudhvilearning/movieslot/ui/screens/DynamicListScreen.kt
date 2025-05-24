package com.prudhvilearning.movieslot.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.prudhvilearning.movieslot.R
import com.prudhvilearning.movieslot.ui.data.Movie
import com.prudhvilearning.movieslot.ui.data.MovieDetails
import com.prudhvilearning.movieslot.ui.theme.TabSelectedColor
import com.prudhvilearning.movieslot.utils.Apputils
import com.prudhvilearning.movieslot.utils.Apputils.getFiarWeatherBold
import com.prudhvilearning.movieslot.utils.Status
import com.prudhvilearning.movieslot.viewModel.MovieViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.prudhvilearning.movieslot.utils.DataType

@Composable
fun DynamicListScreen(
    heading: String,
    type: String,
    genreId: Int = 0,
    onBackClick: () -> Unit,
    onFilterClick: () -> Unit,
    viewModel: MovieViewModel = koinViewModel()
) {
    // Get PagingData Flow from ViewModel functions, no extra fetch calls
    val moviesPagingItems = if (type == DataType.GENRES.name) {
        viewModel.genreMovies(genreId).collectAsLazyPagingItems()
    } else if (type == DataType.CATEGORY.name){
        viewModel.categoryMovies(Apputils.convertToPath(heading)).collectAsLazyPagingItems()
    } else {
        viewModel.keywordMovies(heading).collectAsLazyPagingItems()

    }

    val isShowFilterMenu = remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                }

                Text(
                    text = "Movies",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = getFiarWeatherBold(),
                        fontSize = 25.sp
                    ),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    softWrap = false
                )

                IconButton(onClick = onFilterClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = TabSelectedColor,
                        modifier = Modifier.clickable(
                            onClick = {
                                isShowFilterMenu.value = !isShowFilterMenu.value
                            }
                        )
                    )
                }
            }



            Text(
                text = if(type == DataType.GENRES.name) heading else if(type == DataType.CATEGORY.name) Apputils.getCategory(heading) else heading,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = getFiarWeatherBold(),
                    fontSize = 40.sp
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Paging LazyColumn with LoadState handling
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
            ) {
                // Handle initial loading state
                when (val refreshState = moviesPagingItems.loadState.refresh) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is LoadState.Error -> {
                        item {
                            PagingErrorView(
                                message = refreshState.error.localizedMessage ?: "Unknown error",
                                onRetry = { moviesPagingItems.retry() }
                            )
                        }
                    }
                    else -> {
                        // Show items if available
                        if (moviesPagingItems.itemCount == 0) {
                            item {
                                Text(
                                    text = "No movies found",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            items(moviesPagingItems.itemCount) { index ->
                                moviesPagingItems[index]?.let { movie ->
                                    MovieCardInList(movie)
                                }
                            }

                            // Show loading spinner when appending next page
                            if (moviesPagingItems.loadState.append is LoadState.Loading) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            // Show error during append with retry option
                            if (moviesPagingItems.loadState.append is LoadState.Error) {
                                val error = moviesPagingItems.loadState.append as LoadState.Error
                                item {
                                    PagingErrorView(
                                        message = error.error.localizedMessage ?: "Unknown error",
                                        onRetry = { moviesPagingItems.retry() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if(isShowFilterMenu.value){
            FilterBottomSheet(onDismiss = {}) {

            }
        }
    }
}

@Composable
fun PagingErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.material3.Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}


@Preview
@Composable
fun ListScreenPreview(){
   // DynamicListScreen(heading = "Movies", onBackClick = {})
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieCardInList(movie: Movie) {
    val percentage = (movie.vote_average * 10).toInt()
    val progress = percentage / 100f
    Card(
        modifier = Modifier
            .fillMaxWidth() ,// Adds spacing around card
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Light gray background (or use Transparent)
        ),
    ) {
        Column {
            Row() {
                // Movie Poster
                Card(elevation = CardDefaults.cardElevation(5.dp),colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent // Light gray background (or use Transparent)
                )) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                        contentDescription = movie.title,
                        modifier = Modifier
                            .height(200.dp)
                            .width(120.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop// Rounded image corners
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Movie Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(movie.title, style = MaterialTheme.typography.titleMedium.copy(color = TabSelectedColor , fontFamily = getFiarWeatherBold() , fontSize = 29.sp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically , modifier = Modifier.padding(top = 8.dp)){
                        Box(
                            contentAlignment = Alignment.Center, // Center everything inside the Box
                            modifier = Modifier.size(50.dp)
                        ) {
                            AnimatedCircularProgressIndicator(
                                targetProgress = progress,
                                modifier = Modifier.size(100.dp)
                            )

                            Text(
                                text = "${String.format("%.1f", movie.vote_average)}/10",
                                style = MaterialTheme.typography.bodySmall.copy(color = TabSelectedColor),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        val formattedDate = try {
                            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                            val date = LocalDate.parse(movie.release_date, inputFormatter)
                            date.format(outputFormatter)
                        } catch (e: Exception) {
                            movie.release_date // fallback in case of parsing error
                        }
                        Text(formattedDate,
                            style = MaterialTheme.typography.bodySmall.copy(color = Black , fontSize = 25.sp),
                            modifier = Modifier.padding(start = 8.dp)
                        )

                    }

                    Text(
                        text = movie.overview ?: "No overview available.",
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                            fontSize = 20.sp,
                            lineHeight = 32.sp // adjust this value as needed for spacing
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )


                }


            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp , vertical = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}

