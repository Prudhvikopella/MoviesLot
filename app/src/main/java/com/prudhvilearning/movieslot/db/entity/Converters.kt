package com.prudhvilearning.movieslot.db.entity

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromMovieDetails(movie: MovieDetails): String = gson.toJson(movie)

    @TypeConverter
    fun toMovieDetails(json: String): MovieDetails =
        gson.fromJson(json, MovieDetails::class.java)

    @TypeConverter
    fun fromType(type: MovieListType): String = type.name

    @TypeConverter
    fun toType(value: String): MovieListType = MovieListType.valueOf(value)
}
