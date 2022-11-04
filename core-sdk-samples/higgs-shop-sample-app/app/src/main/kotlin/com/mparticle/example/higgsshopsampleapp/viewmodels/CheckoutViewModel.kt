package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    val cartResponseLiveData = MutableLiveData<List<CartItemEntity>>()
    val checkoutPriceLiveData = MutableLiveData<Map<String, Any>>()
    val checkOutLiveData = MutableLiveData<Boolean>()
    private val cartRepository = CartRepository()
    private val TAG = "PaymentViewModel"

    fun getCartItems() {
        viewModelScope.launch {
            cartResponseLiveData.value = cartRepository.getCartItems(getApplication())
        }
    }

    fun getCheckoutPrices() {
        viewModelScope.launch {
            val items = cartRepository.getCartItems(getApplication())
            var subTotal = "0.0".toBigDecimal()
            items.forEach {
                subTotal += BigDecimal(it.quantity.toString()) * BigDecimal(it.price)
            }
            val salesTax =
                subTotal * BigDecimal(Constants.CHECKOUT_SALES_TAX).divide("100.0".toBigDecimal())
            val shipping =
                subTotal * BigDecimal(Constants.CHECKOUT_SHIPPING_COST).divide("100.0".toBigDecimal())
            val grandTotal = subTotal + salesTax + shipping
            checkoutPriceLiveData.value = mapOf(
                "subTotal" to subTotal.setScale(2, RoundingMode.HALF_UP),
                "salesTax" to salesTax.setScale(2, RoundingMode.HALF_UP),
                "shipping" to shipping.setScale(2, RoundingMode.HALF_UP),
                "grandTotal" to grandTotal.setScale(2, RoundingMode.HALF_UP),
                "cartItems" to items
            )
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            val rowsAffected = cartRepository.clearCart(getApplication())
            if (rowsAffected > 0) {
                Log.d(TAG, "Publish Success")
                checkOutLiveData.value = true
            } else {
                Log.d(TAG, "Publish Fail")
                checkOutLiveData.value = false
            }
        }
    }
}