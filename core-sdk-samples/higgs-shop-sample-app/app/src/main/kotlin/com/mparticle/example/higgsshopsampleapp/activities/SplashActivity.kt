package com.mparticle.example.higgsshopsampleapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAfterTransition
import com.mparticle.example.higgsshopsampleapp.R
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, LandingActivity::class.java)

        //add slight delay so you can see splash screen
        Timer().schedule(500) {
            startActivity(intent)
            finishAfterTransition(this@SplashActivity)
        }
    }
}