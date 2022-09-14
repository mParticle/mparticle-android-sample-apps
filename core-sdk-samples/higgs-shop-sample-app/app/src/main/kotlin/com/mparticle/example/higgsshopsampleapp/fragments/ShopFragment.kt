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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.activities.ProductDetailActivity
import com.mparticle.example.higgsshopsampleapp.fragments.adapters.ShopItemCard
import com.mparticle.example.higgsshopsampleapp.utils.theme.typography
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentShopBinding
import com.mparticle.example.higgsshopsampleapp.repositories.network.models.Product
import com.mparticle.example.higgsshopsampleapp.utils.Constants
import com.mparticle.example.higgsshopsampleapp.viewmodels.ShopViewModel


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var shopViewModel: ShopViewModel
    private val TAG = "ShopFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("")
        binding = DataBindingUtil.inflate<FragmentShopBinding>(
            inflater,
            R.layout.fragment_shop,
            container,
            false
        )
            .apply {
                composeView.setContent {
                    ShopFragmentComposable()
                }
            }
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("Shop")

        shopViewModel.getProducts(this.requireContext())
        shopViewModel.getTotalCartItems(this.requireContext())

        shopViewModel.cartTotalSizeResponseLiveData.observe(viewLifecycleOwner, Observer { total ->
            Log.d(TAG, "Total: $total")

            if (total == null || total == 0) {
                (activity as MainActivity).updateBottomNavCartButtonText(0)
                return@Observer
            }
            (activity as MainActivity).updateBottomNavCartButtonText(total)
        })

        shopViewModel.inventoryResponseLiveData.observe(
            viewLifecycleOwner
        ) { products ->
            Log.d(TAG, "Size: " + products?.size)
        }
    }


    @Composable
    fun ShopFragmentComposable() {
        val products by shopViewModel.inventoryResponseLiveData.observeAsState()
        val context = LocalContext.current

        val navigate = { product: Product ->
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(
                Constants.PRODUCT_ID,
                product.id.toString().toIntOrNull() ?: 0
            )
            (context as MainActivity).activityResultLaunch?.launch(intent)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Text(
                stringResource(R.string.shop_title),
                color = Color.White,
                style = typography.h1,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(bottom = 30.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .width(343.dp)
                    .padding(bottom = 90.dp)
            ) {
                products?.let { it ->
                    itemsIndexed(items = it) { _, product ->
                        ShopItemCard(product = product, onClick = navigate)
                    }
                }
            }
        }
    }
}






