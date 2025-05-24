package com.prudhvilearning.movieslot.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.prudhvilearning.movieslot.utils.Apputils.getFiarWeatherBold
import com.prudhvilearning.movieslot.viewModel.MovieViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import coil.compose.rememberAsyncImagePainter
import com.prudhvilearning.movieslot.R
import com.prudhvilearning.movieslot.ui.data.CastMember
import com.prudhvilearning.movieslot.ui.data.ImagesData
import com.prudhvilearning.movieslot.ui.data.KeywordResponse
import com.prudhvilearning.movieslot.ui.data.Movie
import com.prudhvilearning.movieslot.ui.data.MovieCast
import com.prudhvilearning.movieslot.ui.data.MovieDetails
import com.prudhvilearning.movieslot.ui.data.ReviewResponse
import com.prudhvilearning.movieslot.ui.data.SimilarMoviesDetails
import com.prudhvilearning.movieslot.utils.DataType
import com.prudhvilearning.movieslot.utils.SelectionState
import com.prudhvilearning.movieslot.utils.SharedPrefsHelper
import com.prudhvilearning.movieslot.utils.Status
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnimatedCircularProgressIndicator(
    targetProgress: Float, // The target progress value
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 3.dp,
    gapSize: Dp = 4.dp,
    animationDuration: Int = 3000 // Duration of the animation in milliseconds
) {
    // Animate the progress value
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = animationDuration),
        label = "progressAnimation" // A label for debugging/tooling
    )

    // Determine the color based on the animated progress
    val progressColor = remember(animatedProgress) {
        when {
            animatedProgress < 0.3f -> Color(0xFFEF5350) // Red
            animatedProgress < 0.7f -> Color(0xFFFFCA28) // Amber
            else -> Color(0xFF66BB6A)           // Green
        }
    }

    CircularProgressIndicator(
        progress = { animatedProgress }, // Use the animated progress
        modifier = modifier,
        color = progressColor,
        strokeWidth = strokeWidth,
        gapSize = gapSize
    )
}

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBackClick: () -> Unit,
    viewModel: MovieViewModel = koinViewModel(),
    onSeeAllClick: (String, String, Int) -> Unit
) {
    val movieDetailResponse by viewModel.movieDetails.collectAsState()
    val reviewsResponse by viewModel.reviews.collectAsState()
    val keywordsResponse by viewModel.keywords.collectAsState()
    val creditsResponse by viewModel.credits.collectAsState()
    val similarMoviesResponse by viewModel.similarMovies.collectAsState()
    val imagesResponse by viewModel.imagesData.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
        viewModel.fetchReviews(movieId)
        viewModel.fetchKeywords(movieId)
        viewModel.fetchCredits(movieId)
        viewModel.fetchSimilarMovies(movieId)
        viewModel.fetchImages(movieId)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            MovieDetailToolBar(onBackClick = onBackClick, innerPadding)

            when {
                movieDetailResponse.status == Status.LOADING ||
                        reviewsResponse.status == Status.LOADING ||
                             keywordsResponse.status == Status.LOADING ||
                        creditsResponse.status == Status.LOADING ||
                        similarMoviesResponse.status == Status.LOADING ||
                        imagesResponse.status == Status.LOADING -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                movieDetailResponse.status == Status.SUCCESS &&
                        reviewsResponse.status == Status.SUCCESS &&
                        keywordsResponse.status == Status.SUCCESS &&
                        creditsResponse.status == Status.SUCCESS &&
                        similarMoviesResponse.status == Status.SUCCESS &&
                        imagesResponse.status == Status.SUCCESS-> {
                    movieDetailResponse.data?.let { movie ->
                        LazyColumn(modifier = Modifier.padding(bottom = 56.dp)) {
                            item {
                                SectionHeading(
                                    text = movie.title,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth(),
                                    textSize = 50
                                )
                            }
                            item{
                                MovieDetailMainCard(movie = movie, reviews = reviewsResponse.data , onSeeAllClick)

                            }
                            item{
                                MovieDetailSecondCard(keywordsResponse.data , creditsResponse.data , similarMoviesResponse.data , imagesResponse.data)
                            }
                        }

                    }
                }

                movieDetailResponse.status == Status.ERROR ||
                        reviewsResponse.status == Status.ERROR ||
                        keywordsResponse.status == Status.ERROR ||
                        creditsResponse.status == Status.ERROR ||
                        similarMoviesResponse.status == Status.ERROR ||
                        imagesResponse.status == Status.ERROR-> {
                    Text(
                        text = "Failed to load movie details.",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun MovieDetailToolBar(
    onBackClick: () -> Unit,
    innerPadding: PaddingValues
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovieDetailMainCard(
    movie: MovieDetails,
    reviews: ReviewResponse?,
    onSeeAllClick: (String, String, Int) -> Unit,
) {
    val sharedPrefsHelper = SharedPrefsHelper(LocalContext.current)

    // Initialize selectedState based on sharedPrefsHelper contents
    var selectedState by remember {
        mutableStateOf(
            when {
                sharedPrefsHelper.isInWishlist(movie.id) -> SelectionState.WISHLIST
                sharedPrefsHelper.isInSeenlist(movie.id) -> SelectionState.SEENLIST
                else -> SelectionState.NONE
            }
        )
    }

    var isExpanded by remember { mutableStateOf(false) }

    // Helper to update sharedPrefsHelper based on selectedState change
    fun updatePrefs(newState: SelectionState) {
        when (newState) {
            SelectionState.WISHLIST -> {
                sharedPrefsHelper.addWishlistId(movie.id)
                sharedPrefsHelper.removeSeenlistId(movie.id)
            }
            SelectionState.SEENLIST -> {
                sharedPrefsHelper.addSeenlistId(movie.id)
                sharedPrefsHelper.removeWishlistId(movie.id)
            }
            SelectionState.NONE -> {
                sharedPrefsHelper.removeWishlistId(movie.id)
                sharedPrefsHelper.removeSeenlistId(movie.id)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp))
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column {
            MovieDetailWithBlurBackground(movie , onSeeAllClick)

            Row {
                WishlistButton(
                    movieId = movie.id,
                    isSelected = selectedState == SelectionState.WISHLIST,
                    onClick = {
                        val newState = if (selectedState == SelectionState.WISHLIST) SelectionState.NONE else SelectionState.WISHLIST
                        selectedState = newState
                        updatePrefs(newState)
                    },
                    sharedPrefsHelper = sharedPrefsHelper
                )
                SeenlistButton(
                    movieId = movie.id,
                    isSelected = selectedState == SelectionState.SEENLIST,
                    onClick = {
                        val newState = if (selectedState == SelectionState.SEENLIST) SelectionState.NONE else SelectionState.SEENLIST
                        selectedState = newState
                        updatePrefs(newState)
                    },
                    sharedPrefsHelper = sharedPrefsHelper
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )

            reviews?.let {
                Row(modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)) {
                    Text(text = "${it.results.size} reviews", color = Color.Black, modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                        contentDescription = null,
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }

            Text(
                text = "Overview:",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = getFiarWeatherBold(),
                    color = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            AnimatedContent(
                targetState = isExpanded,
                transitionSpec = { fadeIn() with fadeOut() }
            ) { expanded ->
                Text(
                    text = movie.overview ?: "No overview available.",
                    maxLines = if (expanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray),
                    modifier = Modifier.padding(top = 4.dp).padding(horizontal = 16.dp)
                )
            }

            // Read more / less toggle
            if (!movie.overview.isNullOrBlank()) {
                Text(
                    text = if (isExpanded) "Read less" else "Read more",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(top = 4.dp).padding(horizontal = 16.dp)
                        .clickable { isExpanded = !isExpanded }
                )
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WishlistButton(
    movieId: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    sharedPrefsHelper: SharedPrefsHelper = SharedPrefsHelper(LocalContext.current)
) {
    val animatedContainerColor by animateColorAsState(
        targetValue = if (isSelected) Color.Red.copy(alpha = 0.1f) else Color.Transparent,
        label = "ContainerColor"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (isSelected) Color.Red else Color.Black,
        label = "ContentColor"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color.Red else Color.Gray,
        label = "BorderColor"
    )

    // Remove these lines - side effects shouldn't be here
    // if (isSelected) sharedPrefsHelper.addWishlistId(movieId) else sharedPrefsHelper.removeWishlistId(movieId)

    TextButton(
        onClick = { onClick(movieId) },
        colors = ButtonDefaults.textButtonColors(
            containerColor = animatedContainerColor,
            contentColor = animatedContentColor
        ),
        border = BorderStroke(1.dp, animatedBorderColor),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.padding(8.dp)
    ) {
        val icon = if (isSelected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder

        Crossfade(targetState = icon, label = "IconTransition") { targetIcon ->
            Icon(imageVector = targetIcon, contentDescription = null)
        }

        Spacer(modifier = Modifier.width(6.dp))

        AnimatedContent(
            targetState = isSelected,
            transitionSpec = { fadeIn() with fadeOut() },
            label = "TextChange"
        ) { selected ->
            Text(text = if (selected) "Wishlisted" else "Wishlist")
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SeenlistButton(
    movieId: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    sharedPrefsHelper: SharedPrefsHelper
) {
    val animatedContainerColor by animateColorAsState(
        targetValue = if (isSelected) Color.Green.copy(alpha = 0.1f) else Color.Transparent,
        label = "ContainerColor"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (isSelected) Color.Green else Color.Black,
        label = "ContentColor"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color.Green else Color.Gray,
        label = "BorderColor"
    )


    TextButton(
        onClick =  { onClick(movieId) },
        colors = ButtonDefaults.textButtonColors(
            containerColor = animatedContainerColor,
            contentColor = animatedContentColor
        ),
        border = BorderStroke(1.dp, animatedBorderColor),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.padding(8.dp)
    ) {
        val icon = if (isSelected) painterResource(R.drawable.ic_seen) else painterResource(R.drawable.ic_not_seen)

        Crossfade(targetState = icon, label = "IconTransition") { targetIcon ->
            Icon(painter = targetIcon, contentDescription = null)
        }

        Spacer(modifier = Modifier.width(6.dp))

        AnimatedContent(
            targetState = isSelected,
            transitionSpec = { fadeIn() with fadeOut() },
            label = "TextChange"
        ) { seen ->
            Text(text = if (seen) "Seen" else "Mark seen")
        }
    }
}

@Composable
fun MovieDetailWithBlurBackground(
    movie: MovieDetails?,
    onSeeAllClick: (String, String, Int) -> Unit
) {
    val releasedDate = movie!!.release_date
    val year  = releasedDate.split("-")[0]
    val formattedNumberOfRatings = NumberFormat.getNumberInstance(Locale.US).format(movie.vote_count)
    val percentage = (movie.vote_average * 10).toInt()
    val progress = percentage / 100f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        // Blurred background image
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie?.backdrop_path}",
            contentDescription = null,
            modifier = Modifier
                .height(310.dp)
                .blur(20.dp),  // Adjust blur radius here
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay to darken the background for better text visibility
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Foreground content
        Column{
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie?.poster_path}",
                    contentDescription = movie?.title,
                    modifier = Modifier
                        .height(230.dp)
                        .clip(RoundedCornerShape(5.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Row {
                        Text(text = year, color = Color.White)
                        Text(text = " • ${movie.runtime} minutes", color = Color.White)
                        Text(text = " • ${movie.status}", color = Color.White)
                    }
                    Text(text = movie.production_countries.firstOrNull()?.name ?: "Unknown", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "$formattedNumberOfRatings ratings", color = Color.White)
                    }
                }
            }

            LazyRow (horizontalArrangement = Arrangement.spacedBy(8.dp) , modifier = Modifier.fillMaxWidth() , contentPadding = PaddingValues(horizontal = 16.dp)){
                items(movie.genres.size){index ->
                    Row (modifier = Modifier.clickable{
                        onSeeAllClick(movie.genres[index].name,DataType.GENRES.name , movie.genres[index].id)
                    }.clip(RoundedCornerShape(10.dp)).background(Color.White).padding(vertical = 5.dp , horizontal = 10.dp), horizontalArrangement = Arrangement.Center){
                        Text(text = movie.genres[index].name , color = Color.Black ,modifier = Modifier.fillMaxWidth())
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailSecondCard(
    keywords: KeywordResponse?,
    credits: MovieCast?,
    similarMovies: SimilarMoviesDetails?,
    imagesData: ImagesData?
) {
    Card (modifier = Modifier.fillMaxWidth().padding(16.dp)){
       Column {
           Text(
               text = "KeyWords", style = MaterialTheme.typography.titleLarge.copy(
                   fontFamily = getFiarWeatherBold(),
                   color = Color.Black
               ),
               modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
           )

           LazyRow (
               contentPadding = PaddingValues(horizontal = 16.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp)

           ){
               items(keywords!!.keywords.size){index ->
                   Row (modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.White).padding(vertical = 5.dp , horizontal = 10.dp), horizontalArrangement = Arrangement.Center){
                       Text(text = keywords.keywords[index].name , color = Color.Black ,modifier = Modifier.fillMaxWidth())
                       Icon(
                           imageVector = Icons.Default.KeyboardArrowRight,
                           modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                           contentDescription = null,
                       )
                   }
               }
           }

           Divider(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(horizontal = 16.dp, vertical = 8.dp),
               color = Color.LightGray,
               thickness = 1.dp
           )

           Text(
               text = "Cast", style = MaterialTheme.typography.titleLarge.copy(
                   fontFamily = getFiarWeatherBold(),
                   color = Color.Black
               ),
               modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)

           )

           LazyRow (
               contentPadding = PaddingValues(horizontal = 16.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp)
           ){
               items(credits!!.cast.size){index ->
                   MovieCastCard(credits.cast[index])
               }
           }

           Row (modifier = Modifier.padding(vertical = 8.dp , horizontal = 16.dp)){
               Text(
                   text = "Director: ${credits?.crew?.find { it.department == "Director" }?.original_name ?: "Unknown"}",
                   color = Color.Black,
                   modifier = Modifier.weight(1f)
               )
               Icon(
                   imageVector = Icons.Default.KeyboardArrowRight,
                   modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                   contentDescription = null,
               )
           }

           Text(
               text = "crew", style = MaterialTheme.typography.titleLarge.copy(
                   fontFamily = getFiarWeatherBold(),
                   color = Color.Black
               ),
               modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
           )

           LazyRow (contentPadding = PaddingValues(horizontal = 16.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp)){
               items(credits!!.crew.size){index ->
                   MovieCastCard(credits.crew[index])
               }
           }

           Text(
               text = "similar movies", style = MaterialTheme.typography.titleLarge.copy(
                   fontFamily = getFiarWeatherBold(),
                   color = Color.Black
               ),
               modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
           )

           LazyRow (contentPadding = PaddingValues(horizontal = 16.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp)){
               items(similarMovies!!.results.size){index ->
                   SimilarMovieCard(similarMovies.results[index])
               }
           }

           Text(
               text = "other posters", style = MaterialTheme.typography.titleLarge.copy(
                   fontFamily = getFiarWeatherBold(),
                   color = Color.Black
               ),
               modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
           )

           LazyRow (
               contentPadding = PaddingValues(horizontal = 16.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp)
           ){
               items(imagesData!!.posters.size){index ->
                  AsyncImage(
                      model = "https://image.tmdb.org/t/p/w500${imagesData.posters[index].file_path}",
                      contentDescription = null
                  )
               }
           }

           Text(
               text = "Images", style = MaterialTheme.typography.titleLarge.copy(
                   fontFamily = getFiarWeatherBold(),
                   color = Color.Black
               ),
               modifier = Modifier.padding(horizontal = 16.dp , vertical = 8.dp)
           )

           LazyRow (contentPadding = PaddingValues(horizontal = 16.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               modifier = Modifier.padding(bottom = 16.dp)){
               items(imagesData!!.backdrops.size){index ->
                   AsyncImage(
                       model = "https://image.tmdb.org/t/p/w500${imagesData.backdrops[index].file_path}",
                       contentDescription = null
                   )
               }
           }

       }
    }
}

@Composable
fun MovieCastCard(cast: CastMember , modifier : Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(150.dp)
            .height(250.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Log.e("CheckingPath", "MovieCard: ${cast}")
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://image.tmdb.org/t/p/w500"+cast.profile_path,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground), // your placeholder drawable
                    error = painterResource(R.drawable.ic_launcher_foreground)        // your error drawable
                ),
                contentDescription = cast.original_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            Text(text = cast.original_name , color = Color.Black , modifier = Modifier.padding(top = 4.dp))
            Text(text = cast.character ?: "UnKnown" , color = Color.Black , modifier = Modifier.padding(1.dp))
            //Text(text = cast.character , color = Color.Black.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun SimilarMovieCard(movie: Movie , modifier : Modifier = Modifier) {
    val percentage = (movie.vote_average * 10).toInt()
    val progress = percentage / 100f
    Card(
        modifier = modifier
            .width(150.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Log.e("CheckingPath", "MovieCard: ${movie.poster_path}")
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://image.tmdb.org/t/p/w500"+movie.poster_path,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground), // your placeholder drawable
                        error = painterResource(R.drawable.ic_launcher_foreground)        // your error drawable
                    ),
                    contentDescription = movie.original_title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.4f)).fillMaxWidth() , contentAlignment = Alignment.Center){
                AnimatedCircularProgressIndicator(
                    targetProgress = progress,
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = "${String.format("%.1f", movie.vote_average)}/10",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

