package com.mparticle.example.higgsshopsampleapp;

import android.app.Application
import com.mparticle.MParticle
import com.mparticle.MParticleOptions
import com.mparticle.kits.LocalKit
import com.mparticle.networking.NetworkOptions

class HiggsShopSampleApplication: Application() {
    val TAG = "HiggsShopSampleApplication"
    override fun onCreate() {
        super.onCreate()
        val options: MParticleOptions = MParticleOptions.builder(this)
            .credentials("us1-00d2e177de62f84baae3239825ced69b", "WVqu6dsZ9YGxcLNFsfB97tfp8Ov2uMXHYXFMLjryXfx5iDap1RWIFWLkgAAUOenC")//BuildConfig.HIGGS_SHOP_SAMPLE_APP_KEY, BuildConfig.HIGGS_SHOP_SAMPLE_APP_SECRET)
            .environment(MParticle.Environment.Development)
            .sideloadedKits(mutableListOf(LocalKit().apply { kit = LoggingCustomKit() }) as List<Any>)
//            .addCustomKit(7829, LoggingCustomKit() )
            // Disable SSL pinning for debugging network requests
//            .networkOptions(
//                NetworkOptions.builder()
//                .setPinningDisabledInDevelopment(true)
//                .build())
            // logLevel can be 'NONE', 'ERROR', 'WARNING', 'DEBUG', 'VERBOSE', or 'INFO
            // (the default is 'DEBUG').
            // This logLevel provides context into the inner workings of mParticle.
            // It can be updated after MP has been initialized using mParticle.setLogLevel().
            // and passing.  Logs will be available in the inspector.
            // More can be found at https://docs.mparticle.com/developers/sdk/android/logger/
            .logLevel(MParticle.LogLevel.VERBOSE)
            .build()


        MParticle.start(options)
        // Replace FCM Sender ID from Firebase Console
        // MParticle.getInstance()?.Messaging()?.enablePushNotifications(BuildConfig.HIGGS_SHOP_SAMPLE_FCM_SENDER_ID)
        // MParticle.getInstance()?.Messaging()?.displayPushNotificationByDefault(true)
    }
}