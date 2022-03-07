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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class CartItemsAdapter() :
    RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder>() {

    var onItemClicked: ((CartItemEntity, Int) -> Unit)? = null
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

        var line1 = "Qty: ${list[position].quantity}"
        list[position].color?.apply { line1 += ", Color: ${list[position].color}" }
        list[position].size?.apply { line1 += ", Size: ${list[position].size}" }
        viewHolder.tvLine1.text = line1

        Glide.with(viewHolder.itemView.context)
            .load(
                Uri.parse("file:///android_asset" + list[position].imageUrl))
            .placeholder(R.drawable.product_image_placeholder)
            .override(100,56)
            .centerCrop()
            .into(viewHolder.ivImage)

    }

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val intent: Intent? = null
        var ivImage: ImageView
        var tvLabel: TextView
        var tvPrice: TextView
        var tvLine1: TextView
        var tvLine2: TextView

        init {
            ivImage = itemView.findViewById(R.id.iv_cart_item_picture)
            tvLabel = itemView.findViewById(R.id.tv_cart_item_label)
            tvPrice = itemView.findViewById(R.id.tv_cart_item_price)
            tvLine1 = itemView.findViewById(R.id.tv_cart_item_line1)
            tvLine2 = itemView.findViewById(R.id.tv_cart_item_line2)

            itemView.setOnClickListener {
                onItemClicked?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }
    }
}