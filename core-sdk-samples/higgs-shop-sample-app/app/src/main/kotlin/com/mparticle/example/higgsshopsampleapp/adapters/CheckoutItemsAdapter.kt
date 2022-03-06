package com.mparticle.example.higgsshopsampleapp.adapters

import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mparticle.example.higgsshopsampleapp.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


class CheckoutItemsAdapter() :
    RecyclerView.Adapter<CheckoutItemsAdapter.CartItemViewHolder>() {

    var list = mutableListOf<CartItemEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_product_item, parent, false)
        return CartItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: CartItemViewHolder, position: Int) {
        viewHolder.tvLabel.text = list[position].label

        viewHolder.tvPrice.text = "$${BigDecimal(list[position].price)
            .setScale(2, BigDecimal.ROUND_HALF_UP)}"
        var quantity = "Qty: ${list[position].quantity}"
        list[position].color?.apply { quantity += ", Color: ${list[position].color}" }
        list[position].size?.apply { quantity += ", Size: ${list[position].size}" }
        viewHolder.tvQuantity.text = quantity

        Glide.with(viewHolder.itemView.context)
            .load(
                Uri.parse("file:///android_asset" + list[position].imageUrl))
            .placeholder(R.drawable.product_image_placeholder)
            .override(100,56)
            .centerCrop()
            .into(viewHolder.ivImage)

        viewHolder.tvRemove.visibility = View.INVISIBLE
    }

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val intent: Intent? = null
        var ivImage: ImageView
        var tvSku: TextView
        var tvLabel: TextView
        var tvPrice: TextView
        var tvQuantity: TextView
        var tvRemove: TextView

        init {
            ivImage = itemView.findViewById(R.id.iv_cart_item_picture)
            tvSku = itemView.findViewById(R.id.tv_cart_item_sku)
            tvLabel = itemView.findViewById(R.id.tv_cart_item_label)
            tvPrice = itemView.findViewById(R.id.tv_cart_item_price)
            tvQuantity = itemView.findViewById(R.id.tv_cart_item_quantity)
            tvRemove = itemView.findViewById(R.id.tv_cart_item_remove)
        }
    }
}