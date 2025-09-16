package com.rab.bit.road104.util.web

import android.content.Intent
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.rab.bit.road104.MainActivity
import com.rab.bit.road104.model.repo.impl.DataStoreRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainWebViewClient(
    private val activity: MainActivity,
    private val onWhite: () -> Unit,
    private val showWeb: () -> Unit
) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d("123123", "Main WebView Parent URL is $url")
        url ?: return
        CookieManager.getInstance().flush()
        if (url.contains("Cj5ny2NT")) {
            activity.lifecycleScope.launch {
                delay(1500)
            }
            onWhite.invoke()
        } else {
            val dataStore = DataStoreRepositoryImpl(activity)
            dataStore.saveUrl(url)
            dataStore.saveAdb(false)
        }
        activity.lifecycleScope.launch {
            delay(1500)
            showWeb.invoke()
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        Log.d("123123", "Should shouldOverrideUrlLoading ${request?.url}")

        // For diia
        if (request?.url.toString().contains("diia.app")) {
            try {
                val intentDiia = Intent(Intent.ACTION_VIEW)
                val dataForIntent = request?.url.toString().replaceFirst("https://", "diia://")
                intentDiia.setData(dataForIntent.toUri())
                activity.startActivity(intentDiia)
            } catch (e: Exception) {

            }
        }

        return super.shouldOverrideUrlLoading(view, request)
    }
}