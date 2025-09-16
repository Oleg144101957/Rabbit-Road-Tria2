package com.rab.bit.road104.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rab.bit.road104.navigation.ScreenRoutes
import com.rab.bit.road104.ui.elements.Background
import com.rab.bit.road104.ui.elements.DefaultButton

@Composable
fun HomeScreen(navController: NavController, paddingValues: PaddingValues) {
    BackHandler {}

    var visible by remember { mutableStateOf(false) }

    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else 500.dp, // откуда выезжает
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing
        ),
        label = "menu_slide"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Background()
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
                .offset(y = offsetY)
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaultButton(text = "Play") {
                    navController.navigate(ScreenRoutes.CatcherScreen.route)
                }
                Spacer(Modifier.height(16.dp))
                DefaultButton(text = "Top Table") {
                    navController.navigate(ScreenRoutes.TopGamesScreen.route)
                }
                Spacer(Modifier.height(16.dp))
                DefaultButton(text = "Settings") {
                    navController.navigate(ScreenRoutes.SettingsScreen.route)
                }
            }
        }
    }
}
