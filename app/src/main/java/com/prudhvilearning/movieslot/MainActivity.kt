package com.prudhvilearning.movieslot

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prudhvilearning.movieslot.ui.screens.DiscoverScreen
import com.prudhvilearning.movieslot.ui.screens.DynamicListScreen
import com.prudhvilearning.movieslot.ui.screens.HomeScreen
import com.prudhvilearning.movieslot.ui.screens.MovieDetailScreen
import com.prudhvilearning.movieslot.ui.screens.MyListScreen
import com.prudhvilearning.movieslot.ui.theme.MoviesLotTheme
import com.prudhvilearning.movieslot.ui.theme.TabSelectedColor
import com.prudhvilearning.movieslot.ui.theme.TabUnSelectedColor
import com.prudhvilearning.movieslot.utils.Apputils
import com.prudhvilearning.movieslot.utils.BottomBarScreen
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesLotTheme {
                AppNavigation(activity = this)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppNavigation(activity: Activity) {
    val navController = rememberNavController()
    val windowSizeClass = calculateWindowSizeClass(activity)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Movies.route
        ) {
            composable(BottomBarScreen.Movies.route) {
                HomeScreen(
                    windowWidthSizeClass = windowSizeClass.widthSizeClass,
                    innerPadding = innerPadding,
                    onSeeAllClick = { heading , type , id ->
                        Log.e("TAG", "AppNavigation: $heading $type $id")
                        val encodedHeading = Apputils.getCategoryForSafeNavigation(heading)
                        navController.navigate("list/$encodedHeading/$type/$id")
                    },
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    }
                )
            }
            composable(BottomBarScreen.Discover.route) {
                DiscoverScreen()
            }
            composable(BottomBarScreen.MyLists.route) {
                MyListScreen()
            }
            composable(
                "list/{heading}/{isGenre}/{genreId}",
                arguments = listOf(
                    navArgument("heading") { type = NavType.StringType },
                    navArgument("type") { type = NavType.StringType },
                    navArgument("genreId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val heading = backStackEntry.arguments?.getString("heading") ?: ""
                val type = backStackEntry.arguments?.getString("type") ?: ""
                val genreId = backStackEntry.arguments?.getInt("genreId") ?: 0

                DynamicListScreen(
                    heading = heading,
                    type = type,
                    genreId = genreId,
                    onBackClick = { navController.popBackStack() },
                    onFilterClick = {}
                )
            }
            composable(
                "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                MovieDetailScreen(
                    movieId = id,
                    onBackClick = { navController.popBackStack() },
                    onSeeAllClick = { heading, type, id ->
                        val encodedHeading = URLEncoder.encode(heading, "UTF-8")
                        navController.navigate("list/$encodedHeading/$type/$id")
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val items = listOf(
        BottomBarScreen.Movies,
        BottomBarScreen.Discover,
        BottomBarScreen.MyLists
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        renderEffect = RenderEffect
                            .createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
                    .background(Color.White.copy(alpha = 0.9f))
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.White.copy(alpha = 0.9f))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { screen ->
                val currentRoute = currentDestination?.route ?: ""

                val selected = when {
                    screen.route == BottomBarScreen.Movies.route && (currentRoute == screen.route || currentRoute.startsWith("list/") || currentRoute.startsWith("details/")) -> true
                    else -> currentRoute == screen.route
                }

                IconWithLabel(
                    icon = painterResource(id = screen.icon),
                    label = screen.name,
                    tint = if (selected) TabSelectedColor else TabUnSelectedColor,
                    onClick = {
                        if (!selected) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun IconWithLabel(
    icon: Painter,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = tint
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = tint)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    MoviesLotTheme {
        AppNavigation(activity = LocalActivity.current as Activity)
    }
}
