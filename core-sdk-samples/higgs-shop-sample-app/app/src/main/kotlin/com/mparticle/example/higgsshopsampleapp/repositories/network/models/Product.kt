package com.mparticle.example.higgsshopsampleapp.repositories.network.models

data class Product(
    val id: Int,
    val label: String,
    val price: Float,
    val variants: Variants?,
    val description: String
)