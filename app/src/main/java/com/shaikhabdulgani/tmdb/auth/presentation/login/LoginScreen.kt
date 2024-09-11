package com.shaikhabdulgani.tmdb.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shaikhabdulgani.tmdb.R
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import com.shaikhabdulgani.tmdb.auth.domain.validation.AuthValidators
import com.shaikhabdulgani.tmdb.auth.domain.validation.EmailValidator
import com.shaikhabdulgani.tmdb.auth.domain.validation.PasswordValidator
import com.shaikhabdulgani.tmdb.auth.domain.validation.UsernameValidator
import com.shaikhabdulgani.tmdb.core.presentation.DialogContent
import com.shaikhabdulgani.tmdb.core.presentation.GradientButton
import com.shaikhabdulgani.tmdb.core.presentation.InputText
import com.shaikhabdulgani.tmdb.auth.domain.validation.RepeatPasswordValidator
import com.shaikhabdulgani.tmdb.core.domain.model.User
import com.shaikhabdulgani.tmdb.core.presentation.dummy.DummyAuthRepo
import com.shaikhabdulgani.tmdb.core.presentation.util.ObserveWithLifeCycle
import com.shaikhabdulgani.tmdb.ui.theme.DarkBg
import com.shaikhabdulgani.tmdb.ui.theme.GradientEnd
import com.shaikhabdulgani.tmdb.ui.theme.GradientStart
import com.shaikhabdulgani.tmdb.ui.theme.TMDBTheme
import com.shaikhabdulgani.tmdb.ui.theme.White
import com.shaikhabdulgani.tmdb.ui.theme.White50
import com.shaikhabdulgani.tmdb.ui.theme.spacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow


@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    ObserveWithLifeCycle(flow = viewModel.authState) {
        when (it) {
            is Resource.Error -> {
                if (!it.message.isNullOrBlank()) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                showDialog = false
            }

            is Resource.Loading -> {
                showDialog = true
            }

            is Resource.Success -> {
                onLoginSuccess()
                showDialog = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp)
                .blur(MaterialTheme.spacing.defaultSmall),
            painter = painterResource(id = R.drawable.bg_onboarding),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default),
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = MaterialTheme.spacing.default,
                        topEnd = MaterialTheme.spacing.default
                    )
                )
                .background(color = DarkBg)
                .padding(MaterialTheme.spacing.default)
        ) {
            Text(
                modifier = Modifier.padding(top = MaterialTheme.spacing.defaultSmall),
                text = stringResource(id = R.string.login),
                fontSize = 35.sp,
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        listOf(GradientStart, GradientEnd)
                    )
                )
            )
            Text(
                modifier = Modifier.offset(y = -MaterialTheme.spacing.default),
                text = stringResource(R.string.login_welcome_message),
                color = White50
            )

            InputText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.email_placeholder),
                icon = Icons.Outlined.Email,
                value = viewModel.emailState.value,
                keyboardType = KeyboardType.Email,
                errorMessage = viewModel.emailState.error,
                onTextChange = { viewModel.onEvent(LoginUiEvent.OnEmailChange(it)) }
            )
            InputText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password_placeholder),
                icon = Icons.Outlined.Lock,
                value = viewModel.passwordState.value,
                keyboardType = KeyboardType.Password,
                errorMessage = viewModel.passwordState.error,
                onTextChange = { viewModel.onEvent(LoginUiEvent.OnPasswordChange(it)) },
                isPassword = true,
            )

            Text(
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                context.getString(R.string.forget_password),
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .offset(y = -MaterialTheme.spacing.defaultSmall),
                text = stringResource(R.string.forget_password),
                color = White,
            )

            GradientButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onEvent(LoginUiEvent.LoginClick)
                }
            ) {
                Text(text = stringResource(R.string.login))
            }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            ) {
                Text(
                    text = stringResource(R.string.do_not_have_an_account),
                    color = White,
                )
                Text(
                    modifier = Modifier.clickable {
                        onSignUpClick()
                    },
                    text = stringResource(R.string.sign_up),
                    color = GradientStart,
                )
            }
        }
        if (showDialog) {
            DialogContent(message = stringResource(id = R.string.please_wait))
        }
    }

}

@Preview
@Composable
private fun LoginPreview() {
    TMDBTheme {
        LoginScreen(
            onLoginSuccess = {},
            onSignUpClick = {},
            viewModel = LoginViewModel(
                DummyAuthRepo,
                AuthValidators(
                    emailValidator = EmailValidator(),
                    passwordValidator = PasswordValidator(),
                    rePasswordValidator = RepeatPasswordValidator(),
                    usernameValidator = UsernameValidator(),
                )
            ),
        )
    }
}