package com.prudhvilearning.movieslot.ui.data


data class PopularMoviesResponse(
    val page: Int,
    val results: List<Movie>,
)

data class Movie(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class CategoryDataModel(
    val categoryName: String,
    val movies: List<Movie>
)

data class ListedMoviesDataModel(
    val id : Int,
    val movies: List<MovieDetails>
)

data class GenresDataModel(
    val genres  : List<Genre>
)

data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection: Any?, // You can replace this with a dedicated data class if needed
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val imdb_id: String?,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Long,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
)

data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)

data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)

data class ReviewResponse(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val total_pages: Int,
    val total_results: Int
)

data class Review(
    val author: String,
    val author_details: AuthorDetails,
    val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String
)

data class AuthorDetails(
    val name: String?,
    val username: String,
    val avatar_path: String?,
    val rating: Double?
)

data class KeywordResponse(
    val id: Int,
    val keywords: List<Keyword>
)

data class Keyword(
    val id: Int,
    val name: String
)


data class CastMember(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val order: Int,
    val department : String
)

data class MovieCast(
    val id: Int,
    val cast: List<CastMember>,
    val crew: List<CastMember>
)

data class SimilarMoviesDetails(
        val page : Int,
        val results : List<Movie>,
        val total_pages : Int,
        val total_results: Int
        )

data class backDrops(
    val aspect_ratio : Float,
    val height : Int,
    val iso_639_1 : String,
    val file_path : String,
    val vote_average : Float,
    val vote_count : Int,
    val width : Int
)

data class ImagesData(
    val backdrops : List<backDrops>,
    val id : Int,
    val logos : List<backDrops>,
    val posters : List<backDrops>
)


