package com.mparticle.example.higgsshopsampleapp;

import android.app.Application
import android.util.Log
import com.mparticle.MParticle
import com.mparticle.MParticleOptions
import com.mparticle.example.higgsshopsampleapp.BuildConfig


class HiggsShopSampleApplication: Application() {
    val TAG = "HiggsShopSampleApplication"
    override fun onCreate() {
        super.onCreate()
        val options: MParticleOptions = MParticleOptions.builder(this)
            .credentials(BuildConfig.HIGGS_SHOP_SAMPLE_APP_KEY, BuildConfig.HIGGS_SHOP_SAMPLE_APP_SECRET)
            .build()

        MParticle.start(options)
    }

}