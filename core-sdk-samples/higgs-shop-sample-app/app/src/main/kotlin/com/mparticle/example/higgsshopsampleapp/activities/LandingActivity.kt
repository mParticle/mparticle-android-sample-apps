package com.mparticle.example.higgsshopsampleapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R


class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        MParticle.getInstance()?.logScreen("Landing")
    }
}