package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent =
                Intent(this@MainActivity, SignIn::class.java)
            finish()
            startActivity(mainIntent)
        }, 2500)
    }
}