package com.mparticle.example.higgsshopsampleapp.adapters

import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mparticle.example.higgsshopsampleapp.R
import java.math.BigDecimal


class CheckoutItemsAdapter() :
    RecyclerView.Adapter<CheckoutItemsAdapter.CheckoutItemViewHolder>() {

    var list = mutableListOf<CartItemEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutItemsAdapter.CheckoutItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkout_product_item, parent, false)
        return CheckoutItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: CheckoutItemsAdapter.CheckoutItemViewHolder, position: Int) {
        viewHolder.tvLabel.text = list[position].label

        viewHolder.tvPrice.text = "$${BigDecimal(list[position].price)
            .setScale(2, BigDecimal.ROUND_HALF_UP)}"
        viewHolder.tvLine1.text = "Qty: ${list[position].quantity}"
        var line2 = ""
        list[position].color?.apply { line2 += "Color: ${list[position].color}" }
        list[position].size?.apply {
            if(line2.isNotEmpty()) { line2 += ", " }
            line2 += " Size: ${list[position].size}"
        }
        viewHolder.tvLine2.text = line2

        Glide.with(viewHolder.itemView.context)
            .load(
                Uri.parse("file:///android_asset" + list[position].imageUrl))
            .placeholder(R.drawable.product_image_placeholder)
            .override(100,56)
            .centerCrop()
            .into(viewHolder.ivImage)
    }

    inner class CheckoutItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val intent: Intent? = null
        var ivImage: ImageView
        var tvLabel: TextView
        var tvPrice: TextView
        var tvLine1: TextView
        var tvLine2: TextView

        init {
            ivImage = itemView.findViewById(R.id.iv_checkout_item_picture)
            tvLabel = itemView.findViewById(R.id.tv_checkout_item_label)
            tvPrice = itemView.findViewById(R.id.tv_checkout_item_price)
            tvLine1 = itemView.findViewById(R.id.tv_checkout_item_line1)
            tvLine2 = itemView.findViewById(R.id.tv_checkout_item_line2)
        }
    }
}