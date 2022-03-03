package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    val cartResponseLiveData = MutableLiveData<List<CartItemEntity>>()
    val cartSubtotalPriceLiveData = MutableLiveData<Double>()
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
            var amount = 0.0
            items.forEach {
                amount += it.quantity *  it.price
            }
            cartSubtotalPriceLiveData.value = amount
        }
    }

    fun removeFromCart(context: Context, entity: CartItemEntity) {
        viewModelScope.launch {
            val rowsAffected = cartRepository.removeFromCart(context, entity)
            if (rowsAffected > 0) {
                Log.d(TAG, "Publish Success")
                cartResponseLiveData.value = cartRepository.getCartItems(context)
            } else {
                Log.d(TAG, "Publish Fail")
                cartResponseLiveData.value = cartRepository.getCartItems(context)
            }
        }
    }
}