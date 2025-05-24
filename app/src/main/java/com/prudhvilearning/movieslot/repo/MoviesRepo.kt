package com.prudhvilearning.movieslot.repo

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.prudhvilearning.movieslot.constants.ApiEndpoint
import com.prudhvilearning.movieslot.ui.data.CategoryDataModel
import com.prudhvilearning.movieslot.ui.data.GenresDataModel
import com.prudhvilearning.movieslot.ui.data.ImagesData
import com.prudhvilearning.movieslot.ui.data.KeywordResponse
import com.prudhvilearning.movieslot.ui.data.ListedMoviesDataModel
import com.prudhvilearning.movieslot.ui.data.Movie
import com.prudhvilearning.movieslot.ui.data.MovieCast
import com.prudhvilearning.movieslot.ui.data.MovieDetails
import com.prudhvilearning.movieslot.ui.data.PopularMoviesResponse
import com.prudhvilearning.movieslot.ui.data.ReviewResponse
import com.prudhvilearning.movieslot.ui.data.SimilarMoviesDetails
import com.prudhvilearning.movieslot.ui.interfaces.ApiInterface
import com.prudhvilearning.movieslot.ui.paging.DiscoverMoviesPagingSource
import com.prudhvilearning.movieslot.ui.paging.GenreMoviesPagingSource
import com.prudhvilearning.movieslot.ui.paging.KeywordMoviesPagingSource
import com.prudhvilearning.movieslot.ui.paging.MovieDetailPaginSource
import com.prudhvilearning.movieslot.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRepo(private val api: ApiInterface){

    fun getCategorizedMovies(category: String): Flow<Resource<CategoryDataModel>> = flow {
        emit(Resource.loading(null))
        try {
            val response = api.hitGetCategorizedMovies(ApiEndpoint.BASE_URL + category)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(Resource.success(CategoryDataModel(category, body.results)))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun hitGetAllGenres(): Flow<Resource<GenresDataModel>> = flow {
        emit(Resource.loading(null))
        try {
            val response = api.hitGetAllGenres(ApiEndpoint.BASE_URL + ApiEndpoint.Genres.path())
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(Resource.success(GenresDataModel(body.genres)))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun getUserMovies(id: Int): Flow<Resource<ListedMoviesDataModel>> = flow {
        emit(Resource.loading(null))
        try {
            val response = api.hitGetMovieDetails(
                ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path()
            )
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(Resource.success(ListedMoviesDataModel(id, listOf(body))))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }


    fun hitGetMovieDetails(id : Int): Flow<Resource<MovieDetails>> = flow {
        Log.e("CheckingPath444", "AppNavigation: repo calling with $id")
        emit(Resource.loading(null))
        try {
            Log.e("CheckingPath555", "AppNavigation: ${ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path()}")
            val response = api.hitGetMovieDetails(ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path())
            val body = response.body()
            Log.e("CheckingPath444", "AppNavigation: body calling with $body")
            if (response.isSuccessful && body != null) {
                emit(Resource.success(body))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun hitGetReview(id : Int): Flow<Resource<ReviewResponse>> = flow {
        Log.e("CheckingPath444", "AppNavigation: repo calling with $id")
        emit(Resource.loading(null))
        try {
            Log.e("CheckingPath555", "AppNavigation: ${ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path()}")
            val response = api.hitGetReviews(ApiEndpoint.BASE_URL + ApiEndpoint.MovieReviews(id).path())
            val body = response.body()
            Log.e("CheckingPath444", "AppNavigation: body calling with $body")
            if (response.isSuccessful && body != null) {
                emit(Resource.success(body))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun hitGetKeywords(id : Int): Flow<Resource<KeywordResponse>> = flow {
        Log.e("CheckingPath444", "AppNavigation: repo calling with $id")
        emit(Resource.loading(null))
        try {
            Log.e("CheckingPath555", "AppNavigation: ${ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path()}")
            val response = api.hitGetKeywords(ApiEndpoint.BASE_URL + ApiEndpoint.MovieKeywords(id).path())
            val body = response.body()
            Log.e("CheckingPath444", "AppNavigation: body calling with $body")
            if (response.isSuccessful && body != null) {
                emit(Resource.success(body))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun hitGetCredits(id : Int): Flow<Resource<MovieCast>> = flow {
        Log.e("CheckingPath444", "AppNavigation: repo calling with $id")
        emit(Resource.loading(null))
        try {
            Log.e("CheckingPath555", "AppNavigation: ${ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path()}")
            val response = api.hitGetCredits(ApiEndpoint.BASE_URL + ApiEndpoint.MovieCredits(id).path())
            val body = response.body()
            Log.e("CheckingPath444", "AppNavigation: body calling with $body")
            if (response.isSuccessful && body != null) {
                emit(Resource.success(body))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun hitGetSimilarMovies(id : Int): Flow<Resource<SimilarMoviesDetails>> = flow {
        Log.e("CheckingPath444", "AppNavigation: repo calling with $id")
        emit(Resource.loading(null))
        try {
            Log.e("CheckingPath555", "AppNavigation: ${ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path()}")
            val response = api.hitGetSimilarMovies(ApiEndpoint.BASE_URL + ApiEndpoint.MovieSimilar(id).path())
            val body = response.body()
            Log.e("CheckingPath444", "AppNavigation: body calling with $body")
            if (response.isSuccessful && body != null) {
                emit(Resource.success(body))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun hitGetImages(id : Int): Flow<Resource<ImagesData>> = flow {
        Log.e("CheckingPath444", "AppNavigation: repo calling with $id")
        emit(Resource.loading(null))
        try {
            Log.e("CheckingPath555", "AppNavigation: ${ApiEndpoint.BASE_URL + ApiEndpoint.MovieDetail(id).path()}")
            val response = api.hitGetImages(ApiEndpoint.BASE_URL + ApiEndpoint.MovieImages(id).path())
            val body = response.body()
            Log.e("CheckingPath444", "AppNavigation: body calling with $body")
            if (response.isSuccessful && body != null) {
                emit(Resource.success(body))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                emit(Resource.error(Exception(error), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun getDiscoverMoviesPager(): Pager<Int, Movie> {
        return Pager(PagingConfig(pageSize = 20,
            initialLoadSize = 5, // Important: prevent it from trying to load 60 at once
            enablePlaceholders = false)) {
            DiscoverMoviesPagingSource(api)
        }
    }

    fun getGenreMovies(genreId: Int): Pager<Int, Movie> {
        return Pager(PagingConfig(pageSize = 20 , prefetchDistance = 10)) {
            GenreMoviesPagingSource( api , genreId)
        }
    }

    fun getMovieDetailsPager(category: String): Pager<Int, Movie> {
        return Pager(PagingConfig(pageSize = 20 , prefetchDistance = 10)) {
            MovieDetailPaginSource( api , category)
        }
    }

    fun getKeywordPagingSource(keyword: String): Pager<Int, Movie> {
        return Pager(PagingConfig(pageSize = 20 , prefetchDistance = 10)) {
            KeywordMoviesPagingSource( api , keyword)
        }
    }
}