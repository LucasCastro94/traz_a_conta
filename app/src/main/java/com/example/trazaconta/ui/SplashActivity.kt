package com.example.trazaconta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.trazaconta.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({openMainActivity()}, 3000)
    }
    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}