package com.mparticle.example.higgsshopsampleapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.activities.CheckoutActivity
import com.mparticle.example.higgsshopsampleapp.adapters.CartItemsAdapter
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentCartBinding
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity
import com.mparticle.example.higgsshopsampleapp.viewmodels.CartViewModel

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
        val btnCTA = activity?.findViewById(R.id.cart_cta) as Button

        cartViewModel.cartSubtotalPriceLiveData.observe(
            viewLifecycleOwner,
            { subTotalPrice ->
                _binding?.tvCartSubtotalPrice?.text = "$${subTotalPrice}"
            })

        cartViewModel.cartTotalLiveData.observe(
            viewLifecycleOwner,
            { total ->
                (activity as MainActivity).updateBottomNavCartButtonText(total)
            })

        cartViewModel.cartResponseLiveData.observe(viewLifecycleOwner, Observer { items ->
            Log.d(TAG, "Size: " + items?.size)

            if (items == null || items.size == 0) {
                _binding?.tvCart0?.visibility = View.VISIBLE
                _binding?.rvCartList?.visibility = View.GONE
                _binding?.tvCartSubtotalPrice?.text = getString(R.string.detail_price)
                btnCTA.isClickable = false
                btnCTA.alpha = 0.3F
                (activity as MainActivity).updateBottomNavCartButtonText(0)
                return@Observer
            }
            btnCTA.setBackgroundResource(R.drawable.rounded_button)
            _binding?.tvCart0?.visibility = View.GONE
            _binding?.rvCartList?.visibility = View.VISIBLE
            val adapter = CartItemsAdapter()
            adapter.list = items.toMutableList()
            cartViewModel.getTotalCartItems(this.requireContext())

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

            btnCTA.isClickable = true
            btnCTA.alpha = 1.0F
            btnCTA.setOnClickListener {
                val intent = Intent(activity, CheckoutActivity::class.java)
                (activity as MainActivity).activityResultLaunch?.launch(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.getCartItems(this.requireContext())
    }
}