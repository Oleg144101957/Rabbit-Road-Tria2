package com.rab.bit.road104.util.web

import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewClientForChildren(
    private val parentWebView: MainCustomWebView,
    private val webViewChild: CustomWebViewForChildren,
) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        Log.d("123123", "onPageFinished for Children")

        val child: View? = parentWebView.getChildAt(0)

        if (child == null) {
            parentWebView.addView(webViewChild)
        }
        CookieManager.getInstance().flush()
    }

}