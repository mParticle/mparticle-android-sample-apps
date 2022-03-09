package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.MParticle
import com.mparticle.commerce.CommerceEvent
import com.mparticle.commerce.Product
import com.mparticle.commerce.TransactionAttributes
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import kotlinx.coroutines.launch
import java.math.BigDecimal


class CartViewModel : ViewModel() {
    val cartResponseLiveData = MutableLiveData<List<CartItemEntity>>()
    val cartSubtotalPriceLiveData = MutableLiveData<BigDecimal>()
    private val cartRepository = CartRepository()
    private val TAG = "CartViewModel"

    fun getCartItems(context: Context) {
        viewModelScope.launch {
            cartResponseLiveData.value = cartRepository.getCartItems(context)
        }
    }

    fun getSubtotalPrice(context: Context) {
        viewModelScope.launch {
            val items = cartRepository.getCartItems(context)
            var amount = BigDecimal("0.0")
            items.forEach {
                amount += BigDecimal(it.quantity.toString()) * BigDecimal(it.price)
            }
            cartSubtotalPriceLiveData.value = amount.setScale(2, BigDecimal.ROUND_HALF_UP)
        }
    }

    fun removeFromCart(context: Context, entity: CartItemEntity) {
        viewModelScope.launch {
            val rowsAffected = cartRepository.removeFromCart(context, entity)
            if (rowsAffected > 0) {
                val product = Product.Builder(entity.label, entity.id.toString(), entity.price.toDouble())
                    .customAttributes(mapOf(
                        "size" to entity.size,
                        "color" to entity.color
                    ))
                    .quantity(entity.quantity.toDouble())
                    .build()
                val event = CommerceEvent.Builder(Product.REMOVE_FROM_CART, product)
                    .build()
                MParticle.getInstance()?.logEvent(event)
                Log.d(TAG, "Publish Success")
                cartResponseLiveData.value = cartRepository.getCartItems(context)
            } else {
                Log.d(TAG, "Publish Fail")
                cartResponseLiveData.value = cartRepository.getCartItems(context)
            }
        }
    }
}