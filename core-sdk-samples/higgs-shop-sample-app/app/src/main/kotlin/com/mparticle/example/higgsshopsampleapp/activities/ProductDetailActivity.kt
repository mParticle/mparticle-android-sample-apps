package com.mparticle.example.higgsshopsampleapp.activities

import android.app.ActionBar
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

            val dropdownColors = binding.dropdownColors.editText as? AutoCompleteTextView
            if(productItem.variants?.colors?.isNotEmpty() ?: false) {
                val colorAdapter: ArrayAdapter<*>
                colorAdapter = ArrayAdapter(
                    this,
                    R.layout.shop_detail_spinner_item_popup,
                    productItem.variants?.colors ?: listOf()
                )
                dropdownColors?.setAdapter(colorAdapter)
                dropdownColors?.setOnClickListener {
                    dropdownColors.showDropDown()
                }
                dropdownColors?.setOnItemClickListener {  _, _, position, _ ->
                    detailViewModel.color = colorAdapter.getItem(position).toString()
                }
                detailViewModel.color = colorAdapter.getItem(0).toString()
                dropdownColors?.setText(detailViewModel.color, false)
                binding.dropdownColors.visibility = View.VISIBLE
            } else {
                binding.dropdownColors.visibility = View.GONE
            }

            val dropdownSizes = binding.dropdownSizes.editText as? AutoCompleteTextView
            if(productItem.variants?.sizes?.isNotEmpty() ?: false) {
                val sizeAdapter: ArrayAdapter<*>
                sizeAdapter = ArrayAdapter(
                    this,
                    R.layout.shop_detail_spinner_item_popup,
                    productItem.variants?.sizes ?: listOf()
                )
                dropdownSizes?.setAdapter(sizeAdapter)
                dropdownSizes?.setOnClickListener {
                    dropdownSizes.showDropDown()
                }
                dropdownSizes?.setOnItemClickListener {  _, _, position, _ ->
                    detailViewModel.size = sizeAdapter.getItem(position).toString()
                }
                detailViewModel.size = sizeAdapter.getItem(0).toString()
                dropdownSizes?.setText(detailViewModel.size, false)
                binding.dropdownSizes.visibility = View.VISIBLE
            } else {
                binding.dropdownSizes.visibility = View.GONE
            }

            val quantityAdapter: ArrayAdapter<*>
            quantityAdapter = ArrayAdapter(
                this,
                R.layout.shop_detail_spinner_item_popup,
                listOf("1","2","3","4","5","6","7","8")
            )
            val dropdownQuantity = binding.dropdownQuantity.editText as? AutoCompleteTextView
            dropdownQuantity?.setAdapter(quantityAdapter)
            dropdownQuantity?.setOnClickListener {
                dropdownQuantity.showDropDown()
            }
            dropdownQuantity?.setOnItemClickListener {  _, _, position, _ ->
                detailViewModel.quantity = Integer.parseInt(quantityAdapter.getItem(position).toString())
            }
            detailViewModel.quantity = Integer.parseInt(quantityAdapter.getItem(0).toString())
            dropdownQuantity?.setText(detailViewModel.quantity.toString(), false)

            binding.detailCta.setOnClickListener {
                val sku = "${productItem.id}-${detailViewModel.color}-${detailViewModel.size}"
                val entity = CartItemEntity(
                    sku = sku,
                    id = productItem.id,
                    label = productItem.label,
                    imageUrl = productItem.imageUrl,
                    color = detailViewModel.color,
                    size = detailViewModel.size,
                    price = productItem.price,
                    quantity = detailViewModel.quantity
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
        snackbar.setBackgroundTint(getColor(R.color.white))
        snackbar.setTextColor(getColor(R.color.black))
        snackbar.view.setPadding(20, 10, 20, 0)
        (snackbar.view.findViewById<TextView>(R.id.snackbar_text))?.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        snackbar.setActionTextColor(getColor(R.color.blue_4079FE))
        val snackbarActionTextView =
            snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_action) as TextView
        snackbarActionTextView.setAllCaps(false)
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
        snackbar.setAction(getString(R.string.detail_cta_cart)) {
            setResult(Constants.RESULT_CODE_CART_ADDED)
            finish()
        }
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }
}