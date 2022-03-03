package com.mparticle.example.higgsshopsampleapp.repositories

import android.content.Context
import com.mparticle.example.higgsshopsampleapp.repositories.database.MpDatabase
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository() {
    val TAG = "CartRepository"

    suspend fun getCartItems(context: Context) : List<CartItemEntity> {
        var cartItems: List<CartItemEntity>
        withContext(Dispatchers.IO) {
            val db = MpDatabase.getDatabase(context)
            val cartDao = db.mpDao()
            cartItems = cartDao.getAllCartItems()
        }
        return cartItems
    }

    suspend fun addToCart(context: Context, entity: CartItemEntity) = withContext(Dispatchers.IO) {
        val db = MpDatabase.getDatabase(context)
        val mpDao = db.mpDao()
        val sku = "${entity.id}-${entity.color}-${entity.size}"
        val entityExists = mpDao.getCartItemByKey(sku)
        entityExists?.let { entity.quantity += entityExists.quantity }
        val rowsAffected = mpDao.addToCart(entity)
        rowsAffected
    }

    suspend fun removeFromCart(context: Context, entity: CartItemEntity) = withContext(Dispatchers.IO) {
        val db = MpDatabase.getDatabase(context)
        val mpDao = db.mpDao()
        val rowsAffected = mpDao.removeFromCart(entity)
        rowsAffected
    }
}