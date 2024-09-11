package com.shaikhabdulgani.tmdb.core.presentation.util

import androidx.navigation.NavHostController
import com.shaikhabdulgani.tmdb.global.Screen
import com.shaikhabdulgani.tmdb.home.domain.model.Media

fun NavHostController.toContentDetail(media: Media){
    navigate(Screen.MovieDetail(
        id = media.id,
        mediaType = media.mediaType.getValue(),
    ))
}
fun NavHostController.toProfile(){
    navigate(Screen.Profile)
}