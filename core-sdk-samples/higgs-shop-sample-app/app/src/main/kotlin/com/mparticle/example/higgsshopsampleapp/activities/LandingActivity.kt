package com.mparticle.example.higgsshopsampleapp.activities

import android.app.ActionBar
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MPEvent
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.BuildConfig
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.utils.Constants

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        MParticle.getInstance()?.logScreen("Landing")

        val btnCTA = findViewById(R.id.landing_cta) as Button
        if(hasApiKey()) {
            btnCTA.isClickable = true
            btnCTA.alpha = 1.0F
            btnCTA.setOnClickListener {
                val event = MPEvent.Builder("Landing Button Click", MParticle.EventType.Other)
                    .build()
                MParticle.getInstance()?.logEvent(event)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnCTA.isClickable = false
            btnCTA.alpha = 0.3F
            showBlankAPIKeyAlert()
        }
    }

    fun hasApiKey(): Boolean {
        return !(BuildConfig.HIGGS_SHOP_SAMPLE_APP_KEY.isNullOrBlank()
                || BuildConfig.HIGGS_SHOP_SAMPLE_APP_SECRET.isNullOrBlank())
    }

    fun showBlankAPIKeyAlert() {
        val parentLayout: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(parentLayout, getString(R.string.landing_apikey_alert), Snackbar.LENGTH_INDEFINITE)
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)

        val tv = (snackbar.view.findViewById<TextView>(R.id.snackbar_text))
        tv?.maxLines = 5
        tv?.textAlignment = View.TEXT_ALIGNMENT_TEXT_START

        snackbar.setBackgroundTint(getColor(R.color.white))
        snackbar.setTextColor(getColor(R.color.black))
        snackbar.view.layoutParams = layoutParams
        snackbar.view.setPadding(0, 10, 0, 0)
        snackbar.setActionTextColor(getColor(R.color.blue_4079FE))

        val snackbarActionTextView =
            snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_action) as TextView
        snackbarActionTextView.setAllCaps(false)
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
        snackbar.setAction("Go to docs") {
            val url = Constants.URL_DOCS_API_KEY
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
            finish()
        }
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }
}