package com.mparticle.example.higgsshopsampleapp.repositories.network.models

data class Product(
    val id: Int,
    val label: String,
    val imageUrl: String?,
    val price: Double,
    val variants: Variants?,
    val description: String
)