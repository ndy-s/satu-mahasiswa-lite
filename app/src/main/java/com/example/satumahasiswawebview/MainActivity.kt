package com.example.satumahasiswawebview

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var errorLayout: View
    private lateinit var errorTitle: TextView
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button
    private lateinit var backgroundImage: ImageView
    private lateinit var backgroundBox: View
    private var URL = "http://mahasiswa.unri.ac.id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inflate the custom action bar layout
        val actionBar = supportActionBar
        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setCustomView(R.layout.custom_action_bar)

        // Find views
        webView = findViewById(R.id.webView)
        errorLayout = findViewById(R.id.errorLayout)
        errorTitle = findViewById(R.id.errorTitle)
        errorTextView = findViewById(R.id.errorTextView)
        retryButton = findViewById(R.id.retryButton)
        backgroundImage = findViewById(R.id.backgroundImage)
        backgroundBox = findViewById(R.id.backgroundBox)

        // Configure WebView
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                handleError(errorCode, description)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (webView.progress == 100) {
                    val delayMillis = 2000L // 2 seconds delay
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Show WebView after delay
                        webView.visibility = View.VISIBLE
                    }, delayMillis)
                }
            }
        }

        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportZoom(true)

        loadUrl(URL)

        // Set up retry button click listener
        retryButton.setOnClickListener {
            showSplashAndRetry()
        }
    }

    private fun loadUrl(url: String) {
        // Hide error layout
        errorLayout.visibility = View.GONE
        backgroundImage.visibility = View.GONE
        backgroundBox.visibility = View.GONE
        // Hide WebView initially
        webView.visibility = View.GONE
        // Load URL
        webView.loadUrl(url)
    }

    private fun showSplashAndRetry() {
        // Launch SplashActivity
        val intent = Intent(this, SplashActivity::class.java)

        // Apply custom animations to the intent
        startActivity(intent)
        finish()
    }

    private fun showErrorPage(errorMessage: String, errorTitleMessage: String? = null) {
        // Hide WebView
        webView.visibility = View.GONE

        // Show error layout
        errorLayout.visibility = View.VISIBLE
        backgroundImage.visibility = View.VISIBLE
        backgroundBox.visibility = View.VISIBLE

        // Set error title if provided
        if (errorTitleMessage != null) {
            errorTitle.text = errorTitleMessage
        }

        // Set error message
        errorTextView.text = errorMessage
    }

    private fun handleError(errorCode: Int?, description: String?) {
        val message: String = when (errorCode) {
            WebViewClient.ERROR_HOST_LOOKUP -> "Please check your internet connection and try again."
            WebViewClient.ERROR_FILE_NOT_FOUND -> "The requested page could not be found!"
            WebViewClient.ERROR_CONNECT -> "Failed to connect to the server. Please try again later."
            in 400..499 -> "Client error occurred. Please try again later."
            in 500..599 -> "Server error occurred. Please try again later."
            else -> "An unexpected error occurred. Please try again later."
        }
        val title: String = when (errorCode) {
            WebViewClient.ERROR_HOST_LOOKUP -> "Host Not Found!"
            WebViewClient.ERROR_FILE_NOT_FOUND -> "Page not found!"
            WebViewClient.ERROR_CONNECT -> "Connection Error!"
            in 400..499 -> "Client Error!"
            in 500..599 -> "Server Error!"
            else -> "Error!"
        }
        showErrorPage(message, title)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
