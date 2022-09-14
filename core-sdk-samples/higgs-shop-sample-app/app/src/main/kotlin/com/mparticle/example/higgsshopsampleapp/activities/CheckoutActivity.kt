package com.mparticle.example.higgsshopsampleapp.activities

import android.app.ActionBar
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MParticle
import com.mparticle.commerce.CommerceEvent
import com.mparticle.commerce.Product
import com.mparticle.commerce.TransactionAttributes
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.fragments.adapters.CartItemCard
import com.mparticle.example.higgsshopsampleapp.utils.theme.*
import com.mparticle.example.higgsshopsampleapp.databinding.ActivityCheckoutBinding
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import com.mparticle.example.higgsshopsampleapp.viewmodels.CheckoutViewModel
import java.math.BigDecimal
import java.util.*


class CheckoutActivity : AppCompatActivity() {
    private val TAG = "CheckoutActivity"
    lateinit var binding: ActivityCheckoutBinding
    private lateinit var checkoutViewModel: CheckoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_mParticle_SampleApp)
        binding =
            DataBindingUtil.setContentView<ActivityCheckoutBinding>(
                this,
                R.layout.activity_checkout
            )
                .apply {
                    composeView.setContent {
                        CheckoutActivityComposable()
                    }
                }

        MParticle.getInstance()?.logScreen("Checkout")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.checkout_back)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_4079FE)))

        checkoutViewModel =
            ViewModelProvider(this@CheckoutActivity).get(CheckoutViewModel::class.java)

        checkoutViewModel.checkoutPriceLiveData.observe(
            this@CheckoutActivity
        ) { priceMap ->
            val event = commerceEventConversion(priceMap, Product.CHECKOUT)
            MParticle.getInstance()?.logEvent(event)
        }

        checkoutViewModel.cartResponseLiveData.observe(
            this@CheckoutActivity
        ) { items ->
            Log.d(TAG, "Size: " + items?.size)
            checkoutViewModel.getCheckoutPrices()
        }

        checkoutViewModel.checkOutLiveData.observe(this) { checkedOut ->
            if (checkedOut) {
                showPurchaseAlert()
            }
        }

        checkoutViewModel.getCartItems()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun showPurchaseAlert() {
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.checkout_thanks),
            Snackbar.LENGTH_INDEFINITE
        )
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)
        snackbar.view.layoutParams = layoutParams
        snackbar.setBackgroundTint(getColor(R.color.white))
        snackbar.setTextColor(getColor(R.color.black))
        snackbar.setActionTextColor(getColor(R.color.blue_4079FE))
        snackbar.view.setPadding(20, 10, 20, 0)
        (snackbar.view.findViewById<TextView>(R.id.snackbar_text))?.textAlignment =
            View.TEXT_ALIGNMENT_TEXT_START
        val snackbarActionTextView =
            snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_action) as TextView
        snackbarActionTextView.isAllCaps = false
        snackbarActionTextView.setTypeface(snackbarActionTextView.typeface, Typeface.BOLD)
        snackbar.setAction("Exit") {
            setResult(Constants.RESULT_CODE_PURCHASE)
            finish()
        }
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }

    private fun commerceEventConversion(
        priceMap: Map<String, Any>,
        productAction: String
    ): CommerceEvent {
        val cartItems = priceMap["cartItems"] as List<CartItemEntity>
        var entity = cartItems[0]
        val product =
            Product.Builder(entity.label, entity.id.toString(), entity.price.toDouble())
                .customAttributes(
                    mapOf(
                        "size" to entity.size,
                        "color" to entity.color
                    )
                )
                .quantity(entity.quantity.toDouble())
                .build()
        val event = CommerceEvent.Builder(productAction, product)
        for (i in 1 until cartItems.size) {
            entity = cartItems[i]
            val product2 =
                Product.Builder(entity.label, entity.id.toString(), entity.price.toDouble())
                    .customAttributes(
                        mapOf(
                            "size" to entity.size,
                            "color" to entity.color
                        )
                    )
                    .quantity(entity.quantity.toDouble())
                    .build()
            event.addProduct(product2)
        }

        if (productAction == Product.PURCHASE) {
            val attributes: TransactionAttributes =
                TransactionAttributes(Calendar.getInstance().time.toString())
                    .setRevenue(priceMap["grandTotal"].toString().toDouble())
                    .setTax(priceMap["salesTax"].toString().toDouble())
                    .setShipping(BigDecimal(Constants.CHECKOUT_SHIPPING_COST).toDouble())
            event.transactionAttributes(attributes)
        }

        return event.build()
    }

    @Composable
    fun CheckoutActivityComposable() {
        val priceMap by checkoutViewModel.checkoutPriceLiveData.observeAsState()
        val items by checkoutViewModel.cartResponseLiveData.observeAsState()

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .padding(start = 40.dp, end = 40.dp)
        ) {
            item {
                Text(
                    stringResource(id = R.string.checkout_title),
                    style = typography.h1,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(top = 30.dp)
                )
                Text(
                    stringResource(id = R.string.checkout_header1),
                    style = typography.h4,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(top = 30.dp)
                )
                Text(
                    stringResource(id = R.string.checkout_disclaimer),
                    style = typography.h3,
                    color = gray_999999,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp)
                )
                OutlinedTextField(
                    value = stringResource(R.string.checkout_address_value),
                    textStyle = typography.h5,
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(R.string.checkout_address),
                            color = Color.White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        textColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )

                OutlinedTextField(
                    value = stringResource(R.string.checkout_city_value),
                    textStyle = typography.h5,
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(R.string.checkout_city),
                            color = Color.White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        textColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
                Row(modifier = Modifier.padding(top = 30.dp)) {
                    OutlinedTextField(
                        value = stringResource(R.string.checkout_state_value),
                        textStyle = typography.h5,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.checkout_state),
                                color = Color.White
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            textColor = Color.White,
                        ),
                        modifier = Modifier
                            .width(150.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    OutlinedTextField(
                        value = stringResource(R.string.checkout_zip_value),
                        textStyle = typography.h5,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.checkout_zip),
                                color = Color.White
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            textColor = Color.White,
                        ),
                        modifier = Modifier
                            .width(150.dp)
                    )
                }
                Text(
                    stringResource(id = R.string.checkout_header2),
                    style = typography.h4,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(top = 30.dp)
                )
                Text(
                    stringResource(id = R.string.checkout_disclaimer),
                    style = typography.h3,
                    color = gray_999999,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp)
                )
                OutlinedTextField(
                    value = stringResource(R.string.checkout_creditcard_value),
                    textStyle = typography.h5,
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(R.string.checkout_creditcard),
                            color = Color.White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        textColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
                Row(modifier = Modifier.padding(top = 30.dp)) {
                    OutlinedTextField(
                        value = stringResource(R.string.checkout_expiration_value),
                        textStyle = typography.h5,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.checkout_expiration),
                                color = Color.White
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                                "dropDownIcon",
                                tint = Color.White,
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            textColor = Color.White,
                        ),
                        modifier = Modifier
                            .width(150.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    OutlinedTextField(
                        value = stringResource(R.string.checkout_cvc_value),
                        textStyle = typography.h5,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.checkout_cvc),
                                color = Color.White
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            textColor = Color.White,
                        ),
                        modifier = Modifier
                            .width(150.dp)
                    )
                }
                Text(
                    stringResource(id = R.string.checkout_header3),
                    style = typography.h4,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(
                            top = 50.dp,
                            bottom = 10.dp
                        )
                )
                LazyColumn(modifier = Modifier.height(229.dp)) {
                    items?.let { it ->
                        itemsIndexed(items = it) { _, item ->
                            CartItemCard(item = item, isCheckout = true)
                        }
                    }
                }

                Row(modifier = Modifier.padding(top = 30.dp)) {
                    Text(
                        stringResource(id = R.string.checkout_subtotal_header),
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(start = 5.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "$${priceMap?.get("subTotal")}",
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(end = 5.dp)
                    )
                }
                Divider(
                    thickness = 1.dp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(
                        top = 15.dp,
                        bottom = 15.dp
                    )
                )
                Row {
                    Text(
                        stringResource(id = R.string.checkout_salestax_header),
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(start = 5.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "$${priceMap?.get("salesTax")}",
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(end = 5.dp)
                    )
                }
                Divider(
                    thickness = 1.dp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(
                        top = 15.dp,
                        bottom = 15.dp
                    )
                )
                Row {
                    Text(
                        stringResource(id = R.string.checkout_shipping_header),
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(start = 5.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "$${priceMap?.get("shipping")}",
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(end = 5.dp)
                    )
                }
                Divider(
                    thickness = 1.dp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(
                        top = 15.dp,
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(gray_333333)
                        .height(48.dp)
                ) {
                    Text(
                        stringResource(id = R.string.checkout_grand_header),
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(start = 5.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "$${priceMap?.get("grandTotal")}",
                        style = typography.h2,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(end = 5.dp)
                    )
                }
                Divider(
                    thickness = 1.dp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(
                        bottom = 40.dp
                    )
                )

                Button(
                    onClick = {
                        checkoutViewModel.clearCart()
                    },
                    colors = ButtonDefaults.buttonColors(blue_4079FE),
                    shape = Shapes.small,
                    modifier = Modifier
                        .width(190.dp)
                        .height(50.dp)

                ) {
                    Text(
                        stringResource(R.string.cart_cta),
                        style = MaterialTheme.typography.button,
                        color = Color.White
                    )
                }
                Text(
                    stringResource(R.string.cart_disclaimer),
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier
                        .width(262.dp)
                        .wrapContentHeight()
                        .padding(top = 30.dp, bottom = 100.dp)
                        .alpha(0.6F),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}









