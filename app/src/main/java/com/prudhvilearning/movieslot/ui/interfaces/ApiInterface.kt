package com.prudhvilearning.movieslot.ui.interfaces


import com.prudhvilearning.movieslot.constants.ApiEndpoint
import com.prudhvilearning.movieslot.ui.data.GenresDataModel
import com.prudhvilearning.movieslot.ui.data.ImagesData
import com.prudhvilearning.movieslot.ui.data.KeywordResponse
import com.prudhvilearning.movieslot.ui.data.MovieCast
import com.prudhvilearning.movieslot.ui.data.MovieDetails
import com.prudhvilearning.movieslot.ui.data.PopularMoviesResponse
import com.prudhvilearning.movieslot.ui.data.ReviewResponse
import com.prudhvilearning.movieslot.ui.data.SimilarMoviesDetails
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    @GET
    suspend fun hitGetCategorizedMovies(
        @Url url: String ,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<SimilarMoviesDetails>

    @GET
    suspend fun hitGetAllGenres(
        @Url url: String ,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<GenresDataModel>

    @GET
    suspend fun hitGetMovieDetails(
        @Url url: String ,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<MovieDetails>

    @GET
    suspend fun hitGetReviews(
        @Url url: String ,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<ReviewResponse>

    @GET
    suspend fun hitGetKeywords(
        @Url url: String ,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<KeywordResponse>

    @GET
    suspend fun hitGetCredits(
        @Url url: String ,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<MovieCast>

    @GET
    suspend fun hitGetSimilarMovies(
        @Url url: String ,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<SimilarMoviesDetails>

    @GET
    suspend fun hitGetImages(
        @Url url: String ,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<ImagesData>

    @GET
    suspend fun hitGetDiscoverMovies(
        @Url url: String ,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871"
    ): retrofit2.Response<SimilarMoviesDetails>

    @GET
    suspend fun hitGetGenreMovies(
        @Url url: String ,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871",
        @Query("with_genres") genreId : Int = 0
    ): retrofit2.Response<SimilarMoviesDetails>

    @GET
    suspend fun hitGetKeywordMovies(
        @Url url: String ,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey : String = "1d9b898a212ea52e283351e521e17871",
        @Query("query") keyword : String = ""
    ): retrofit2.Response<SimilarMoviesDetails>




}