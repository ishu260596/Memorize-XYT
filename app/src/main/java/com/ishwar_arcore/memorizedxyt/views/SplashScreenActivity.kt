package com.ishwar_arcore.memorizedxyt.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ishwar_arcore.memorizedxyt.R


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide();

        Handler().postDelayed({
            val i = Intent(
                this,
                MainActivity::class.java
            )

            startActivity(i)

            finish()

        }, 4500)
    }
}