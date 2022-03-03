package com.mparticle.example.higgsshopsampleapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.adapters.CartItemsAdapter
import com.mparticle.example.higgsshopsampleapp.adapters.ProductItemsAdapter
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentCartBinding
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.viewmodels.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartViewModel: CartViewModel
    private val TAG = "CartFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("");
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        cartViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("View My Cart")

        _binding?.rvCartList?.layoutManager = LinearLayoutManager(activity)

        cartViewModel.cartSubtotalPriceLiveData.observe(viewLifecycleOwner, Observer { subTotalPrice ->
            _binding?.tvCartSubtotalPrice?.text = "$${subTotalPrice}"
        })

        cartViewModel.cartResponseLiveData.observe(viewLifecycleOwner, Observer { items ->
            Log.d(TAG, "Size: " + items?.size)

            if (items == null) {
                return@Observer
            }

            val adapter = CartItemsAdapter()
            adapter.list = items.toMutableList()

            _binding?.rvCartList?.let { listView ->
                if (listView.adapter == null) {
                    listView.adapter = adapter.also { adapter ->
                        adapter.onItemClicked = { cartItem, position ->
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
                            cartViewModel.removeFromCart(requireContext(), entity)
                            adapter.list.removeAt(position)
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    adapter.notifyDataSetChanged()
                }
                cartViewModel.getSubtotalPrice(this.requireContext())
            }
        })
        cartViewModel.getCartItems(this.requireContext())
    }
}