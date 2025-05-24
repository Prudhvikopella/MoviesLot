package com.prudhvilearning.movieslot.constants

import androidx.compose.runtime.remember

class AppConstants {
    companion object {
        val CATEGORY_LIST    = listOf(
                ApiEndpoint.MoviePopular,
                ApiEndpoint.MovieNowPlaying,
                ApiEndpoint.MovieTopRated,
                ApiEndpoint.MovieUpcoming,
                ApiEndpoint.MovieTrending
            )
    }
}