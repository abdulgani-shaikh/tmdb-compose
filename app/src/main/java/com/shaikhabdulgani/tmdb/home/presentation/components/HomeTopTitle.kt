package com.shaikhabdulgani.tmdb.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaikhabdulgani.tmdb.R
import com.shaikhabdulgani.tmdb.core.presentation.util.getInitials
import com.shaikhabdulgani.tmdb.ui.theme.White

@Composable
fun HomeTopTitle(modifier: Modifier = Modifier, username: String, onProfileClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(
                weight = 1f
            ),
            text = stringResource(R.string.home_top_title),
            fontSize = 26.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Box(
            modifier = Modifier
                .padding(5.dp)
                .size(40.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = White,
                    shape = CircleShape
                )
                .clickable {
                    onProfileClick.invoke()
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = username.getInitials(),
                textAlign = TextAlign.Center,
                color = White
            )
        }
    }
}