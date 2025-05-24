package com.prudhvilearning.movieslot.db.entity

enum class MovieListType { WISHLIST, SEENLIST }

data class MovieDetails(
    val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: String,
    val voteAverage: Double
)
