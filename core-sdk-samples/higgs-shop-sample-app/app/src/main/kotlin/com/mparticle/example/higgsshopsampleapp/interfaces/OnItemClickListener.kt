package com.mparticle.example.higgsshopsampleapp.interfaces

import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product

interface OnItemClickListener {
    fun onItemClicked(item: Product)
}