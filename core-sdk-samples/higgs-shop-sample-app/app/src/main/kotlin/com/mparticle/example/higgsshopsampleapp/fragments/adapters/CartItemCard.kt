package com.mparticle.example.higgsshopsampleapp.fragments.adapters

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.utils.theme.blue_4079FE
import com.mparticle.example.higgsshopsampleapp.utils.theme.gray_999999
import com.mparticle.example.higgsshopsampleapp.utils.theme.typography
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.utils.DEFAULT_PRODUCT_IMAGE
import com.mparticle.example.higgsshopsampleapp.utils.loadPicture


@Composable
fun CartItemCard(
    item: CartItemEntity,
    onClick: (CartItemEntity) -> Unit? = {},
    isCheckout: Boolean
) {
    Card(
        modifier = Modifier
            .width(384.dp)
            .height(88.dp)
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Black)
                .padding(top = 10.dp)
        ) {
            val image =
                loadPicture(imageUrl = item.imageUrl, defaultImage = DEFAULT_PRODUCT_IMAGE).value
            image?.let { productImage ->
                Image(
                    bitmap = productImage.asImageBitmap(),
                    "productImage",
                    modifier = Modifier
                        .height(56.dp)
                        .padding(start = 15.dp, end = 15.dp)
                )
            }

            Column() {
                Row(Modifier.padding(end = 10.dp)) {
                    Text(
                        item.label,
                        style = typography.button,
                        modifier = Modifier
                            .wrapContentWidth()
                    )
                    Spacer(Modifier.weight(1f))

                    Text(
                        "$${item.price}",
                        style = typography.button,
                        modifier = Modifier
                            .wrapContentWidth()
                    )
                }
                Text(
                    "Qty: ${item.quantity}",
                    style = typography.button,
                    color = gray_999999,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                )
                if (!isCheckout) {
                    Text(
                        stringResource(id = R.string.cart_remove),
                        style = typography.button,
                        color = blue_4079FE,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .clickable { onClick(item) }
                            .padding(top = 5.dp)
                    )
                }
                if (isCheckout && !item.color.isNullOrEmpty()) {
                    Text(
                        "${item.color}, ${item.size}",
                        style = typography.button,
                        color = gray_999999,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(top = 5.dp)
                    )
                }

                Divider(
                    thickness = 1.dp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(
                        top = 23.dp,
                        end = 10.dp
                    )
                )
            }
        }
    }
}