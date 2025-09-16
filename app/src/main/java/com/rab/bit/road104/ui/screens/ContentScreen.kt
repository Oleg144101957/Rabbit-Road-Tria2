package com.rab.bit.road104.ui.screens

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.rab.bit.road104.MainActivity
import com.rab.bit.road104.model.repo.impl.NetworkCheckerRepositoryImpl
import com.rab.bit.road104.navigation.ScreenRoutes
import com.rab.bit.road104.util.lockOrientation
import com.rab.bit.road104.util.web.MainCustomWebView
import com.rab.bit.road104.util.web.MainWebChromeClient

@Composable
fun ContentScreen(
    navigationController: NavHostController,
    paddingValues: PaddingValues,
    url: String
) {
    val isWebViewVisible = remember { mutableStateOf(false) }
    val showNextButton = remember { mutableStateOf(false) } // состояние для кнопки

    val context = LocalContext.current
    val activity = context as MainActivity
    activity.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)

    val webView = remember { mutableStateOf<MainCustomWebView?>(null) }
    val destination = remember { mutableStateOf(url) }
    val networkChecker = NetworkCheckerRepositoryImpl(context)

    val fileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        webView.value?.let { webViewInstance ->
            (webViewInstance.webChromeClient as? MainWebChromeClient)?.onFileCallback(uris.toTypedArray())
        }
    }

    BackHandler {
        webView.value?.let {
            val child = it.getChildAt(0)
            if (child != null) {
                it.removeView(child)
            } else {
                if (it.canGoBack()) {
                    it.goBack()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(paddingValues)
    ) {
        if (!isWebViewVisible.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        destination.value.let { url ->
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                factory = {
                    MainCustomWebView(
                        context = activity,
                        content = fileChooserLauncher,
                        onWhite = {
                            showNextButton.value = true // показываем кнопку
                        },
                        onShowWeb = {
                            isWebViewVisible.value = true
                        }
                    ).apply {
                        webView.value = this

                        if (networkChecker.isConnectionAvailable()) {
                            loadUrl(url)
                        } else {
                            navigationController.navigate(ScreenRoutes.NoNetworkScreen.route)
                        }
                    }
                }
            )
        }

        if (showNextButton.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        navigationController.navigate(ScreenRoutes.HomeScreen.route)
                    },
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                ) {
                    Text("Next")
                }
            }
        }
    }
}
