package com.shaikhabdulgani.tmdb.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.NavHostController
import com.shaikhabdulgani.tmdb.core.presentation.GradientButton
import com.shaikhabdulgani.tmdb.core.presentation.MediaRow
import com.shaikhabdulgani.tmdb.core.presentation.util.clearBackStack
import com.shaikhabdulgani.tmdb.core.presentation.util.toContentDetail
import com.shaikhabdulgani.tmdb.global.Screen
import com.shaikhabdulgani.tmdb.ui.theme.DarkBg
import com.shaikhabdulgani.tmdb.ui.theme.GradientStart
import com.shaikhabdulgani.tmdb.ui.theme.Transparent
import com.shaikhabdulgani.tmdb.ui.theme.spacing

@Composable
fun ProfileScreen(
    controller: NavHostController,
    viewModel: ProfileViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.getProfileData()
    }
    if (!viewModel.isLoggedIn) {
        controller.navigate(Screen.Login)
    }
    MaterialTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = DarkBg)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            GradientStart,
                            Transparent,
                            Transparent,
                        )
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.default,
                    end = MaterialTheme.spacing.default,
                    top = MaterialTheme.spacing.default
                ),
            ) {
                Text(text = viewModel.user.email)
                Text(text = viewModel.user.username)

            }
            MediaRow(
                title = "Your Bookmarks",
                movies = viewModel.favorites,
                isLoading = viewModel.isLoading,
                onClick = controller::toContentDetail
            )
            GradientButton(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.default)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.logout()
                    controller.navigate(Screen.Onboarding) {
                        clearBackStack(controller)
                    }
                }) {
                Text(text = "Logout")
            }
        }
    }
}

@Composable
fun ProfileShimmer(modifier: Modifier = Modifier) {
    Column {

    }
}