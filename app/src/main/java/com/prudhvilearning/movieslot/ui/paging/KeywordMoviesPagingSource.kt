package com.prudhvilearning.movieslot.ui.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prudhvilearning.movieslot.constants.ApiEndpoint
import com.prudhvilearning.movieslot.ui.data.Movie
import com.prudhvilearning.movieslot.ui.data.SimilarMoviesDetails
import com.prudhvilearning.movieslot.ui.interfaces.ApiInterface
import retrofit2.Response

class KeywordMoviesPagingSource(
    private val api: ApiInterface,
    private val keyword : String
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        Log.d("PagingDebug", "Attempting to load page: $page with loadSize: ${params.loadSize}")

        return try {
            // Make the API call, expecting a Response<MovieResponse>
            val moviesResponse: Response<SimilarMoviesDetails> = api.hitGetKeywordMovies(ApiEndpoint.BASE_URL + ApiEndpoint.Discover.path(), page , keyword = keyword)

            if (!moviesResponse.isSuccessful) {
                val errorBody = moviesResponse.errorBody()?.string()
                Log.e("PagingDebug", "API call failed for page $page. Code: ${moviesResponse.code()}, Error: $errorBody")
                return LoadResult.Error(Exception("API Error: ${moviesResponse.code()} - $errorBody"))
            }

            // Get the response body, which should contain results and total_pages
            val responseBody = moviesResponse.body()
            val movies = responseBody?.results ?: emptyList()
            // Assume totalPages is available in the MovieResponse body.
            // Default to 1 if it's null, but ideally your API always provides this.
            val totalPages = responseBody?.total_pages ?: 1

            Log.d("PagingDebug", "Loaded ${movies.size} movies for page $page. Total pages from API: $totalPages. First movie ID: ${movies.firstOrNull()?.id}, Last movie ID: ${movies.lastOrNull()?.id}")

            val prevKey = if (page == 1) null else page - 1
            // Calculate nextKey based on totalPages from the API response
            // Only increment if the current page is less than the total number of pages
            val nextKey = if (page < totalPages) page + 1 else null

            Log.d("PagingDebug", "Page $page: prevKey=$prevKey, nextKey=$nextKey")

            LoadResult.Page(
                data = movies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("PagingDebug", "Exception during load for page $page: ${e.message}", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val refreshKey = state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
        Log.d("PagingDebug", "getRefreshKey called, returning: $refreshKey")
        return refreshKey
    }
}
