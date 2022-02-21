package com.mparticle.example.higgsshopsampleapp.repositories.network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Products
import java.io.IOException


class ProductsRepository()  {
    val TAG = "ProductsRepository"

    fun getProductById(context: Context, id: Int) : Product? {
        val products = getProducts(context)
        products.forEach {
            if (it.id == id)
                return it
        }
        return null
    }

    fun getProducts(context: Context) : List<Product> {
        val jsonFileString = getJsonDataFromAsset(context, "products.json")
        Log.i(TAG, jsonFileString ?: "Could not find local products.json")
        val listProductType = object : TypeToken<Products>() {}.type
        var products: Products = Gson().fromJson(jsonFileString, listProductType)
        return products.products
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
