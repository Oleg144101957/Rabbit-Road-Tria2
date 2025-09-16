package com.rab.bit.road104.util

import android.R
import android.content.Context
import android.util.Log
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

object CustomTabsUtil {

    fun openCustomTab(context: Context, url: String) {
        if (url.isNotBlank()) {
            val uri = url.toUri()
            if (uri.isAbsolute) {
                val colorGreen = ContextCompat.getColor(context, R.color.white)
                val colorPurple = ContextCompat.getColor(context, R.color.white)

                val intentBuilder = CustomTabsIntent.Builder()
                    .setDefaultColorSchemeParams(
                        CustomTabColorSchemeParams.Builder()
                            .setToolbarColor(colorGreen)
                            .build()
                    )
                    .setColorSchemeParams(
                        CustomTabsIntent.COLOR_SCHEME_DARK, CustomTabColorSchemeParams.Builder()
                            .setToolbarColor(colorPurple)
                            .build()
                    )
                    .setExitAnimations(
                        context,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .setShowTitle(true)
                    .setUrlBarHidingEnabled(true)
                val customTabsIntent = intentBuilder.build()
                customTabsIntent.launchUrl(context, uri)
            } else {
                Log.e("CustomTabsUtil", "Invalid URL: $url")
            }
        } else {
            Log.e("CustomTabsUtil", "URL is empty or blank.")
        }
    }
}
