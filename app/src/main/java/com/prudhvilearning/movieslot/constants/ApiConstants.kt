package com.prudhvilearning.movieslot.constants

sealed class ApiEndpoint {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    // Movie endpoints
    object MoviePopular : ApiEndpoint()
    object MovieTopRated : ApiEndpoint()
    object MovieUpcoming : ApiEndpoint()
    object MovieNowPlaying : ApiEndpoint()
    object MovieTrending : ApiEndpoint()

    data class MovieDetail(val movieId: Int) : ApiEndpoint()
    data class MovieVideos(val movieId: Int) : ApiEndpoint()
    data class MovieCredits(val movieId: Int) : ApiEndpoint()
    data class MovieReviews(val movieId: Int) : ApiEndpoint()
    data class MovieRecommended(val movieId: Int) : ApiEndpoint()
    data class MovieSimilar(val movieId: Int) : ApiEndpoint()
    data class MovieKeywords(val movieId: Int) : ApiEndpoint()
    data class MovieImages(val movieId: Int) : ApiEndpoint()

    // Person endpoints
    object PersonPopular : ApiEndpoint()
    data class PersonDetail(val personId: Int) : ApiEndpoint()
    data class PersonMovieCredits(val personId: Int) : ApiEndpoint()
    data class PersonImages(val personId: Int) : ApiEndpoint()

    // Search endpoints
    object SearchMovie : ApiEndpoint()
    object SearchKeyword : ApiEndpoint()
    object SearchPerson : ApiEndpoint()

    // Genres and Discover
    object Genres : ApiEndpoint()
    object Discover : ApiEndpoint()

    fun path(): String = when (this) {
        MoviePopular -> "movie/popular"
        MovieTopRated -> "movie/top_rated"
        MovieUpcoming -> "movie/upcoming"
        MovieNowPlaying -> "movie/now_playing"
        MovieTrending -> "trending/movie/day"

        is MovieDetail -> "movie/${movieId}"
        is MovieVideos -> "movie/${movieId}/videos"
        is MovieCredits -> "movie/${movieId}/credits"
        is MovieReviews -> "movie/${movieId}/reviews"
        is MovieRecommended -> "movie/${movieId}/recommendations"
        is MovieSimilar -> "movie/${movieId}/similar"
        is MovieKeywords -> "movie/${movieId}/keywords"
        is MovieImages -> "movie/${movieId}/images"

        PersonPopular -> "person/popular"
        is PersonDetail -> "person/${personId}"
        is PersonMovieCredits -> "person/${personId}/movie_credits"
        is PersonImages -> "person/${personId}/images"

        SearchMovie -> "search/movie"
        SearchKeyword -> "search/keyword"
        SearchPerson -> "search/person"

        Genres -> "genre/movie/list"
        Discover -> "discover/movie"
    }
}
