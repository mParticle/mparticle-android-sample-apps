package com.mparticle.example.higgsshopsampleapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        MParticle.getInstance()?.logScreen("Landing")
        val btnCTA = findViewById(R.id.landing_cta) as Button
        btnCTA.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}