package com.mparticle.example.higgsshopsampleapp.activities

import android.app.ActionBar
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MParticle
import com.mparticle.commerce.CommerceEvent
import com.mparticle.commerce.Product
import com.mparticle.commerce.TransactionAttributes
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.adapters.CheckoutItemsAdapter
import com.mparticle.example.higgsshopsampleapp.databinding.ActivityCheckoutBinding
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import com.mparticle.example.higgsshopsampleapp.viewmodels.CheckoutViewModel
import java.math.BigDecimal
import java.util.Calendar


class CheckoutActivity : AppCompatActivity() {
    private val TAG = "CheckoutActivity"
    lateinit var binding: ActivityCheckoutBinding
    private lateinit var checkoutViewModel: CheckoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_mParticle_SampleApp);
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MParticle.getInstance()?.logScreen("Checkout")

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle(R.string.checkout_back)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_4079FE)))

        checkoutViewModel =
            ViewModelProvider(this).get(CheckoutViewModel::class.java)

        binding.rvCartList.layoutManager = LinearLayoutManager(this)

        checkoutViewModel.checkoutPriceLiveData.observe(this
        ) { priceMap ->
            binding.tvPaymentSubtotalPrice.text = "$${priceMap["subTotal"]}"
            binding.tvPaymentTaxPrice.text = "$${priceMap["salesTax"]}"
            binding.tvPaymentShippingPrice.text = "$${priceMap["shipping"]}"
            binding.tvPaymentGrandPrice.text = "$${priceMap["grandTotal"]}"

            var event = commerceEventConversion(priceMap, Product.CHECKOUT)
            MParticle.getInstance()?.logEvent(event)

            val btnCTA = findViewById(R.id.payment_cta) as Button
            btnCTA.setOnClickListener {
                event = commerceEventConversion(priceMap, Product.PURCHASE)
                MParticle.getInstance()?.logEvent(event)
                showPurchaseAlert()
                btnCTA.isEnabled = false
                checkoutViewModel.clearCart(this)
            }
        }

        checkoutViewModel.cartResponseLiveData.observe(this, Observer { items ->
            Log.d(TAG, "Size: " + items?.size)

            if (items == null) {
                return@Observer
            }

            val adapter = CheckoutItemsAdapter()
            adapter.list = items.toMutableList()

            binding.rvCartList.let { listView ->
                if (listView.adapter == null) {
                    listView.adapter = adapter
                } else {
                    adapter.notifyDataSetChanged()
                }
                checkoutViewModel.getCheckoutPrices(this)
            }
        })
        checkoutViewModel.getCartItems(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun showPurchaseAlert() {
        val snackbar = Snackbar.make(binding.root, getString(R.string.checkout_thanks), Snackbar.LENGTH_INDEFINITE)
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)
        snackbar.view.layoutParams = layoutParams
        snackbar.setBackgroundTint(getColor(R.color.blue_4079FE))
        snackbar.setTextColor(getColor(R.color.white))
        snackbar.view.setPadding(0, 10, 0, 0)
        (snackbar.view.findViewById<TextView>(R.id.snackbar_text))?.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.view.setOnClickListener({
            setResult(Constants.RESULT_CODE_PURCHASE)
            finish()
        })
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }

    fun commerceEventConversion(priceMap: Map<String, Any>, productAction: String): CommerceEvent {
        val cartItems = priceMap["cartItems"] as List<CartItemEntity>
        var entity = cartItems.get(0)
        val product = Product.Builder(entity.label, entity.id.toString(), entity.price.toDouble())
            .customAttributes(mapOf(
                "size" to entity.size,
                "color" to entity.color
            ))
            .quantity(entity.quantity.toDouble())
            .build()
        val event = CommerceEvent.Builder(productAction, product)
        for (i in 1 until cartItems.size) {
            entity = cartItems.get(i)
            val product2 = Product.Builder(entity.label, entity.id.toString(), entity.price.toDouble())
                .customAttributes(mapOf(
                    "size" to entity.size,
                    "color" to entity.color
                ))
                .quantity(entity.quantity.toDouble())
                .build()
            event.addProduct(product2)
        }

        if(productAction == Product.PURCHASE) {
            val attributes: TransactionAttributes = TransactionAttributes(Calendar.getInstance().time.toString())
                .setRevenue(priceMap["grandTotal"].toString().toDouble())
                .setTax(priceMap["salesTax"].toString().toDouble())
                .setShipping(BigDecimal(Constants.CHECKOUT_SHIPPING_COST).toDouble())
            event.transactionAttributes(attributes)
        }

        return event.build()
    }
}