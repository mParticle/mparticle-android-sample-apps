package com.mparticle.example.higgsshopsampleapp.fragments.adapters

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mparticle.MParticle
import com.mparticle.commerce.CommerceEvent
import com.mparticle.commerce.Impression
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.utils.theme.blue_4079FE
import com.mparticle.example.higgsshopsampleapp.utils.theme.typography
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import com.mparticle.example.higgsshopsampleapp.utils.DEFAULT_PRODUCT_IMAGE
import com.mparticle.example.higgsshopsampleapp.utils.loadPicture


@Composable
fun ShopItemCard(product: Product, onClick:(Product) -> Unit?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onClick(product)}
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            val image = loadPicture(imageUrl = product.imageUrl, defaultImage = DEFAULT_PRODUCT_IMAGE).value
            image?.let { productImage ->
                Image(
                    bitmap = productImage.asImageBitmap(),
                    "productImage",
                    modifier = Modifier
                        .width(343.dp)
                        .height(264.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(blue_4079FE)
                    .height(54.dp)
            ) {
                Text(
                    product.label,
                    color = Color.White,
                    style = typography.button,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .width(200.dp)
                        .padding(start = 15.dp)
                        .align(Alignment.CenterVertically)
                )
                Image(
                    painter = painterResource(id = R.drawable.icon_right_arrow),
                    "rightArrowImage",
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentWidth(Alignment.End)
                        .align(Alignment.CenterVertically)
                )

            }
            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxWidth()
                    .height(20.dp)
            ) {}
        }
    }

    val mpProduct = com.mparticle.commerce.Product.Builder(
        product.label,
        product.id.toString(), product.price.toDouble()
    )
        .build()
    Impression("Products List", mpProduct).let {
        CommerceEvent.Builder(it).build()
    }.let {
        MParticle.getInstance()?.logEvent(it)
    }
}
