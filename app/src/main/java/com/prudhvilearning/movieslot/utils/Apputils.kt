package com.prudhvilearning.movieslot.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.prudhvilearning.movieslot.R
import com.prudhvilearning.movieslot.constants.ApiEndpoint

object Apputils {

    fun getCategory(endPoint : String): String {
        val categories = listOf(
            "Popular",
            "Now Playing",
            "Top Rated",
            "Upcoming",
            "Trending"
        )

        return when(endPoint){
            ApiEndpoint.MoviePopular.path() -> categories[0]
            ApiEndpoint.MovieNowPlaying.path() -> categories[1]
            ApiEndpoint.MovieTopRated.path() -> categories[2]
            ApiEndpoint.MovieUpcoming.path() -> categories[3]
            ApiEndpoint.MovieTrending.path() -> categories[4]
            else -> endPoint
        }
    }

    fun getCategoryForSafeNavigation(endPoint : String): String {
        val categories = listOf(
            "Popular",
            "Now_Playing",
            "Top_Rated",
            "Upcoming",
            "Trending"
        )

        return when(endPoint){
            ApiEndpoint.MoviePopular.path() -> categories[0]
            ApiEndpoint.MovieNowPlaying.path() -> categories[1]
            ApiEndpoint.MovieTopRated.path() -> categories[2]
            ApiEndpoint.MovieUpcoming.path() -> categories[3]
            ApiEndpoint.MovieTrending.path() -> categories[4]
            else -> endPoint
        }
    }

    fun convertToPath(value: String): String {
        val categories = listOf(
            "Popular",
            "Now Playing",
            "Top Rated",
            "Upcoming",
            "Trending"
        )
        return when(value){
            categories[0] -> ApiEndpoint.MoviePopular.path()
            categories[1] ->  ApiEndpoint.MovieNowPlaying.path()
            categories[2] -> ApiEndpoint.MovieTopRated.path()
            categories[3] -> ApiEndpoint.MovieUpcoming.path()
            categories[4] -> ApiEndpoint.MovieTrending.path()
            else -> ""
        }
    }



    fun getFiarWeatherBold(): FontFamily {
        return FontFamily(Font(R.font.fairweather_bold, FontWeight.Normal))

    }
}