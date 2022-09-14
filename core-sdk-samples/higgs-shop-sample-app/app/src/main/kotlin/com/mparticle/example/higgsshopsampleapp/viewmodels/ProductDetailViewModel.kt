package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mparticle.MParticle
import com.mparticle.commerce.CommerceEvent
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.ProductsRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import kotlinx.coroutines.launch


class ProductDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "ProductDetailViewModel"
    val detailResponseLiveData = MutableLiveData<Product>()
    val cartResponseLiveData = MutableLiveData<Boolean>()
    var quantity: Int = 0
    var color: String? = null
    var size: String? = null

    private val productsRepository = ProductsRepository()
    private val cartRepository = CartRepository()

    fun getProductById(id: Int) {
        viewModelScope.launch {
            detailResponseLiveData.value = productsRepository.getProductById(getApplication(), id)
        }
    }

    fun addToCart(cartItem: CartItemEntity) {
        viewModelScope.launch {
            val rowsAffected = cartRepository.addToCart(getApplication(), cartItem)
            if (rowsAffected > 0) {
                Log.d(TAG, "Publish Success")
                val product = com.mparticle.commerce.Product.Builder(
                    cartItem.label,
                    cartItem.id.toString(),
                    cartItem.price.toDouble()
                )
                    .customAttributes(
                        mapOf(
                            "size" to cartItem.size,
                            "color" to cartItem.color
                        )
                    )
                    .unitPrice(cartItem.price.toDouble())
                    .quantity(cartItem.quantity.toDouble())
                    .build()
                val event =
                    CommerceEvent.Builder(com.mparticle.commerce.Product.ADD_TO_CART, product)
                        .build()
                MParticle.getInstance()?.logEvent(event)
                cartResponseLiveData.value = true
            } else {
                Log.d(TAG, "Publish Fail")
                cartResponseLiveData.value = false
            }
        }
    }

}