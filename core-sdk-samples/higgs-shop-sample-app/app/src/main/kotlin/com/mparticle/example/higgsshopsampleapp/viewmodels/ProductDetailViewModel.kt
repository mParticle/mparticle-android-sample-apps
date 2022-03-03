package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.ProductsRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    val detailResponseLiveData = MutableLiveData<Product>()
    val cartResponseLiveData = MutableLiveData<Boolean>()
    private val TAG = "ProductDetailViewModel"

    private val productsRepository = ProductsRepository()
    private val cartRepository = CartRepository()

    fun getProductById(context: Context, id : Int) {
        viewModelScope.launch {
            detailResponseLiveData.value = productsRepository.getProductById(context, id)
        }
    }

    fun addToCart(context: Context, cartItem : CartItemEntity) {
        viewModelScope.launch {
            val rowsAffected = cartRepository.addToCart(context, cartItem)
            if (rowsAffected > 0) {
                Log.d(TAG, "Publish Success")
                cartResponseLiveData.value = true
            } else {
                Log.d(TAG, "Publish Fail")
                cartResponseLiveData.value = false
            }
        }
    }

}