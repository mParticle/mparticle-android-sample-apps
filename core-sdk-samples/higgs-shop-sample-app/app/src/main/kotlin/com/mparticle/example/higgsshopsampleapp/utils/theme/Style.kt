package com.mparticle.example.higgsshopsampleapp.utils.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mparticle.example.higgsshopsampleapp.R

val lato = FontFamily(
    listOf(
        Font(R.font.lato_regular, FontWeight.Normal),
        Font(R.font.lato_regular, FontWeight.Medium),
        Font(R.font.lato_regular, FontWeight.SemiBold),
        Font(R.font.lato_regular, FontWeight.Bold),
        Font(R.font.lato_regular, FontWeight.Black),
    )
)

val typography = Typography(
    button = TextStyle(
        color = Color.White,
        fontFamily = lato,
        fontSize = 14.sp,
    ),
    h1 = TextStyle(
        color = Color.White,
        fontFamily = lato,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    h2 = TextStyle(
        color = Color.White,
        fontFamily = lato,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h3 = TextStyle(
        color = Color.White,
        fontFamily = lato,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    h4 = TextStyle(
        color = Color.White,
        fontFamily = lato,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h5 = TextStyle(
        color = Color.White,
        fontFamily = lato,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)