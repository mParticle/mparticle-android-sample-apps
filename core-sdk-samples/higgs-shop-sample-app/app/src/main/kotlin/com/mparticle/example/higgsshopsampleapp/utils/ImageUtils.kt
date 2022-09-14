package com.mparticle.example.higgsshopsampleapp.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mparticle.example.higgsshopsampleapp.R


const val DEFAULT_PRODUCT_IMAGE = R.drawable.product_image_placeholder

@SuppressLint("UnrememberedMutableState")
@Composable
fun loadPicture(imageUrl: String?, @DrawableRes defaultImage: Int): MutableState<Bitmap?> {
    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(defaultImage)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

        })

    Glide.with(LocalContext.current)
        .asBitmap()
        .load(Uri.parse("file:///android_asset${imageUrl}"))
        .centerCrop()
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    return bitmapState
}