package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CheckoutViewModel : ViewModel() {
    val cartResponseLiveData = MutableLiveData<List<CartItemEntity>>()
    val checkoutPriceLiveData = MutableLiveData<Map<String, Any>>()
    private val cartRepository = CartRepository()
    private val TAG = "PaymentViewModel"

    fun getCartItems(context: Context) {
        viewModelScope.launch {
            cartResponseLiveData.value = cartRepository.getCartItems(context)
        }
    }

    fun getCheckoutPrices(context: Context) {
        viewModelScope.launch {
            val items = cartRepository.getCartItems(context)
            var subTotal = "0.0".toBigDecimal()
            items.forEach {
                subTotal += BigDecimal(it.quantity.toString()) * BigDecimal(it.price)
            }
            val salesTax = subTotal * BigDecimal(Constants.CHECKOUT_SALES_TAX).divide("100.0".toBigDecimal())
            val shipping = subTotal * BigDecimal(Constants.CHECKOUT_SHIPPING_COST).divide("100.0".toBigDecimal())
            val grandTotal = subTotal + salesTax + shipping
            checkoutPriceLiveData.value = mapOf(
                "subTotal" to subTotal.setScale(2, BigDecimal.ROUND_HALF_UP),
                "salesTax" to salesTax.setScale(2, BigDecimal.ROUND_HALF_UP),
                "shipping" to shipping.setScale(2, BigDecimal.ROUND_HALF_UP),
                "grandTotal" to grandTotal.setScale(2, BigDecimal.ROUND_HALF_UP),
                "cartItems" to items
            )
        }
    }

    fun clearCart(context: Context) {
        viewModelScope.launch {
            val rowsAffected = cartRepository.clearCart(context)
            if (rowsAffected > 0) {
                Log.d(TAG, "Publish Success")
            } else {
                Log.d(TAG, "Publish Fail")
            }
        }
    }
}