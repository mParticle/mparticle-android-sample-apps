package com.mparticle.example.higgsshopsampleapp.repositories.database.daos

import androidx.room.*
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity

@Dao
interface CartDao {

    @Query("SELECT * FROM CartItems ORDER BY sku desc")
    fun getAllCartItems(): List<CartItemEntity>

    @Query("SELECT * FROM CartItems WHERE sku=:sku ORDER BY sku desc")
    fun getCartItemByKey(sku: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(CartItemEntity: CartItemEntity): Long

    @Delete
    suspend fun removeFromCart(CartItemEntity: CartItemEntity): Int
}