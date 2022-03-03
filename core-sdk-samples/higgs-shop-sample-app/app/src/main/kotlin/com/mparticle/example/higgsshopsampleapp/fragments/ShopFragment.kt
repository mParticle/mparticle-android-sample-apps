package com.mparticle.example.higgsshopsampleapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.adapters.ProductItemsAdapter
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentShopBinding
import com.mparticle.example.higgsshopsampleapp.viewmodels.ShopViewModel


class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var shopViewModel: ShopViewModel
    private val TAG = "ShopFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("");
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        shopViewModel =
            ViewModelProvider(this).get(ShopViewModel::class.java)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("Shop")

        _binding?.rvProductList?.layoutManager = LinearLayoutManager(activity)

        shopViewModel.inventoryResponseLiveData.observe(viewLifecycleOwner, Observer { products ->
            Log.d(TAG, "Size: " + products?.size)

            if (products == null) {
                return@Observer
            }

            val adapter = ProductItemsAdapter()
            adapter.list = products.toMutableList()

            _binding?.rvProductList?.let { listView ->
                if (listView.adapter == null) {
                    listView.adapter = adapter
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
        })
        shopViewModel.getProducts(this.requireContext())
    }
}