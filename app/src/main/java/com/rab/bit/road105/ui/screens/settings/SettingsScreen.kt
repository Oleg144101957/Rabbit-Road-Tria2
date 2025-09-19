package com.rab.bit.road105.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.rab.bit.road105.R
import com.rab.bit.road105.model.data.catcher.DifficultyOptions
import com.rab.bit.road105.navigation.ScreenRoutes
import com.rab.bit.road105.ui.elements.Background
import com.rab.bit.road105.ui.elements.DropDownMenu
import com.rab.bit.road105.ui.theme.DefFont
import com.rab.bit.road105.util.CustomTabsUtil

@Composable
fun SettingsScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val difficultyOptions = viewModel.getSpeed()
    val context = LocalContext.current

    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Background()
        Image(
            painterResource(R.drawable.ic_back),
            R.drawable.ic_back.toString(),
            modifier = Modifier
                .padding(8.dp)
                .size(64.dp)
                .align(Alignment.TopStart)
                .clickable {
                    navController.popBackStack()
                },
            contentScale = ContentScale.FillWidth
        )
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Settings", color = Color.White, fontSize = 48.sp, fontFamily = DefFont)
            Spacer(Modifier.height(64.dp))
            DropDownMenu(
                items = DifficultyOptions.entries,
                initialSelectedIndex = DifficultyOptions.entries.indexOf(difficultyOptions)
            ) {
                viewModel.setSpeed(it)
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "About us",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = DefFont,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate(ScreenRoutes.AboutScreen.route)
                }
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Privacy policy",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = DefFont,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    CustomTabsUtil.openCustomTab(
                        context,
                        "https://sites.google.com/view/cj5ny2nt/Cj5ny2NT"
                    )
                }
            )
        }
    }
}
