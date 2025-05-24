    package com.prudhvilearning.movieslot.ui.screens

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Favorite
    import androidx.compose.material.icons.filled.FavoriteBorder
    import androidx.compose.material.icons.filled.KeyboardArrowRight
    import androidx.compose.material3.*
    import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.Font
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextOverflow
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import coil.compose.rememberAsyncImagePainter
    import com.prudhvilearning.movieslot.constants.ApiEndpoint
    import com.prudhvilearning.movieslot.ui.data.CategoryDataModel
    import com.prudhvilearning.movieslot.ui.data.GenresDataModel
    import com.prudhvilearning.movieslot.ui.data.Movie
    import com.prudhvilearning.movieslot.utils.Resource
    import com.prudhvilearning.movieslot.utils.Status
    import com.prudhvilearning.movieslot.viewModel.MovieViewModel
    import org.koin.androidx.compose.koinViewModel
    import com.prudhvilearning.movieslot.R
    import com.prudhvilearning.movieslot.constants.AppConstants
    import com.prudhvilearning.movieslot.ui.data.Genre
    import com.prudhvilearning.movieslot.ui.theme.Indigo
    import com.prudhvilearning.movieslot.ui.theme.TabSelectedColor
    import com.prudhvilearning.movieslot.utils.Apputils.getCategory
    import com.prudhvilearning.movieslot.utils.Apputils.getFiarWeatherBold
    import com.prudhvilearning.movieslot.utils.DataType
    import com.prudhvilearning.movieslot.utils.SharedPrefsHelper

    /**
     * HomeScreen entry composable selecting layout based on window size class
     */
    @Composable
    fun HomeScreen(
        windowWidthSizeClass: WindowWidthSizeClass,
        viewModel: MovieViewModel = koinViewModel(),
        innerPadding: PaddingValues = PaddingValues(0.dp),
        onSeeAllClick: (String, String, Int)  -> Unit,
        onMovieClick: (Int)  -> Unit
    ) {

        val categorizedMoviesMap by viewModel.categorizedMoviesStateMap.collectAsState()
        val allGenres by viewModel.allGenres.collectAsState()
        CallApi(viewModel)
        openHomeScreen(
            windowWidthSizeClass = windowWidthSizeClass,
            innerPadding = innerPadding,
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            categorizedMoviesMap = categorizedMoviesMap,
            allGenres = allGenres
        )
    }

    @Composable
    private fun CallApi(viewModel: MovieViewModel){
        LaunchedEffect(Unit) {
            viewModel.fetchAll(AppConstants.CATEGORY_LIST)
            viewModel.fetchAllGenres()
        }
    }

    @Composable
    private fun openHomeScreen(windowWidthSizeClass: WindowWidthSizeClass  , innerPadding: PaddingValues = PaddingValues(0.dp) , onSeeAllClick: (String, String, Int)  -> Unit,
                       onMovieClick: (Int)  -> Unit , categorizedMoviesMap: Map<String, Resource<CategoryDataModel>>,allGenres: Resource<GenresDataModel>){
        when (windowWidthSizeClass) {
            WindowWidthSizeClass.Compact -> HomeCompactUI(AppConstants.CATEGORY_LIST, categorizedMoviesMap, allGenres , innerPadding , onSeeAllClick , onMovieClick)
            WindowWidthSizeClass.Medium -> HomeMediumUI(AppConstants.CATEGORY_LIST, categorizedMoviesMap, allGenres , onSeeAllClick)
            WindowWidthSizeClass.Expanded -> HomeExpandedUI(
                categorizedMoviesMap.values
                    .mapNotNull { it.data }
                    .toList(),
                allGenres.data,
                onSeeAllClick
            )
            else -> HomeCompactUI(
                AppConstants.CATEGORY_LIST,
                categorizedMoviesMap,
                allGenres,
                onSeeAllClick = onSeeAllClick,
                onMovieClick = onMovieClick
            )
        }
    }

    @Composable
    private fun ResourceAwareCategory(
        category: ApiEndpoint,
        resource: Resource<CategoryDataModel>,
        modifier: Modifier = Modifier,
        onSeeAllClick: (String, String, Int) -> Unit,
        onMovieClick: (Int) -> Unit
    ) {
        when (resource.status) {
            Status.LOADING -> {
                Box(
                    modifier = modifier.padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            Status.SUCCESS -> {
                resource.data?.let {
                    CategoryListItem(it, modifier , onSeeAllClick , onMovieClick)
                }
            }
            Status.ERROR -> {
                Text(
                    text = "Error loading ${category.path()} : ${resource.errorValue?.message}",
                    color = Color.Red,
                    modifier = modifier.padding(16.dp)
                )
            }
        }
    }


    /**
     * Wraps genres resource with loading/error/success handling
     */
    @Composable
    private fun ResourceAwareGenres(resource: Resource<GenresDataModel> , onSeeAllClick: (String, String, Int) -> Unit) {
        when (resource.status) {
            Status.LOADING -> {
                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            Status.SUCCESS -> {
                GenreList(resource.data , onSeeAllClick)
            }
            Status.ERROR -> {
                Text(
                    text = "Error loading genres : ${resource.errorValue?.message}",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    /**
     * Displays a category with heading and horizontal movies list
     */
    @Composable
    private fun CategoryListItem(
        model: CategoryDataModel,
        modifier: Modifier = Modifier,
        onSeeAllClick: (String, String, Int) -> Unit,
        onMovieClick: (Int) -> Unit
    ) {
        Column(modifier = modifier.padding(vertical = 12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeading(getCategory(model.categoryName), Modifier.weight(1f))
                SeeAllButton(model.categoryName, onSeeAllClick)
            }
            Spacer(modifier.height(12.dp))
            MovieRow(model.movies , onMovieClick)
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }


    /**
     * Horizontal scrolling row of movie cards
     */
    @Composable
    private fun MovieRow(movies: List<Movie> , onMovieClick: (Int) -> Unit) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies.size) { index ->
                MovieCard(movies[index] , modifier = Modifier.clickable { onMovieClick(movies[index].id) })
            }
        }
    }

    /**
     * Single movie card UI
     */
    @Composable
    private fun MovieCard(movie: Movie , modifier : Modifier = Modifier) {
        val sharedPrefsHelper = SharedPrefsHelper(LocalContext.current)
        Card(
            modifier = modifier
                .width(150.dp)
                .height(200.dp)
                .padding(start = 16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://image.tmdb.org/t/p/w500"+movie.poster_path,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground), // your placeholder drawable
                        error = painterResource(R.drawable.ic_launcher_foreground)        // your error drawable
                    ),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if(sharedPrefsHelper.isInSeenlist(movie.id) || sharedPrefsHelper.isInWishlist(movie.id)){
                    Icon(
                        painter = painterResource(if(sharedPrefsHelper.isInWishlist(movie.id)) R.drawable.baseline_favorite_24 else R.drawable.ic_seen),
                        contentDescription = "Favorite",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                    )
                }
            }
        }
    }

    /**
     * Vertical list of genres
     */
    @Composable
    private fun GenreList(allGenres: GenresDataModel? , onSeeAllClick: (String, String, Int) -> Unit) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            allGenres?.genres?.let { genres ->
                items(genres.size) { index ->
                    GenreListItem(genres[index] , onSeeAllClick =  onSeeAllClick)
                }
            }
        }
    }

    /**
     * Single genre row with right arrow icon
     */
    @Composable
    private fun GenreListItem(value: Genre, modifier: Modifier = Modifier, onSeeAllClick: (String, String, Int) -> Unit) {
        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .clickable{
                        onSeeAllClick(value.name , DataType.GENRES.name, value.id )
                    },
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = value.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See more",
                    modifier = Modifier.size(20.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                color = Color.LightGray,)
        }
    }

    /**
     * Standard section heading for titles
     */
    @Composable
     fun SectionHeading(text: String, modifier: Modifier = Modifier , textSize : Int = 30) {

            Text(
                text = text,
                color = TabSelectedColor,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = textSize.sp,
                    fontFamily = getFiarWeatherBold()
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
            )

    }

    @Composable
    private fun TopBar(text: String, modifier: Modifier = Modifier , innerPadding: PaddingValues = PaddingValues(0.dp)) {
        val BarlowFont = FontFamily(
            Font(R.font.fairweather_bold, FontWeight.Normal),
        )
        Card(modifier = Modifier,
            elevation = CardDefaults.cardElevation(4.dp)) {
            Text(
                text = text,
                color = TabSelectedColor,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp,
                    fontFamily = BarlowFont,
                    letterSpacing = 1.sp
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = innerPadding.calculateTopPadding())
                    .padding(start = 16.dp, bottom = 8.dp))

        }

    }

    /**
     * Compact layout: vertical scrolling list of categories and genres below
     */
    @Composable
    private fun HomeCompactUI(
        categories: List<ApiEndpoint>,
        categorizedMoviesMap: Map<String, Resource<CategoryDataModel>>,
        allGenres: Resource<GenresDataModel>,
        innerPadding: PaddingValues = PaddingValues(0.dp),
        onSeeAllClick: (String, String, Int) -> Unit,
        onMovieClick: (Int) -> Unit
    ) {
        Scaffold(modifier = Modifier.fillMaxSize() , topBar = {TopBar("Movies", innerPadding =innerPadding) } ){ innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {

                // Movies categories list
                items(categories.size) { index ->
                    ResourceAwareCategory(
                        category = categories[index],
                        resource = categorizedMoviesMap[categories[index].path()] ?: Resource.loading(null),
                        onSeeAllClick = onSeeAllClick,
                        onMovieClick = onMovieClick,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Genres header
                item {
                    SectionHeading("Genres", Modifier.padding(horizontal = 16.dp))
                }

                // Instead of single item with nested LazyColumn,
                // expand genres list as multiple items:
                when (allGenres.status) {
                    Status.LOADING -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    Status.SUCCESS -> {
                        allGenres.data?.genres?.let { genres ->
                            items(genres.size) { index ->
                                GenreListItem(genres[index] , onSeeAllClick = onSeeAllClick)
                            }
                        }
                    }
                    Status.ERROR -> {
                        item {
                            Text(
                                text = "Error loading genres: ${allGenres.errorValue?.message}",
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }



    /**
     * Medium layout: horizontal row for categories, genres below
     */
    @Composable
    private fun HomeMediumUI(
        categories: List<ApiEndpoint>,
        categorizedMoviesMap: Map<String, Resource<CategoryDataModel>>,
        allGenres: Resource<GenresDataModel>,
        onSeeAllClick: (String, String, Int) -> Unit
    ) {

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SectionHeading("Movies", Modifier.padding(16.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        ResourceAwareCategory(
                            category = category,
                            resource = categorizedMoviesMap[category.path()] ?: Resource.loading(null),
                            modifier = Modifier.width(280.dp),
                            onSeeAllClick = {_,_,_ ->},
                            onMovieClick = {}
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeading("Genres", Modifier.padding(horizontal = 16.dp))
                ResourceAwareGenres(resource = allGenres , onSeeAllClick = onSeeAllClick)
            }
        }
    }

    /**
     * Expanded layout: side-by-side list of categories and genres
     */
    @Composable
    private fun HomeExpandedUI(
        categoryDataList: List<CategoryDataModel>?,
        allGenres: GenresDataModel?,
        onSeeAllClick: (String, String, Int) -> Unit
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Categories column
                Column(modifier = Modifier.weight(1f)) {
                    SectionHeading("Movies", Modifier.padding(bottom = 8.dp))
                    categoryDataList?.forEach { categoryData ->
                        CategoryListItem(categoryData , onSeeAllClick = {_,_,_ ->} , onMovieClick = {})
                    }
                }
                // Genres column
                Column(modifier = Modifier.weight(1f)) {
                    SectionHeading("Genres", Modifier.padding(bottom = 8.dp))
                    GenreList(allGenres, onSeeAllClick)
                }
            }
        }
    }





