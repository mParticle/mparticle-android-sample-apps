package com.mparticle.example.higgsshopsampleapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.CheckoutActivity
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.fragments.adapters.CartItemCard
import com.mparticle.example.higgsshopsampleapp.utils.theme.Shapes
import com.mparticle.example.higgsshopsampleapp.utils.theme.blue_4079FE
import com.mparticle.example.higgsshopsampleapp.utils.theme.typography
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentCartBinding
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.viewmodels.CartViewModel

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private val TAG = "CartFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("")

        binding = DataBindingUtil.inflate<FragmentCartBinding>(
            inflater,
            R.layout.fragment_cart,
            container,
            false
        )
            .apply {
                composeView.setContent {
                    CartFragmentComposable()
                }
            }
        cartViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("View My Cart")

        cartViewModel.getTotalCartItems()
        cartViewModel.cartTotalLiveData.observe(
            viewLifecycleOwner
        ) { total ->
            (activity as MainActivity).updateBottomNavCartButtonText(total)
        }

        cartViewModel.cartResponseLiveData.observe(
            viewLifecycleOwner
        ) { items ->
            Log.d(TAG, "Size: " + items?.size)
            cartViewModel.getSubtotalPrice(this.requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.getCartItems()
    }

    @Composable
    fun CartFragmentComposable() {

        val context = LocalContext.current

        val items by cartViewModel.cartResponseLiveData.observeAsState()
        val subtotal by cartViewModel.cartSubtotalPriceLiveData.observeAsState()
        var clickable=0f

        val removeItem = { cartItem: CartItemEntity ->
            val entity = CartItemEntity(
                sku = cartItem.sku,
                id = cartItem.id,
                label = cartItem.label,
                imageUrl = cartItem.imageUrl,
                color = cartItem.color,
                size = cartItem.size,
                price = cartItem.price,
                quantity = cartItem.quantity
            )
            cartViewModel.removeFromCart(entity)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                stringResource(R.string.cart_title),
                color = Color.White,
                style = typography.h1,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(bottom = 30.dp)
            )
            if (items.isNullOrEmpty()) {
                clickable =0.3f
                Text(
                    stringResource(R.string.cart_0),
                    color = Color.White,
                    style = typography.h2,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(top = 30.dp)
                )
            }
            if (!items.isNullOrEmpty()) {
                clickable=1f
                LazyColumn {
                    items?.let { it ->
                        itemsIndexed(items = it) { _, item ->
                            CartItemCard(item = item, onClick = removeItem, isCheckout = false)
                        }
                    }
                }

            }
            Row(
                Modifier
                    .height(48.dp)
                    .width(344.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.cart_subtotal),
                    color = Color.White,
                    style = typography.h2,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .wrapContentHeight()
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "$$subtotal",
                    color = Color.White,
                    style = typography.h2,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End)
                        .wrapContentHeight()
                )
            }
            Divider(
                thickness = 1.dp,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            )
            Button(
                onClick = {
                    if (!items.isNullOrEmpty()) {
                        val intent = Intent(context, CheckoutActivity::class.java)
                        (context as MainActivity).activityResultLaunch?.launch(intent)

                    } },

                colors = ButtonDefaults.buttonColors(blue_4079FE),
                shape = Shapes.small,
                modifier = Modifier
                    .width(190.dp)
                    .height(50.dp)
                    .alpha(clickable)

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
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(262.dp)
                    .wrapContentHeight()
                    .alpha(.6f)
                    .padding(top = 20.dp)
            )
        }
    }
}







