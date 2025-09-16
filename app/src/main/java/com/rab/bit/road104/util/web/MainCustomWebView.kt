package com.rab.bit.road104.util.web

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher
import com.rab.bit.road104.MainActivity

@SuppressLint("SetJavaScriptEnabled")
class MainCustomWebView(
    context: MainActivity,
    content: ActivityResultLauncher<String>,
    onWhite: () -> Unit,
    onShowWeb: () -> Unit
) : WebView(context) {

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webChromeClient = MainWebChromeClient(content, context, this)
        webViewClient = MainWebViewClient(context, onWhite, onShowWeb)

        with(settings) {
            databaseEnabled = true
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            allowContentAccess = true
            allowFileAccess = true
            useWideViewPort = true
            builtInZoomControls = false
            displayZoomControls = false
            setSupportZoom(true)
            loadWithOverviewMode = true
            loadsImagesAutomatically = true
            defaultTextEncodingName = "UTF-8"
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            userAgentString = userAgentString.agentChanger()
        }

        setLayerType(LAYER_TYPE_HARDWARE, null)

        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false

        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        CookieManager.getInstance().setAcceptCookie(true)
    }

    private fun String.agentChanger(): String {
        return this.replace("wv", "")
    }
}
