package com.shaikhabdulgani.tmdb.core.presentation.util

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import com.shaikhabdulgani.tmdb.core.domain.model.User
import kotlinx.coroutines.flow.Flow

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}


fun Modifier.shimmer(
    backgroundColor: Color = Color(0xFFA0A0A0),
    highlightColor: Color = Color(0xFFC9C9C9)
): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition("shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 1000
            )
        ), label = ""
    )
    background(backgroundColor)
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                highlightColor.copy(0.5f),
                highlightColor,
                highlightColor.copy(0.5f),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()),
        ),
    )
        .onGloballyPositioned {
            size = it.size
        }
}

fun NavOptionsBuilder.clearBackStack(controller: NavController) {
    popUpTo(controller.graph.findStartDestination().id) {
        inclusive = true
    }
}

private var toast: Toast? = null

fun Context.toast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT).also { it.show() }
}

fun String.getInitials(): String {
    val username = trim()
    return if (username.length<=1){
        this
    }else{
        username[0].toString() + username[1].toString()
    }
}

fun emptyUser():User{
    return User(
        "",
        "",
        "",
        emptyList()
    )
}

@Composable
fun <T> ObserveWithLifeCycle(flow: Flow<T>, action: (T) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collect(action)
        }
    }
}