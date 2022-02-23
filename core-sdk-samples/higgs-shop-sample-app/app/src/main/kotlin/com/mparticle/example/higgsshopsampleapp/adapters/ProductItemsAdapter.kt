package com.mparticle.example.higgsshopsampleapp.adapters

import android.content.Intent
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.ProductDetailActivity
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import com.mparticle.example.higgsshopsampleapp.utils.Constants

class ProductItemsAdapter(val list: List<Product>) :
    RecyclerView.Adapter<ProductItemsAdapter.ProductItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_product_item, parent, false)
        return ProductItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: ProductItemViewHolder, position: Int) {
        viewHolder.tvId.text = list[position].id.toString()
        viewHolder.tvLabel.text = list[position].label
    }

    inner class ProductItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val intent: Intent? = null
        //var ivImage: ImageView
        var tvId: TextView
        var tvLabel: TextView
        init {
            tvId = itemView.findViewById(R.id.tv_product_item_id)
            tvLabel = itemView.findViewById(R.id.tv_product_item_label)
            //ivImage = itemView.findViewById(R.id.product_item_picture)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ProductDetailActivity::class.java)
                intent.putExtra(Constants.PRODUCT_ID, tvId.text.toString().toIntOrNull() ?: 0)
                itemView.context.startActivity(intent)
            }
        }
    }
}