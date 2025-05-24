package com.prudhvilearning.movieslot.ui.screens

import android.R.attr.x
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.prudhvilearning.movieslot.R
import com.prudhvilearning.movieslot.ui.data.Movie
import com.prudhvilearning.movieslot.ui.data.MovieDetails
import com.prudhvilearning.movieslot.ui.theme.TabSelectedColor
import com.prudhvilearning.movieslot.utils.Apputils.getFiarWeatherBold
import com.prudhvilearning.movieslot.utils.SharedPrefsHelper
import com.prudhvilearning.movieslot.utils.SortType
import com.prudhvilearning.movieslot.viewModel.MovieViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyListScreen(viewModel: MovieViewModel = koinViewModel()) {
    val showBottomSheet = remember { mutableStateOf(false) }
    var selectedSortType by remember { mutableStateOf(SortType.RELEASE_DATE) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding(), bottom = 56.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Filter",
                    tint = TabSelectedColor,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(30.dp)
                        .clickable {
                            showBottomSheet.value = true
                        }
                )
            }

            Text(
                text = "MyLists",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = getFiarWeatherBold(),
                    fontSize = 50.sp,
                    color = TabSelectedColor
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )

            WishlistSeenlistTabs(viewModel = viewModel, sortType = selectedSortType)
        }

        if (showBottomSheet.value) {
            FilterBottomSheet(onDismiss = { showBottomSheet.value = false }) { sortType ->
                selectedSortType = sortType
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onSortSelected: (SortType) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "Sort Movies By",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val sortOptions = listOf(
                "Release Date" to SortType.RELEASE_DATE,
                "Rating" to SortType.RATING,
                "Popularity" to SortType.POPULARITY
            )

            sortOptions.forEach { (label, type) ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSortSelected(type)
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        }
                        .padding(vertical = 12.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Cancel",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Red,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            sheetState.hide()
                            onDismiss()
                        }
                    }
                    .padding(vertical = 12.dp)
            )
        }
    }
}


@Preview
@Composable
fun PreviewMyListScreen() {
    MyListScreen()
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WishlistSeenlistTabs(viewModel: MovieViewModel , sortType: SortType) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Wishlist", "Seenlist")
    val context = LocalContext.current
    val sharedPrefsHelper = remember { SharedPrefsHelper(context) }

    val listedMoviesState by viewModel.listedMovies.collectAsState()
    val wishlist = remember { sharedPrefsHelper.getWishlistIds() }
    val seenlist = remember { sharedPrefsHelper.getSeenlistIds() }
    val numberOfItemsInList = if(selectedTabIndex == 0) wishlist.size else seenlist.size
    val currentIds = if (selectedTabIndex == 0) wishlist else seenlist

    // Fetch movie data on tab change
    LaunchedEffect(selectedTabIndex) {
        viewModel.fetchListOfMovies(currentIds)
    }

    val currentMovies = currentIds.mapNotNull { id ->
        listedMoviesState[id]?.data?.movies?.firstOrNull()
    }.sortedWith(
        when (sortType) {
            SortType.RELEASE_DATE -> compareByDescending { it.release_date }
            SortType.RATING -> compareByDescending { it.vote_average }
            SortType.POPULARITY -> compareByDescending { it.popularity }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        // Tabs
        Row{
            Spacer(modifier = Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        val animatedColor by animateColorAsState(
                            if (isSelected) Color.White else Color.LightGray,
                            animationSpec = tween(durationMillis = 300)
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(animatedColor)
                                .clickable { selectedTabIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = Color.Black,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row{
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.weight(1f).background(Color.LightGray).height(50.dp).padding(horizontal = 4.dp) , contentAlignment = Alignment.BottomStart){
                Text(text = "${numberOfItemsInList} MOVIES IN LIST" , style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ) , modifier = Modifier.padding(start = 16.dp , bottom = 4.dp))
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the correct list
//        val currentIds = if (selectedTabIndex == 0) wishlist else seenlist
//        val currentMovies = currentIds.mapNotNull { id ->
//            listedMoviesState[id]?.data?.movies?.firstOrNull() // Adjust if multiple per id
//        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(currentMovies.size) { movie ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()


                ) {
                    MovieCardInList(currentMovies[movie])
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieCardInList(movie: MovieDetails) {
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




