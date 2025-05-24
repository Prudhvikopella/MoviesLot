package com.prudhvilearning.movieslot.utils

import com.prudhvilearning.movieslot.R

sealed class BottomBarScreen(val route: String, val name: String, val icon: Int) {
    object Movies : BottomBarScreen("movies", "Movies", R.drawable.ic_movies_ns)
    object Discover : BottomBarScreen("discover", "Discover", R.drawable.ic_discover)
    object MyLists : BottomBarScreen("mylists", "My Lists", R.drawable.ic_mylist)
}

enum class SelectionState {
    WISHLIST,
    SEENLIST,
    NONE
}

enum class SwipeAction {
    NONE, WISHLIST, SEEN
}

enum class SortType {
    RELEASE_DATE,
    RATING,
    POPULARITY
}

enum class MovieListType {
    WISHLIST,
    SEENLIST
}

enum class DataType {
    GENRES,
    CATEGORY,
    KEYWORD
}



