package com.prudhvilearning.movieslot.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.prudhvilearning.movieslot.repo.MoviesRepo
import com.prudhvilearning.movieslot.utils.Resource
import kotlinx.coroutines.launch
import com.prudhvilearning.movieslot.constants.ApiEndpoint
import com.prudhvilearning.movieslot.ui.data.CategoryDataModel
import com.prudhvilearning.movieslot.ui.data.GenresDataModel
import com.prudhvilearning.movieslot.ui.data.ImagesData
import com.prudhvilearning.movieslot.ui.data.KeywordResponse
import com.prudhvilearning.movieslot.ui.data.ListedMoviesDataModel
import com.prudhvilearning.movieslot.ui.data.Movie
import com.prudhvilearning.movieslot.ui.data.MovieCast
import com.prudhvilearning.movieslot.ui.data.MovieDetails
import com.prudhvilearning.movieslot.ui.data.Review
import com.prudhvilearning.movieslot.ui.data.ReviewResponse
import com.prudhvilearning.movieslot.ui.data.SimilarMoviesDetails
import com.prudhvilearning.movieslot.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class MovieViewModel(private val repo: MoviesRepo) : ViewModel() {



    // Map category -> state flow for that category
    private val _categorizedMoviesStateMap = MutableStateFlow<Map<String, Resource<CategoryDataModel>>>(emptyMap())
    val categorizedMoviesStateMap: StateFlow<Map<String, Resource<CategoryDataModel>>> = _categorizedMoviesStateMap

    private val movieCategoryCache = mutableMapOf<String, CategoryDataModel>()
    private var genresCache: GenresDataModel? = null

    fun fetchMovies(category: String, forceReload: Boolean = false) = viewModelScope.launch {
        if (!forceReload && movieCategoryCache.containsKey(category)) {
            _categorizedMoviesStateMap.value = _categorizedMoviesStateMap.value + (category to Resource.success(movieCategoryCache[category]))
            return@launch
        }

        repo.getCategorizedMovies(category).collect { resource ->
            val currentMap = _categorizedMoviesStateMap.value.toMutableMap()
            currentMap[category] = resource
            _categorizedMoviesStateMap.value = currentMap

            if (resource.status == Status.SUCCESS) {
                movieCategoryCache[category] = resource.data!!
            }
        }
    }


    // Convenience method to fetch all at once
    fun fetchAll(categories: List<ApiEndpoint>) {
        categories.forEach { category ->
            fetchMovies(category.path())
        }
    }

    private val _listedMovies = MutableStateFlow<Map<Int, Resource<ListedMoviesDataModel>>>(emptyMap())
    val listedMovies: StateFlow<Map<Int, Resource<ListedMoviesDataModel>>> = _listedMovies

    fun fetchMovieDetailsForUser(id: Int) = viewModelScope.launch {
        repo.getUserMovies(id).collect { resource ->
            val updatedMap = _listedMovies.value.toMutableMap()
            updatedMap[id] = resource
            _listedMovies.value = updatedMap
        }
    }

    fun fetchListOfMovies(ids: List<Int>) {
        ids.forEach { id ->
            fetchMovieDetailsForUser(id)
        }
    }



    private val _allGenres = MutableStateFlow<Resource<GenresDataModel>>(Resource.loading(null))
    val allGenres: StateFlow<Resource<GenresDataModel>> = _allGenres

    fun fetchAllGenres(forceReload: Boolean = false) = viewModelScope.launch {
        if (!forceReload && genresCache != null) {
            _allGenres.value = Resource.success(genresCache)
            return@launch
        }

        repo.hitGetAllGenres().collect { resource ->
            _allGenres.value = resource
            if (resource.status == Status.SUCCESS) {
                genresCache = resource.data
            }
        }
    }

    private val _movieDetails = MutableStateFlow<Resource<MovieDetails>>(Resource.loading(null))
    val movieDetails: StateFlow<Resource<MovieDetails>> = _movieDetails
    private val movieDetailsCache = mutableMapOf<Int, MovieDetails>()

    fun fetchMovieDetails(id: Int, forceReload: Boolean = false) = viewModelScope.launch {
        if (!forceReload && movieDetailsCache.containsKey(id)) {
            _movieDetails.value = Resource.success(movieDetailsCache[id])
            return@launch
        }

        repo.hitGetMovieDetails(id).collect { resource ->
            _movieDetails.value = resource
            if (resource.status == Status.SUCCESS) {
                movieDetailsCache[id] = resource.data!!
            }
        }
    }


    private val _reviews = MutableStateFlow<Resource<ReviewResponse>>(Resource.loading(null))
    val reviews: StateFlow<Resource<ReviewResponse>> = _reviews

    private val reviewCache = mutableMapOf<Int, ReviewResponse>()

    fun fetchReviews(id: Int, forceReload: Boolean = false) = viewModelScope.launch {
        if (!forceReload && reviewCache.containsKey(id)) {
            _reviews.value = Resource.success(reviewCache[id])
            return@launch
        }

        repo.hitGetReview(id).collect { resource ->
            _reviews.value = resource
            if (resource.status == Status.SUCCESS) {
                reviewCache[id] = resource.data!!
            }
        }
    }


    private val _keywords = MutableStateFlow<Resource<KeywordResponse>>(Resource.loading(null))
    val keywords: StateFlow<Resource<KeywordResponse>> = _keywords

    fun fetchKeywords(id : Int) = viewModelScope.launch {
        Log.e("CheckingPath444", "AppNavigation: calling with $id")

        repo.hitGetKeywords(id).collect { resource ->
            _keywords.value = resource
        }
    }

    private val _credits = MutableStateFlow<Resource<MovieCast>>(Resource.loading(null))
    val credits: StateFlow<Resource<MovieCast>> = _credits

    fun fetchCredits(id : Int) = viewModelScope.launch {
        Log.e("CheckingPath444", "AppNavigation: calling with $id")

        repo.hitGetCredits(id).collect { resource ->
            _credits.value = resource
        }
    }

    private val _similarMovies = MutableStateFlow<Resource<SimilarMoviesDetails>>(Resource.loading(null))
    val similarMovies: StateFlow<Resource<SimilarMoviesDetails>> = _similarMovies

    fun fetchSimilarMovies(id : Int) = viewModelScope.launch {
        Log.e("CheckingPath444", "AppNavigation: calling with $id")

        repo.hitGetSimilarMovies(id).collect { resource ->
            _similarMovies.value = resource
        }
    }

    private val _imagesData = MutableStateFlow<Resource<ImagesData>>(Resource.loading(null))
    val imagesData: StateFlow<Resource<ImagesData>> = _imagesData

    fun fetchImages(id : Int) = viewModelScope.launch {
        Log.e("CheckingPath444", "AppNavigation: calling with $id")

        repo.hitGetImages(id).collect { resource ->
            _imagesData.value = resource
        }
    }


    // In your MovieViewModel
    val movies = repo.getDiscoverMoviesPager().flow.cachedIn(viewModelScope)
    val genreMovies: (Int) -> Flow<PagingData<Movie>> = { genreId -> repo.getGenreMovies(genreId).flow.cachedIn(viewModelScope)}
    val categoryMovies: (String) -> Flow<PagingData<Movie>> = { category -> repo.getMovieDetailsPager(category).flow.cachedIn(viewModelScope)}
    val keywordMovies: (String) -> Flow<PagingData<Movie>> = { keyword -> repo.getKeywordPagingSource(keyword).flow.cachedIn(viewModelScope)}


}





