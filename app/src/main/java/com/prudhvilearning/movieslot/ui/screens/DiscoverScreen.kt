package com.prudhvilearning.movieslot.ui.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.prudhvilearning.movieslot.ui.data.Movie
import com.prudhvilearning.movieslot.viewModel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.prudhvilearning.movieslot.R
import com.prudhvilearning.movieslot.ui.theme.SoftIOSGreen
import com.prudhvilearning.movieslot.utils.SharedPrefsHelper

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Error: $message", color = Color.Red, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun DiscoverScreen(viewModel: MovieViewModel = koinViewModel()) {
    val moviesPaging = viewModel.movies.collectAsLazyPagingItems()
    val removedMovieIds = remember { mutableStateListOf<Int>() }

    val movies = moviesPaging.itemSnapshotList.items
        .filterNotNull()
        .filterNot { removedMovieIds.contains(it.id) }

    Log.e("PagingDebug", "movies: ${movies}")


    LaunchedEffect(moviesPaging.loadState) {
        snapshotFlow { moviesPaging.loadState }.collectLatest { loadState ->
            if (loadState.refresh is LoadState.Error) {
                val error = (loadState.refresh as LoadState.Error).error
                Log.e("DiscoverScreen", "Refresh error: ${error.message}", error)
            }
            if (loadState.append is LoadState.Error) {
                val error = (loadState.append as LoadState.Error).error
                Log.e("DiscoverScreen", "Append error: ${error.message}", error)
                moviesPaging.retry()
            }
        }
    }

    when {
        moviesPaging.loadState.refresh is LoadState.Error -> {
            val refreshError = moviesPaging.loadState.refresh as LoadState.Error
            ErrorView(
                message = refreshError.error.message ?: "Failed to load movies. Please try again.",
                onRetry = { moviesPaging.retry() }
            )
        }
        moviesPaging.loadState.refresh is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            MovieStackWithBackgroundBlur(
                movies = movies,
                onSwiped = {
                    removedMovieIds.add(it.id)
                    val remainingCount = moviesPaging.itemSnapshotList.items.count { it.id !in removedMovieIds }

                    // Trigger next page if low on items
                    if (remainingCount <= 3 &&
                        moviesPaging.loadState.append !is LoadState.Loading &&
                        moviesPaging.loadState.append !is LoadState.Error
                    ) {
                        // No need to call refresh() — Paging will auto-trigger if more data is available
                        // You can force prefetching by accessing next index:
                        moviesPaging[removedMovieIds.size + 1]
                    }
                           },
                onEndReached = {
                    //moviesPaging.refresh()
                },
                onRetry = { moviesPaging.refresh() },
                loadState = moviesPaging.loadState
            )

            if (moviesPaging.loadState.append is LoadState.Loading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovieStackWithBackgroundBlur(
    movies: List<Movie>,
    onSwiped: (Movie) -> Unit,
    onEndReached: () -> Unit,
    loadState: CombinedLoadStates,
    onRetry: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val isDragging = remember { mutableStateOf(false) }
    val draggingDirection = remember { mutableStateOf<String?>(null) } // "left", "right", or null
    val draggingAway = remember { mutableStateOf(false) }
    val draggingBack = remember { mutableStateOf(false) }
    val sharedPrefsHelper = SharedPrefsHelper(LocalContext.current) // or requireContext()
    val movieList = remember { mutableStateListOf<Movie>() }
    // Initialize movieList when movies input changes
    LaunchedEffect(movies) {
        movieList.clear()
        movieList.addAll(movies)
    }

    // Remember last swiped movie in state
    val lastItemSwiped = remember { mutableStateOf<Movie?>(null) }

    fun removeMovie(movie: Movie) {
        lastItemSwiped.value = movie
        movieList.remove(movie)
    }

    fun revertPreviousMovie() {
        lastItemSwiped.value?.let { movie ->
            // Add last swiped movie back on top
            if (!movieList.contains(movie)) {
                movieList.add(movie)
                // Remove from seen or wishlist to keep prefs consistent
                if(sharedPrefsHelper.isInSeenlist(movie.id)) {
                    sharedPrefsHelper.removeSeenlistId(movie.id)
                } else if(sharedPrefsHelper.isInWishlist(movie.id)) {
                    sharedPrefsHelper.removeWishlistId(movie.id)
                }
            }
            lastItemSwiped.value = null
        }
    }

    // Use movieList internally instead of movies parameter
    val topMovie = movieList.lastOrNull()
    val backgroundUrl = topMovie?.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" }

    // Call onEndReached when only 3 movies left
    LaunchedEffect(movieList.size) {
        if (movieList.size == 3) onEndReached()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        backgroundUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().blur(25.dp).background(Color.Black.copy(alpha = 0.5f))
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .border(BorderStroke(2.dp, SoftIOSGreen), shape = RoundedCornerShape(10.dp))
                    .padding(5.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = null,
                        tint = SoftIOSGreen,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Text(
                        text = "Random",
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = SoftIOSGreen,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                val stack = movieList.takeLast(5)

                // Offset states, gestures etc...
                val topMovieInStack = stack.lastOrNull()
                val offsetX = remember { Animatable(0f) }
                val offsetY = remember { Animatable(0f) }

                stack.forEachIndexed { index, movie ->
                    val isTop = movie == topMovieInStack

                    val positionFromTop = stack.size - 1 - index
                    val scale = 1f - (positionFromTop * 0.05f)
                    val verticalOffset = 20.dp * positionFromTop

                    key(movie.id) {
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    translationY = verticalOffset.toPx()
                                    if (isTop) {
                                        translationX = offsetX.value
                                        translationY += offsetY.value
                                        rotationZ = (offsetX.value / 30).coerceIn(-30f, 30f)
                                    }
                                }
                                .zIndex(index.toFloat())
                                .size(260.dp, 390.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Gray.copy(alpha = 0.2f))
                                .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                                .then(
                                    if (isTop) Modifier.pointerInput(movie.id) {
                                        detectDragGestures(
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                isDragging.value = true
                                                coroutineScope.launch {
                                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                                    offsetY.snapTo(offsetY.value + dragAmount.y)
                                                    draggingDirection.value = when {
                                                        offsetX.value > 0 -> "right"
                                                        offsetX.value < 0 -> "left"
                                                        else -> null
                                                    }

                                                    draggingAway.value = offsetX.value > 150f || offsetX.value < -150f
                                                    draggingBack.value = false
                                                }
                                            },
                                            onDragEnd = {
                                                coroutineScope.launch {
                                                    when {
                                                        offsetX.value > 150f -> {
                                                            offsetX.animateTo(1000f)
                                                            // Update prefs & last swiped
                                                            if(sharedPrefsHelper.isInWishlist(movie.id)) sharedPrefsHelper.removeWishlistId(movie.id)
                                                            sharedPrefsHelper.addSeenlistId(movie.id)
                                                            lastItemSwiped.value = movie
                                                            movieList.remove(movie)
                                                            onSwiped(movie)
                                                            offsetX.snapTo(0f)
                                                            offsetY.snapTo(0f)
                                                            draggingAway.value = false // Make icons visible again
                                                        }
                                                        offsetX.value < -150f -> {
                                                            offsetX.animateTo(-1000f)
                                                            if(sharedPrefsHelper.isInSeenlist(movie.id)) sharedPrefsHelper.removeSeenlistId(movie.id)
                                                            sharedPrefsHelper.addWishlistId(movie.id)
                                                            lastItemSwiped.value = movie
                                                            movieList.remove(movie)
                                                            onSwiped(movie)
                                                            offsetX.snapTo(0f)
                                                            offsetY.snapTo(0f)
                                                            draggingAway.value = false
                                                        }
                                                        else -> {
                                                            offsetX.animateTo(0f)
                                                            offsetY.animateTo(0f)
                                                        }
                                                    }
                                                }
                                                isDragging.value = false
                                                draggingAway.value = false
                                                isDragging.value = false
                                                draggingDirection.value = null
                                            }
                                        )
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !draggingAway.value,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Revert */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_refresh),
                                contentDescription = "Revert",
                                tint = SoftIOSGreen,
                                modifier = Modifier.clickable{
                                    revertPreviousMovie()
                                }
                            )
                        }
                        IconButton(onClick = {
                            val movieToRemove = movieList.lastOrNull()
                            movieToRemove?.let { removeMovie(it)}
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Remove",
                                tint = Color.Red,
                            )
                        }
                        IconButton(onClick = {
                            onRetry()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_rearrange),
                                contentDescription = "Rearrange",
                                tint = SoftIOSGreen
                            )
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = draggingAway.value,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    val seenIconAlpha by animateFloatAsState(
                        targetValue = if (draggingDirection.value == "left") 1f else 0.3f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val wishlistIconAlpha by animateFloatAsState(
                        targetValue = if (draggingDirection.value == "right") 1f else 0.3f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Action 1 */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_favorite_24),
                                contentDescription = "Icon 1",
                                tint = Color.Red.copy(alpha = seenIconAlpha)
                            )
                        }
                        IconButton(onClick = { /* Action 2 */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_seen),
                                contentDescription = "Icon 2",
                                tint = SoftIOSGreen.copy(alpha = wishlistIconAlpha)
                            )
                        }
                    }
                }
            }

                // Your other buttons, animated visibility etc...
            }
        }
    }
