package com.mparticle.example.higgsshopsampleapp.activities

import android.app.ActionBar
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MPEvent
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.BuildConfig
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.utils.theme.Shapes
import com.mparticle.example.higgsshopsampleapp.utils.theme.blue_4079FE
import com.mparticle.example.higgsshopsampleapp.databinding.ActivityLandingBinding
import com.mparticle.example.higgsshopsampleapp.utils.Constants

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLandingBinding?>(this, R.layout.activity_landing)
            .apply {
                composeView.setContent {
                    LandingActivityComposable()
                }
            }

        MParticle.getInstance()?.logScreen("Landing")
    }

    private fun onClick(): Boolean {
        return if (hasApiKey()) {
            val event = MPEvent.Builder("Landing Button Click", MParticle.EventType.Other)
                .build()
            MParticle.getInstance()?.logEvent(event)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true

        } else {
            showBlankAPIKeyAlert()
            false
        }
    }

    private fun hasApiKey(): Boolean {
        return !(BuildConfig.HIGGS_SHOP_SAMPLE_APP_KEY.isNullOrBlank()
                || BuildConfig.HIGGS_SHOP_SAMPLE_APP_SECRET.isNullOrBlank())
    }

    private fun showBlankAPIKeyAlert() {
        val parentLayout: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(
            parentLayout,
            getString(R.string.landing_apikey_alert),
            Snackbar.LENGTH_INDEFINITE
        )
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
        snackbarActionTextView.isAllCaps = false
        snackbarActionTextView.setTypeface(snackbarActionTextView.typeface, Typeface.BOLD)
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

    @Composable
    fun LandingActivityComposable() {
        Image(
            painter = painterResource(id = R.drawable.background_gradient),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_splash_logo),
                contentDescription = "splashLogo",
                tint = Color.Unspecified,
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .padding(top = 123.dp)
            )
            Text(
                stringResource(R.string.landing_welcome),
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(282.dp)
                    .wrapContentHeight()
            )
            Button(
                onClick = {
                    if (onClick()) {
                        Modifier.alpha(1.0f)
                    } else {
                        Modifier.alpha(0.3f)
                    }
                },
                colors = ButtonDefaults.buttonColors(blue_4079FE),
                shape = Shapes.small,
                modifier = Modifier
                    .width(190.dp)
                    .height(50.dp)

            )
            {
                Text(
                    stringResource(R.string.landing_cta),
                    style = MaterialTheme.typography.button,
                    color = Color.White
                )
            }
            Text(
                stringResource(R.string.landing_disclaimer),
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,

                modifier = Modifier
                    .width(262.dp)
                    .wrapContentHeight()
                    .padding(bottom = 100.dp)
                    .alpha(0.6F)
            )
        }
    }
}