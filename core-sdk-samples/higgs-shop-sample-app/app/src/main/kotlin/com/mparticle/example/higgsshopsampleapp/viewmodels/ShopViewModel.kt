package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.example.higgsshopsampleapp.repositories.ProductsRepository
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import kotlinx.coroutines.launch

class ShopViewModel() : ViewModel() {
    val inventoryResponseLiveData = MutableLiveData<List<Product>>()
    private val TAG = "ProductsViewModel"

    private val repository = ProductsRepository()

    fun getProducts (context: Context) {
        viewModelScope.launch {
            inventoryResponseLiveData.value = repository.getProducts(context)
        }
    }

    override fun onCleared() {
        super.onCleared()
        //repository.cancelCoroutinesJob()
    }
}