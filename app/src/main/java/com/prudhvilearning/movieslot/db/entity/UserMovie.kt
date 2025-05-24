package com.prudhvilearning.movieslot.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded

@Entity(tableName = "user_movies")
data class UserMovie(
    @PrimaryKey val movieId: Int,
    @Embedded val movie: MovieDetails,
    val type: MovieListType,
    val addedAt: Long // timestamp millis
)
