package com.mparticle.example.higgsshopsampleapp.activities

import android.app.ActionBar
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MParticle
import com.mparticle.commerce.CommerceEvent
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.utils.theme.Shapes
import com.mparticle.example.higgsshopsampleapp.utils.theme.blue_4079FE
import com.mparticle.example.higgsshopsampleapp.utils.theme.typography
import com.mparticle.example.higgsshopsampleapp.databinding.ActivityDetailBinding
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import com.mparticle.example.higgsshopsampleapp.utils.DEFAULT_PRODUCT_IMAGE
import com.mparticle.example.higgsshopsampleapp.utils.loadPicture
import com.mparticle.example.higgsshopsampleapp.viewmodels.ProductDetailViewModel

class ProductDetailActivity : AppCompatActivity() {
    private val TAG = "ProductDetailActivity"
    lateinit var binding: ActivityDetailBinding
    private lateinit var productDetailViewModel: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_mParticle_SampleApp)
        binding =
            DataBindingUtil.setContentView<ActivityDetailBinding>(
                this,
                R.layout.activity_detail
            )
                .apply {
                    composeView.setContent {
                        ProductDetailActivityComposable(productDetailViewModel)
                    }
                }

        MParticle.getInstance()?.logScreen("Detail")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.detail_back)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_4079FE)))

        val productId = intent.getIntExtra(Constants.PRODUCT_ID, 0)

        productDetailViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)

        productDetailViewModel.cartResponseLiveData.observe(this) { cartAdded ->
            if (cartAdded) {
                showAddToCartAlert()
            }
        }

        productDetailViewModel.detailResponseLiveData.observe(this, Observer { productItem ->
            Log.d(TAG, "Show Product ID: " + productItem?.id)

            if (productItem == null) {
                finish()
                return@Observer
            }

            val mProduct = com.mparticle.commerce.Product.Builder(
                productItem.label,
                productItem.id.toString(),
                productItem.price.toDouble()
            )
                .unitPrice(productItem.price.toDouble())
                .build()
            val event = CommerceEvent.Builder(com.mparticle.commerce.Product.DETAIL, mProduct)
                .build()
            MParticle.getInstance()?.logEvent(event)

        })
        productDetailViewModel.getProductById(productId)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun showAddToCartAlert() {
        val snackbar =
            Snackbar.make(
                binding.root,
                getString(R.string.detail_cta_added),
                Snackbar.LENGTH_LONG
            )
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)
        snackbar.view.layoutParams = layoutParams
        snackbar.setBackgroundTint(getColor(R.color.white))
        snackbar.setTextColor(getColor(R.color.black))
        snackbar.view.setPadding(20, 10, 20, 0)
        (snackbar.view.findViewById<TextView>(R.id.snackbar_text))?.textAlignment =
            View.TEXT_ALIGNMENT_TEXT_START
        snackbar.setActionTextColor(getColor(R.color.blue_4079FE))
        val snackbarActionTextView =
            snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_action) as TextView
        snackbarActionTextView.isAllCaps = false
        snackbarActionTextView.setTypeface(snackbarActionTextView.typeface, Typeface.BOLD)
        snackbar.setAction(getString(R.string.detail_cta_cart)) {
            setResult(Constants.RESULT_CODE_CART_ADDED)
            finish()
        }
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }

    @Composable
    fun ProductDetailActivityComposable(productDetailViewModel: ProductDetailViewModel) {
        val product by productDetailViewModel.detailResponseLiveData.observeAsState()
        var productSize = listOf<String>()
        var productColor = listOf<String>()
        val productQuantity = listOf("1", "2", "3", "4", "5", "6", "7", "8")

        product?.let {productItem->
            if (productItem.variants?.colors?.isNotEmpty() == true) {
                productColor = productItem.variants.colors
            }

            if (productItem.variants?.sizes?.isNotEmpty() == true) {
                productSize = productItem.variants.sizes
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
        ) {

            product?.label?.let {
                Text(
                    it,
                    style = typography.h1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 30.dp)
                )
            }

            Text(
                "$${product?.price}",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 30.dp)
                    .width(282.dp)
            )

            val image =
                loadPicture(imageUrl = product?.imageUrl, defaultImage = DEFAULT_PRODUCT_IMAGE).value
            image?.let { productImage ->
                Image(
                    bitmap = productImage.asImageBitmap(),
                    "productImage",
                    modifier = Modifier
                        .padding(start = 40.dp, end = 40.dp)
                        .fillMaxWidth()
                )
            }

            Row(Modifier.padding(top = 30.dp)) {
                if (productColor.isNotEmpty()) {
                    DropDownMenu("Color", 10, productColor, productDetailViewModel)
                }
                if (productSize.isNotEmpty()) {
                    DropDownMenu("Size", 0, productSize, productDetailViewModel)
                }
            }
            Row(Modifier.padding(top = 30.dp, bottom = 30.dp)) {
                DropDownMenu("Quantity", 10, productQuantity, productDetailViewModel)
                Box(
                    Modifier
                        .width(153.dp)
                        .height(55.dp)
                )
            }

            Button(
                onClick = {
                    product?.let { product ->
                        val sku =
                            "${product.id}-${productDetailViewModel.color}-${productDetailViewModel.size}"
                        val entity = CartItemEntity(
                            sku = sku,
                            id = product.id,
                            label = product.label,
                            imageUrl = product.imageUrl,
                            color = productDetailViewModel.color,
                            size = productDetailViewModel.size,
                            price = product.price,
                            quantity = productDetailViewModel.quantity
                        )
                        productDetailViewModel.addToCart(entity)
                    }
                },
                colors = ButtonDefaults.buttonColors(blue_4079FE),
                shape = Shapes.small,
                modifier = Modifier
                    .width(190.dp)
                    .height(50.dp)

            )
            {
                Text(
                    stringResource(R.string.detail_add_to_cart),
                    style = MaterialTheme.typography.button,
                    color = Color.White
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 30.dp)
            )

        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DropDownMenu(
        label: String,
        padding: Int,
        category: List<String>,
        productDetailViewModel: ProductDetailViewModel
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedValue by remember { mutableStateOf(category[0]) }
        val icon = R.drawable.ic_baseline_arrow_drop_down_24

        when (label) {
            "Color" -> productDetailViewModel.color = selectedValue
            "Size" -> productDetailViewModel.size = selectedValue
            "Quantity" -> productDetailViewModel.quantity = selectedValue.toInt()
        }

        Box(
            Modifier
                .width(153.dp)
                .height(56.dp)
                .padding(end = padding.dp),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.width(150.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedValue,
                    onValueChange = { },
                    label = { Text(text = label, color = Color.White) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = icon),
                            "dropDownIcon",
                            tint = Color.White,
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        textColor = Color.White,
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.background(Color.DarkGray)
                ) {
                    category.forEach { value ->
                        DropdownMenuItem(
                            onClick = {
                                selectedValue = value
                                expanded = false
                                when (label) {
                                    "Color" -> productDetailViewModel.color = selectedValue
                                    "Size" -> productDetailViewModel.size = selectedValue
                                    "Quantity" -> productDetailViewModel.quantity =
                                        selectedValue.toInt()
                                }
                            },
                        ) {
                            Text(text = value, color = Color.White)
                        }

                    }
                }
            }
        }
    }
}





