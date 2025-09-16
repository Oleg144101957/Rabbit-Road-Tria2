package com.rab.bit.road104.util.web

import android.net.Uri
import android.os.Message
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher
import com.rab.bit.road104.MainActivity

class MainWebChromeClient(
    private val content: ActivityResultLauncher<String>,
    private val context1: MainActivity,
    private val parentWebView: MainCustomWebView
) : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri?>>? = null
    private var newCustomWebViewChild: CustomWebViewForChildren? = null


    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri?>>,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        // Сохраняем ссылку на filePathCallback
        this.filePathCallback = filePathCallback
        // Вызываем выбор контента
        content.launch("image/*")
        return true
    }

    fun onFileCallback(uris: Array<Uri?>) {
        // Передаем выбранные файлы в filePathCallback
        filePathCallback?.onReceiveValue(uris)
        // Очищаем filePathCallback после обработки
        filePathCallback = null
    }


    override fun onCreateWindow(
        view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?
    ): Boolean {
        Log.d("123123", "onCreateWindow $resultMsg")

        newCustomWebViewChild = CustomWebViewForChildren(context1, parentWebView)

        newCustomWebViewChild!!.webViewClient =
            WebViewClientForChildren(
                parentWebView = parentWebView,
                webViewChild = newCustomWebViewChild!!
            )


        val newWebChromeClientForChild = object : WebChromeClient() {
            override fun onCloseWindow(window: WebView?) {
                super.onCloseWindow(window)
                parentWebView.removeView(window)
            }
        }

        newCustomWebViewChild!!.webChromeClient = newWebChromeClientForChild

        parentWebView.addView(newCustomWebViewChild)

        val trans = resultMsg?.obj as WebView.WebViewTransport
        trans.webView = newCustomWebViewChild
        resultMsg.sendToTarget()

        return true
    }


}


