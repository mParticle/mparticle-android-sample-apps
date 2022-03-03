package com.mparticle.example.higgsshopsampleapp.repositories.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartItems")
data class CartItemEntity (
    @PrimaryKey val sku: String,
    val id: Int,
    val label: String,
    val imageUrl: String?,
    val color: String?,
    val size: String?,
    val price: Double,
    var quantity: Int
)
