package com.rab.bit.road104.ui.screens.splash

import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rab.bit.road104.MainActivity
import com.rab.bit.road104.R
import com.rab.bit.road104.model.data.LoadingState
import com.rab.bit.road104.navigation.ScreenRoutes
import com.rab.bit.road104.ui.elements.Background
import com.rab.bit.road104.util.lockOrientation
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    BackHandler {}

    val context = LocalContext.current
    val activity = context as? MainActivity
    activity?.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val state = viewModel.liveState.collectAsState().value

    LaunchedEffect(state) {
        when (state) {
            is LoadingState.ContentState -> {
                val url = URLEncoder.encode(state.url, StandardCharsets.UTF_8.toString())
                val route = "${ScreenRoutes.ContentScreen.route}/$url"
                navController.navigate(route)
            }

            LoadingState.HomeState -> {
                delay(1500)
                navController.navigate(ScreenRoutes.HomeScreen.route)
            }

            LoadingState.InitState -> {
                viewModel.load(context)
            }

            LoadingState.NoNetworkState -> {
                navController.navigate(ScreenRoutes.NoNetworkScreen.route)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Background()
        val infiniteTransition = rememberInfiniteTransition()

        val angle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "angle"
        )

        Image(
            painterResource(R.drawable.catch_2),
            R.drawable.catch_2.toString(),
            Modifier
                .size(120.dp)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                    rotationZ = angle
                },
            contentScale = ContentScale.FillHeight
        )
    }
}
