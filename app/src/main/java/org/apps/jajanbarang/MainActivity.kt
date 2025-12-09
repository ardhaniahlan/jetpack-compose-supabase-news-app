package org.apps.jajanbarang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.apps.jajanbarang.domain.model.Article
import org.apps.jajanbarang.presentation.BottomNavItem
import org.apps.jajanbarang.presentation.BottomNavigationBar
import org.apps.jajanbarang.presentation.profile.ProfileScreen
import org.apps.jajanbarang.presentation.SplashScreen
import org.apps.jajanbarang.presentation.auth.LoginScreen
import org.apps.jajanbarang.presentation.auth.RegisterScreen
import org.apps.jajanbarang.presentation.home.NewsDetailScreen
import org.apps.jajanbarang.presentation.home.NewsScreen
import org.apps.jajanbarang.presentation.home.NewsWebViewScreen
import org.apps.jajanbarang.ui.theme.JajanBarangTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomNav = currentRoute in listOf(
                BottomNavItem.News.route,
                BottomNavItem.Profile.route
            )

            JajanBarangTheme {
                Scaffold(
                    bottomBar = {
                        if (showBottomNav) {
                            BottomNavigationBar(navController)
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    },
                    contentWindowInsets = WindowInsets.safeGestures
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        startDestination = "splash"
                    ) {
                        // Auth
                        composable("splash"){
                            SplashScreen(navController)
                        }
                        composable("login"){
                            LoginScreen(navController, snackbarHostState)
                        }
                        composable("register"){
                            RegisterScreen(navController, snackbarHostState)
                        }

                        // App
                        composable(BottomNavItem.News.route) {
                            NewsScreen(navController)
                        }

                        composable("newsDetail") {
                            val article = navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
                            if (article != null) {
                                NewsDetailScreen(
                                    navController = navController,
                                    article = article
                                )
                            }
                        }

                        composable("newsWebView") {
                            val url = navController.previousBackStackEntry?.savedStateHandle?.get<String>("url")
                            if (!url.isNullOrEmpty()) {
                                NewsWebViewScreen(
                                    url = url,
                                    navController = navController
                                )
                            }
                        }

                        composable(BottomNavItem.Profile.route) {
                            ProfileScreen(navController)
                        }

                    }
                }
            }
        }
    }
}