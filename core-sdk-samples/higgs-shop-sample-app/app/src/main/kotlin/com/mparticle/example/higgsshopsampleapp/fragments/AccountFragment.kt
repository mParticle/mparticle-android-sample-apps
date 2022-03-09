package com.mparticle.example.higgsshopsampleapp.fragments

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.databinding.FragmentAccountBinding
import com.mparticle.example.higgsshopsampleapp.viewmodels.AccountViewModel
import com.mparticle.identity.IdentityApiRequest


class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setActionBarTitle("");
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MParticle.getInstance()?.logScreen("My Account")
        val btnCTA = activity?.findViewById(R.id.account_cta) as Button

        accountViewModel.loggedInResponseLiveData.observe(
            viewLifecycleOwner,
            { loggedIn ->
                when(loggedIn) {
                    false -> {
                        btnCTA.text = getString(R.string.account_cta_login)
                        showIdentityAlert("Logout Successful")
                    }
                    true -> {
                        btnCTA.text = getString(R.string.account_cta_logout)
                        showIdentityAlert("Login Successful")
                    }
                }
            })

        btnCTA.setOnClickListener {
            when(MParticle.getInstance()?.Identity()?.currentUser?.isLoggedIn) {
                false -> {
                    //no user so login
                    val identityRequest = IdentityApiRequest.withEmptyUser()
                        .email("higgs@example.com")
                        .customerId("123456")
                        .build()
                    MParticle.getInstance()?.Identity()?.login(identityRequest)?.addSuccessListener {
                        accountViewModel.login()
                    }
                }
                true -> {
                    //user exists in sample app so logout
                    MParticle.getInstance()?.Identity()?.logout()?.addSuccessListener {
                        accountViewModel.logout()
                    }
                }
                else -> {}
            }
        }
    }

    fun showIdentityAlert(message: String) {
        val parentLayout: View = requireActivity().findViewById(android.R.id.content)
        val snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT)
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)

        val tv = (snackbar.view.findViewById<TextView>(R.id.snackbar_text))
        tv?.textAlignment = View.TEXT_ALIGNMENT_CENTER

        snackbar.setBackgroundTint(requireContext().getColor(R.color.blue_4079FE))
        snackbar.setTextColor(requireContext().getColor(R.color.white))
        snackbar.view.layoutParams = layoutParams
        snackbar.view.setPadding(0, 10, 0, 0)
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }
}