package com.mparticle.example.higgsshopsampleapp.activities

import android.app.ActionBar
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MParticle
import com.mparticle.commerce.CommerceEvent
import com.mparticle.commerce.Product
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.databinding.ActivityDetailBinding
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import com.mparticle.example.higgsshopsampleapp.viewmodels.ProductDetailViewModel
import java.math.BigDecimal

class ProductDetailActivity : AppCompatActivity() {
    private val TAG = "ProductDetailActivity"
    lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_mParticle_SampleApp);
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MParticle.getInstance()?.logScreen("Detail")

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle(R.string.detail_back)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_4079FE)))

        val productId = intent.getIntExtra(Constants.PRODUCT_ID, 0)

        detailViewModel =
            ViewModelProvider(this).get(ProductDetailViewModel::class.java)

        detailViewModel.cartResponseLiveData.observe(this, { cartAdded ->
            if(cartAdded) {
                showAddToCartAlert()
            }
        })

        detailViewModel.detailResponseLiveData.observe(this, Observer { productItem ->
            Log.d(TAG, "Show Product ID: " + productItem?.id)

            if (productItem == null) {
                finish()
                return@Observer
            }

            val product = Product.Builder(productItem.label, productItem.id.toString(), productItem.price.toDouble())
                .unitPrice(productItem.price.toDouble())
                .build()
            val event = CommerceEvent.Builder(Product.DETAIL, product)
                .build()
            MParticle.getInstance()?.logEvent(event)

            binding.tvDetailHeader.text = productItem.label
            binding.tvDetailPrice.text = "\$${BigDecimal(productItem.price)
                .setScale(2, BigDecimal.ROUND_HALF_UP)}"

            Glide.with(this)
                .load(
                    Uri.parse("file:///android_asset" + productItem.imageUrl))
                .placeholder(R.drawable.product_image_placeholder)
                //.override(328,264)
                .centerCrop()
                .into(binding.ivDetailImage)

            if(productItem.variants?.colors?.isNotEmpty() ?: false) {
                val arrayAdapter: ArrayAdapter<*>
                arrayAdapter = ArrayAdapter(
                    this,
                    R.layout.shop_detail_spinner_item,
                    productItem.variants?.colors ?: listOf()
                )
                arrayAdapter.setDropDownViewResource(R.layout.shop_detail_spinner_item_popup)
                binding.spinnerColors.visibility = View.VISIBLE
                binding.spinnerColors.adapter = arrayAdapter
            } else {
                binding.spinnerColors.visibility = View.GONE
            }

            if(productItem.variants?.sizes?.isNotEmpty() ?: false) {
                val arrayAdapter: ArrayAdapter<*>
                arrayAdapter = ArrayAdapter(
                    this,
                    R.layout.shop_detail_spinner_item,
                    productItem.variants?.sizes ?: listOf()
                )
                arrayAdapter.setDropDownViewResource(R.layout.shop_detail_spinner_item_popup)
                binding.spinnerSizes.visibility = View.VISIBLE
                binding.spinnerSizes.adapter = arrayAdapter
            } else {
                binding.spinnerSizes.visibility = View.GONE
            }

            val arrayAdapter: ArrayAdapter<*>
            arrayAdapter = ArrayAdapter(
                this,
                R.layout.shop_detail_spinner_item,
                listOf("1","2","3","4","5","6","7","8")
            )
            arrayAdapter.setDropDownViewResource(R.layout.shop_detail_spinner_item_popup)
            binding.spinnerQty.adapter = arrayAdapter

            binding.detailCta.setOnClickListener {
                val sku = "${productItem.id}-${binding.spinnerColors.selectedItem}-${binding.spinnerSizes.selectedItem}"
                val entity = CartItemEntity(
                    sku = sku,
                    id = productItem.id,
                    label = productItem.label,
                    imageUrl = productItem.imageUrl,
                    color = binding.spinnerColors.selectedItem?.toString(),
                    size = binding.spinnerSizes.selectedItem?.toString(),
                    price = productItem.price,
                    quantity = Integer.parseInt(binding.spinnerQty.selectedItem.toString())
                )
                detailViewModel.addToCart(this.applicationContext, entity)
            }
        })
        detailViewModel.getProductById(this, productId)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun showAddToCartAlert() {
        val snackbar = Snackbar.make(binding.root, getString(R.string.detail_cta_added), Snackbar.LENGTH_LONG)
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)
        snackbar.view.layoutParams = layoutParams
        snackbar.setBackgroundTint(getColor(R.color.blue_4079FE))
        snackbar.setTextColor(getColor(R.color.white))
        snackbar.view.setPadding(0, 10, 0, 0)
        (snackbar.view.findViewById<TextView>(R.id.snackbar_text))?.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.view.setOnClickListener({
            setResult(Constants.RESULT_CODE_CART_ADDED)
            finish()
        })
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }
}