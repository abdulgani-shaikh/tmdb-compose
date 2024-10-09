package com.shaikhabdulgani.tmdb.global

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.auth.presentation.login.LoginScreen
import com.shaikhabdulgani.tmdb.auth.presentation.login.LoginViewModel
import com.shaikhabdulgani.tmdb.auth.presentation.signup.SignUpScreen
import com.shaikhabdulgani.tmdb.auth.presentation.signup.SignUpViewModel
import com.shaikhabdulgani.tmdb.core.presentation.util.clearBackStack
import com.shaikhabdulgani.tmdb.home.presentation.HomeScreen
import com.shaikhabdulgani.tmdb.home.presentation.HomeViewModel
import com.shaikhabdulgani.tmdb.moviedetail.presentation.MovieDetailScreen
import com.shaikhabdulgani.tmdb.moviedetail.presentation.MovieDetailViewModel
import com.shaikhabdulgani.tmdb.onboarding.OnboardingScreen
import com.shaikhabdulgani.tmdb.profile.presentation.ProfileScreen
import com.shaikhabdulgani.tmdb.profile.presentation.ProfileViewModel
import com.shaikhabdulgani.tmdb.search.presentation.SearchScreen
import com.shaikhabdulgani.tmdb.search.presentation.SearchViewModel
import com.shaikhabdulgani.tmdb.ui.theme.TMDBTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {


    private val authRepository: AuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TMDBTheme {
                MainApp(authRepository)
            }
        }
    }
}

@Composable
fun MainApp(authRepository: AuthRepository) {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = if (authRepository.isLoggedIn()) Screen.Home else Screen.Onboarding) {
        composable<Screen.Onboarding> {
            OnboardingScreen(controller)
        }
        composable<Screen.Login> {
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                onLoginSuccess = {
                    controller.navigate(Screen.Home) {
                        clearBackStack(controller)
                    }
                },
                onSignUpClick = { controller.navigate(Screen.SignUp) },
                viewModel = viewModel
            )
        }
        composable<Screen.SignUp> {
            val viewModel: SignUpViewModel = koinViewModel()
            SignUpScreen(
                controller = controller,
                viewModel = viewModel
            )
        }
        composable<Screen.Home> {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(controller, viewModel)
        }

        composable<Screen.Search> {
            val viewModel: SearchViewModel = koinViewModel()
            SearchScreen(controller,viewModel)
        }
        composable<Screen.MovieDetail> {
            val args = it.toRoute<Screen.MovieDetail>()
            val viewModel = koinViewModel<MovieDetailViewModel>()
            MovieDetailScreen(
                id = args.id,
                contentType = args.mediaType,
                controller = controller,
                viewModel = viewModel
            )
        }
        composable<Screen.Profile> {
            val viewModel: ProfileViewModel = koinViewModel()
            ProfileScreen(
                controller = controller,
                viewModel = viewModel
            )
        }
    }
}