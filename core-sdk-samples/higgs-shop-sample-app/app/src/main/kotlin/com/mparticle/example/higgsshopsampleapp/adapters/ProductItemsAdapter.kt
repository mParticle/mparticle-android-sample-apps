package com.mparticle.example.higgsshopsampleapp.adapters

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
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product

class ProductItemsAdapter() :
    RecyclerView.Adapter<ProductItemsAdapter.ProductItemViewHolder>() {

    var onItemClicked: ((Product, Int) -> Unit)? = null
    var list = mutableListOf<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_product_item, parent, false)
        return ProductItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: ProductItemViewHolder, position: Int) {
        viewHolder.tvLabel.text = list[position].label

        Glide.with(viewHolder.itemView.context)
            .load(
                Uri.parse("file:///android_asset" + list[position].imageUrl))
            .placeholder(R.drawable.product_image_placeholder)
            .override(343,264)
            .centerCrop()
            .into(viewHolder.ivImage)
    }

    inner class ProductItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val intent: Intent? = null
        var ivImage: ImageView
        var tvLabel: TextView
        init {
            tvLabel = itemView.findViewById(R.id.tv_product_item_label)
            ivImage = itemView.findViewById(R.id.product_item_picture)

            itemView.setOnClickListener {
                onItemClicked?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }
    }
}