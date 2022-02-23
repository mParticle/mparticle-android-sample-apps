package com.mparticle.example.higgsshopsampleapp.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.example.higgsshopsampleapp.repositories.network.ProductsRepository
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    val detailResponseLiveData = MutableLiveData<Product>()
    private val TAG = "ProductDetailViewModel"

    private val repository = ProductsRepository()

    fun getProductById(context: Context, id : Int) {
        viewModelScope.launch {
            detailResponseLiveData.value = repository.getProductById(context, id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        //repository.cancelCoroutinesJob()
    }
}