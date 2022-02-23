package com.mparticle.example.higgsshopsampleapp.activities

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.databinding.ActivityDetailBinding
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import com.mparticle.example.higgsshopsampleapp.viewmodels.ProductDetailViewModel

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
        getSupportActionBar()?.setTitle("Back")
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_4079FE)))

        val productId = intent.getIntExtra(Constants.PRODUCT_ID, 0)

        detailViewModel =
            ViewModelProvider(this).get(ProductDetailViewModel::class.java)

        detailViewModel.detailResponseLiveData.observe(this, Observer { product ->
            Log.d(TAG, "Show Product ID: " + product?.id)

            if (product == null) {
                return@Observer
            }

            binding.tvDetailHeader.text = product.label
            binding.tvDetailPrice.text = "\$${"%.2f".format(product.price)}"

            if(product.variants?.colors?.isNotEmpty() ?: false) {
                val arrayAdapter: ArrayAdapter<*>
                arrayAdapter = ArrayAdapter(
                    this,
                    R.layout.shop_detail_spinner_item,
                    listOf("Select Color") + (product.variants?.colors ?: listOf("Red"))
                )
                arrayAdapter.setDropDownViewResource(R.layout.shop_detail_spinner_item_popup)
                binding.spinnerColors.visibility = View.VISIBLE
                binding.spinnerColors.adapter = arrayAdapter
            } else {
                binding.spinnerColors.visibility = View.GONE
            }

            if(product.variants?.sizes?.isNotEmpty() ?: false) {
                val arrayAdapter: ArrayAdapter<String>
                arrayAdapter = ArrayAdapter(
                    this,
                    R.layout.shop_detail_spinner_item,
                    listOf("Select Size") + (product.variants?.sizes ?: listOf("M"))
                )
                arrayAdapter.setDropDownViewResource(R.layout.shop_detail_spinner_item_popup)
                binding.spinnerSizes.visibility = View.VISIBLE
                binding.spinnerSizes.adapter = arrayAdapter
            } else {
                binding.spinnerSizes.visibility = View.GONE
            }
        })
        detailViewModel.getProductById(this, productId)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}