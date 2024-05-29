package com.example.satumahasiswawebview

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Load the fade-in animation
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_animation)

        // Apply the animation to the root layout
        val rootLayout = findViewById<ImageView>(R.id.splash_image)
        rootLayout.startAnimation(animFadeIn)

        // Set a delay before starting the MainActivity
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)

            // Apply custom animations to the intent
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
