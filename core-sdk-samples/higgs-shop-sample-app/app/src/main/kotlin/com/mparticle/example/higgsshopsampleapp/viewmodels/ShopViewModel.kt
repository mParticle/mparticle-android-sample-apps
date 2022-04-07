package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.ProductsRepository
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import kotlinx.coroutines.launch

class ShopViewModel() : ViewModel() {
    private val TAG = "ProductsViewModel"

    val inventoryResponseLiveData = MutableLiveData<List<Product>>()
    val cartTotalSizeResponseLiveData = MutableLiveData<Int>()

    private val cartRepository = CartRepository()
    private val repository = ProductsRepository()

    fun getProducts (context: Context) {
        viewModelScope.launch {
            inventoryResponseLiveData.value = repository.getProducts(context)
        }
    }

    fun getTotalCartItems(context: Context) {
        viewModelScope.launch {
            val items = cartRepository.getCartItems(context)
            var total = 0
            items.forEach {
                total += it.quantity
            }
            cartTotalSizeResponseLiveData.value = total
        }
    }
}